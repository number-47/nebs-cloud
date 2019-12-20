package configure;

import handle.NebsAccessDeniedHandler;
import handle.NebsAuthExceptionEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author number47
 * @date 2019/11/22 03:11
 * @description
 */
public class NebsAuthExceptionConfigure {

	@Bean
	@ConditionalOnMissingBean(name = "accessDeniedHandler") //当IOC容器中没有指定名称或类型的Bean的时候，就注册它
	public NebsAccessDeniedHandler accessDeniedHandler() {
		return new NebsAccessDeniedHandler();
	}

	@Bean
	@ConditionalOnMissingBean(name = "authenticationEntryPoint")
	public NebsAuthExceptionEntryPoint authenticationEntryPoint() {
		return new NebsAuthExceptionEntryPoint();
	}
}
