package handle;

import entity.NebsResponse;
import exception.NebsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Set;

/**
 * @author number47
 * @date 2019/11/22 03:30
 * @description 全局异常处理指的是全局处理Controller层抛出来的异常
 */
@Slf4j
public class BaseExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public NebsResponse handleException(Exception e) {
		log.error("系统内部异常，异常信息", e);
		return new NebsResponse().message("系统内部异常");
	}

	@ExceptionHandler(value = NebsException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public NebsResponse handleNebsAuthException(NebsException e) {
		log.error("系统错误", e);
		return new NebsResponse().message(e.getMessage());
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public NebsResponse handleAccessDeniedException(){
		return new NebsResponse().message("没有权限访问该资源");
	}

	/**
	 * 统一处理请求参数校验(普通传参)
	 *
	 * @param e ConstraintViolationException
	 * @return NebsResponse
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public NebsResponse handleConstraintViolationException(ConstraintViolationException e) {
		StringBuilder message = new StringBuilder();
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			Path path = violation.getPropertyPath();
			String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
			message.append(pathArr[1]).append(violation.getMessage()).append(",");
		}
		message = new StringBuilder(message.substring(0, message.length() - 1));
		return new NebsResponse().message(message.toString());
	}
}
