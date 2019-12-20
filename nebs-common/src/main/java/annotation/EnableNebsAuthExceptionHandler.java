package annotation;

import configure.NebsAuthExceptionConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(NebsAuthExceptionConfigure.class)
public @interface EnableNebsAuthExceptionHandler {
}
