package com.javaee.sale.dto;

import lombok.Data;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private java.math.BigDecimal balance;
    private Integer role;
    private String token;
}
