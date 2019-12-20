package exception;

/**
 * @author number47
 * @date 2019/11/25 00:45
 * @description 验证码类型异常
 */
public class ValidateCodeException extends Exception{

	private static final long serialVersionUID = 7514854456967620043L;

	public ValidateCodeException(String message){
		super(message);
	}
}