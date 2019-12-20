package annotation;

import configure.NebsOAuth2FeignConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(NebsOAuth2FeignConfigure.class)
public @interface EnableNebsOauth2FeignClient {

}
