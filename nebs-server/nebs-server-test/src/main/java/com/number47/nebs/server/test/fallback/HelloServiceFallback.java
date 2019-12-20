package com.number47.nebs.server.test.fallback;

import com.number47.nebs.server.test.service.IHelloService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author number47
 * @date 2019/11/24 13:47
 * @description
 */
@Slf4j
@Component
public class HelloServiceFallback implements FallbackFactory<IHelloService> {

	@Override
	public IHelloService create(Throwable throwable) {
		/*return new IHelloService() {
			@Override
			public String hello(String name) {
				log.error("调用nebs-server-system服务出错", throwable);
				return "调用出错";
			}
		};*/
		return name -> {
			log.error("调用nebs-server-system服务出错", throwable);
			return "调用出错";
		};
	}
}