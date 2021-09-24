package selector;

import configure.NebsAuthExceptionConfigure;
import configure.NebsOAuth2FeignConfigure;
import configure.NebsServerProtectConfigure;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author number47
 * @date 2019/11/24 14:38
 * @description 1.@EnableNebsServerProtect，开启微服务防护，避免客户端绕过网关直接请求微服务；
 * 				2.@EnableNebsOauth2FeignClient，开启带令牌的Feign请求，避免微服务内部调用出现401异常；
 * 				3.@EnableNebsAuthExceptionHandler，认证类型异常翻译。
 *
 *三个注解都是通过@Enable类型注解来将配置类注册到IOC容器中，所以我们现在要做的就是将这三个配置类一次性都注册到IOC容器中。在Spring中，要将多个类进行注册，可以使用selector的方式
 */
public class NebsCloudApplicationSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return new String[]{
				NebsAuthExceptionConfigure.class.getName(),
				NebsOAuth2FeignConfigure.class.getName(),
				NebsServerProtectConfigure.class.getName()
		};
	}
}