package configure;

import interceptor.NebsServerProtectInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author number47
 * @date 2019/11/24 14:27 微服务防护配置
 * @description 让该过滤器生效我们需要定义一个配置类来将它注册到Spring IOC容器中
 */
public class NebsServerProtectConfigure implements WebMvcConfigurer {

	@Bean
	@ConditionalOnMissingBean(value = PasswordEncoder.class)
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public HandlerInterceptor nebsServerProtectInterceptor() {
		return new NebsServerProtectInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(nebsServerProtectInterceptor());
	}
}