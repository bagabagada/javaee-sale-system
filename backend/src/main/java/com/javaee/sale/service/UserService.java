package com.javaee.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaee.sale.dto.LoginRequest;
import com.javaee.sale.dto.UserVO;
import com.javaee.sale.entity.User;

public interface UserService extends IService<User> {
    UserVO login(LoginRequest request);

    UserVO register(User user);
    
    User recharge(Long userId, java.math.BigDecimal amount);
}
