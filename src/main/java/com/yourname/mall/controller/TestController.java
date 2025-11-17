package com.yourname.mall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "✅ 基础框架测试成功！时间：" + System.currentTimeMillis();
    }

    @GetMapping("/")
    public String home() {
        return "🎉 电商系统基础框架运行正常！";
    }
}