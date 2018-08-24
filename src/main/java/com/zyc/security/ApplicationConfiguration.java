package com.zyc.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zyc.baselibs.SpringContextHolder;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public SpringContextHolder springContextHolder(ApplicationContext applicationContext) {
		return new SpringContextHolder(applicationContext);
	}
}
