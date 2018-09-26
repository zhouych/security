package com.zyc.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zyc.baselibs.SpringContextUtils;

//@EnableDiscoveryClient
//@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages = { "com.zyc.baselibs.dao", "com.zyc.security.dao" })
//@ComponentScan(basePackages = { "com.zyc.security", "com.zyc.baselibs" })
public class SecurityApplication {

	public static void main(String[] args) {
		SpringContextUtils.setApplicationContext(SpringApplication.run(SecurityApplication.class, args));
	}
}
