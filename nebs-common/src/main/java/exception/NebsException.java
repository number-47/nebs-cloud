package exception;

/**
 * @author number47
 * @date 2019/11/22 03:32
 * @description NEBS系统异常
 */
public class NebsException extends Exception{


	private static final long serialVersionUID = -6916154462432027437L;

	public NebsException(String message) {
		super(message);
	}
}
