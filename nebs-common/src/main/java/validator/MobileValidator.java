package validator;

/**
 * @author number47
 * @date 2019/12/2 00:04
 * @description
 */

import annotation.IsMobile;
import entity.constant.RegexpConstant;
import org.apache.commons.lang3.StringUtils;
import util.NebsUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author number47
 * @date 2019/11/22 03:04
 * @description 校验是否为合法的手机号码
 */
public class MobileValidator implements ConstraintValidator<IsMobile, String> {

	@Override
	public void initialize(IsMobile isMobile) {
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		try {
			if (StringUtils.isBlank(s)) {
				return true;
			} else {
				String regex = RegexpConstant.MOBILE_REG;
				return NebsUtil.match(regex, s);
			}
		} catch (Exception e) {
			return false;
		}
	}
}
