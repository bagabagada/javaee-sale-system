package com.javaee.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaee.sale.common.BusinessException;
import com.javaee.sale.dto.LoginRequest;
import com.javaee.sale.dto.UserVO;
import com.javaee.sale.entity.User;
import com.javaee.sale.mapper.UserMapper;
import com.javaee.sale.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // Simple in-memory token store. In production, use Redis.
    public static final Map<String, User> tokenMap = new ConcurrentHashMap<>();

    @Override
    public UserVO login(LoginRequest request) {
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String inputPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!inputPassword.equals(user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // Generate token
        String token = UUID.randomUUID().toString();
        tokenMap.put(token, user);

        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setBalance(user.getBalance());
        userVO.setRole(user.getRole());
        userVO.setToken(token);

        return userVO;
    }

    @Override
    public UserVO register(User user) {
        long count = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        // Default nickname to username if empty
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname(user.getUsername());
        }
        // Default role to User (0)
        if (user.getRole() == null) {
            user.setRole(0);
        }
        if (user.getBalance() == null) {
            user.setBalance(new java.math.BigDecimal("10000.00"));
        }
        
        this.save(user);

        // Auto login after register
        String token = UUID.randomUUID().toString();
        tokenMap.put(token, user);

        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setBalance(user.getBalance());
        userVO.setRole(user.getRole());
        userVO.setToken(token);

        return userVO;
    }

    @Override
    public User recharge(Long userId, java.math.BigDecimal amount) {
        if (amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setBalance(user.getBalance().add(amount));
        this.updateById(user);
        
        // Update token cache if exists (optional but good for consistency)
        for (Map.Entry<String, User> entry : tokenMap.entrySet()) {
            if (entry.getValue().getId().equals(userId)) {
                entry.getValue().setBalance(user.getBalance());
            }
        }
        
        return user;
    }
}
