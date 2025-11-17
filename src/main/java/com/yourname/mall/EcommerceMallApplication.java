package com.yourname.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class EcommerceMallApplication {

    @Autowired(required = false)
    private DataSource dataSource;

    @PostConstruct
    public void checkDatabase() {
        if (dataSource != null) {
            try {
                Connection conn = dataSource.getConnection();
                System.out.println("✅ 数据库连接成功！");
                System.out.println("✅ 数据库URL: " + conn.getMetaData().getURL());
                conn.close();
            } catch (Exception e) {
                System.out.println("❌ 数据库连接失败: " + e.getMessage());
            }
        } else {
            System.out.println("❌ 数据源未配置或注入失败");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(EcommerceMallApplication.class, args);
    }
}