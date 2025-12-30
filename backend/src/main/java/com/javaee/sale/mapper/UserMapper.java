package com.javaee.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaee.sale.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
