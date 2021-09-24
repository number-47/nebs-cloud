package validator;

import annotation.DateTime;
import lombok.extern.log4j.Log4j2;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: nebs-common
 * @description: 日期校验
 * @author: number47
 * @create: 2020-10-26 14:16
 **/
@Log4j2
public class DateTimeValidator implements ConstraintValidator<DateTime, Date> {

    private DateTime dateTime;

    @Override
    public void initialize(DateTime constraintAnnotation) {
        this.dateTime=constraintAnnotation;
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext constraintValidatorContext) {
        // 如果value为空则不进行格式验证，为空的验证可以使用@NotBlank @NotNull @NotEmpty等注解
        if (value == null){
            return true;
        }
        String format = dateTime.format();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.format(value);
        }catch (IllegalArgumentException e){
            log.error("检验日期出错",e);
            return false;
        }
        return true;
    }
}
