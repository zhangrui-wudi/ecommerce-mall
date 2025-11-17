package com.yourname.mall.controller;

import com.yourname.mall.common.Result;
import com.yourname.mall.common.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache-test")
public class CacheTestController {

    @Autowired
    private RedisService redisService;

    /**
     * 测试Redis连接
     */
    @GetMapping("/ping")
    public Result<String> ping() {
        redisService.set("test:ping", "pong", 60);
        String result = (String) redisService.get("test:ping");
        return Result.success("Redis连接测试: " + result);
    }

    /**
     * 设置缓存
     */
    @PostMapping("/set")
    public Result<String> setCache(@RequestParam String key,
                                   @RequestParam String value,
                                   @RequestParam(defaultValue = "0") Long expire) {
        if (expire > 0) {
            redisService.set(key, value, expire);
        } else {
            redisService.set(key, value);
        }
        return Result.success("设置成功: " + key);
    }

    /**
     * 获取缓存
     */
    @GetMapping("/get")
    public Result<Object> getCache(@RequestParam String key) {
        Object value = redisService.get(key);
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("value", value);
        result.put("exists", value != null);
        if (value != null) {
            result.put("expire", redisService.getExpire(key));
        }
        return Result.success(result);
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/delete")
    public Result<String> deleteCache(@RequestParam String key) {
        Boolean result = redisService.delete(key);
        return Result.success(result ? "删除成功" : "键不存在");
    }

    /**
     * 缓存统计信息
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> cacheStats() {
        Map<String, Object> stats = new HashMap<>();

        // 测试一些关键缓存键
        String[] testKeys = {"hot:products", "all:categories"};
        for (String key : testKeys) {
            Map<String, Object> keyInfo = new HashMap<>();
            keyInfo.put("exists", redisService.hasKey(key));
            if (redisService.hasKey(key)) {
                keyInfo.put("expire", redisService.getExpire(key));
            }
            stats.put(key, keyInfo);
        }

        return Result.success(stats);
    }
}