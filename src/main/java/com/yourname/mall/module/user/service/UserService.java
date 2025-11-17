package com.yourname.mall.module.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourname.mall.common.Result;
import com.yourname.mall.common.exception.BusinessException;
import com.yourname.mall.module.user.entity.User;
import com.yourname.mall.module.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    public Result<User> register(String username, String password, String email) {
        System.out.println("=== 开始用户注册流程 ===");
        System.out.println("注册参数 - 用户名: " + username + ", 邮箱: " + email);

        // 1. 校验用户名是否已存在
        System.out.println("正在检查用户名是否存在: " + username);
        LambdaQueryWrapper<User> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(User::getUsername, username);
        Long usernameCount = userMapper.selectCount(usernameWrapper);
        System.out.println("用户名检查结果: " + usernameCount + " 条记录");

        if (usernameCount > 0) {
            System.out.println("❌ 用户名已存在，注册失败: " + username);
            throw new BusinessException(1001, "用户名已存在: " + username);
        }
        System.out.println("✅ 用户名可用: " + username);

        // 2. 校验邮箱是否已存在
        System.out.println("正在检查邮箱是否存在: " + email);
        LambdaQueryWrapper<User> emailWrapper = new LambdaQueryWrapper<>();
        emailWrapper.eq(User::getEmail, email);
        Long emailCount = userMapper.selectCount(emailWrapper);
        System.out.println("邮箱检查结果: " + emailCount + " 条记录");

        if (emailCount > 0) {
            System.out.println("❌ 邮箱已存在，注册失败: " + email);
            throw new BusinessException(1002, "邮箱已存在: " + email);
        }
        System.out.println("✅ 邮箱可用: " + email);

        // 3. 密码加密
        System.out.println("开始密码加密...");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        System.out.println("✅ 密码加密完成");
        System.out.println("原始密码长度: " + password.length());
        System.out.println("加密后密码哈希: " + encodedPassword);

        // 4. 创建用户对象
        System.out.println("创建用户对象...");
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(encodedPassword);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        System.out.println("✅ 用户对象创建完成");

        // 5. 保存到数据库
        System.out.println("正在保存用户到数据库...");
        int insertResult = userMapper.insert(user);
        System.out.println("数据库插入结果: " + insertResult + " 条记录受影响");
        System.out.println("✅ 用户注册成功，用户ID: " + user.getId());

        System.out.println("=== 用户注册流程完成 ===");

        return Result.success(user);
    }
}