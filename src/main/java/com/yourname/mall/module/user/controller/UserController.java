package com.yourname.mall.module.user.controller;

import com.yourname.mall.common.Result;
import com.yourname.mall.module.user.entity.User;
import com.yourname.mall.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<User> register(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String email) {
        System.out.println("接收到注册请求: " + username + ", " + email);
        return userService.register(username, password, email);
    }

    @GetMapping("/test")
    public String test() {
        return "用户服务正常运行 - " + System.currentTimeMillis();
    }
}