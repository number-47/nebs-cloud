package annotation;

import org.springframework.context.annotation.Import;
import selector.NebsCloudApplicationSelector;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(NebsCloudApplicationSelector.class)
public @interface NebsCloudApplication {
}
