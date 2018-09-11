package com.zyc.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.zyc.baselibs.SpringContextUtils;

@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages = { "com.zyc.baselibs.dao", "com.zyc.security.dao" })
public class SecurityApplication {

	public static void main(String[] args) {
		SpringContextUtils.setApplicationContext(SpringApplication.run(SecurityApplication.class, args));
	}
}
