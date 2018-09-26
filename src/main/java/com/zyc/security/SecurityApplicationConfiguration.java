package com.zyc.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zyc.baselibs.SpringContextHolder;
import com.zyc.baselibs.aopv.ParamVerificationAspect;
import com.zyc.baselibs.aopv.VerificationRulerContainer;

@Configuration
public class SecurityApplicationConfiguration {

	@Bean
	public SpringContextHolder springContextHolder(ApplicationContext applicationContext) {
		return new SpringContextHolder(applicationContext);
	}

	/**
	 * 实例化<code>ParamVerificationAspect</code>（参数验证切面），开启参数验证Spring AOP。
	 * @see 
	 * <code>ParamVerificationAspect</code>类是包含Component注解的，实例化的另一种方式：</br>
	 * 在SpringBoot启动类，通过<code>ComponentScan</code>注解配置<code>ParamVerificationAspect</code>的包名进行扫描。
	 * @return
	 */
	@Bean
	public ParamVerificationAspect paramVerificationAspect() {
		return new ParamVerificationAspect();
	}
	
	@Bean
	public VerificationRulerContainer verificationRulerContainer() {
		return new VerificationRulerContainer();
	}
}
