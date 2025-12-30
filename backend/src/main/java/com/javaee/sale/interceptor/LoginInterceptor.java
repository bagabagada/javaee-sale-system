package com.javaee.sale.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaee.sale.common.Result;
import com.javaee.sale.entity.User;
import com.javaee.sale.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || token.isEmpty()) {
            sendError(response, "未授权：未提供令牌");
            return false;
        }

        User user = UserServiceImpl.tokenMap.get(token);
        if (user == null) {
            sendError(response, "未授权：无效令牌");
            return false;
        }

        // Store user in request attribute for Controller access if needed
        request.setAttribute("currentUser", user);
        return true;
    }

    private void sendError(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Result<String> result = Result.error(401, message);
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
