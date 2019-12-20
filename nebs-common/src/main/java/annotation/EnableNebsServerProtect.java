package annotation;

import configure.NebsServerProtectConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(NebsServerProtectConfigure.class)
public @interface EnableNebsServerProtect {
}
