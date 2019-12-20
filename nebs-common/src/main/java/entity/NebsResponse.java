package entity;

import java.util.HashMap;

/**
 * @author number47
 * @date 2019/11/22 03:09
 * @description
 */
public class NebsResponse extends HashMap<String, Object> {

	private static final long serialVersionUID = -8713837118340960775L;

	public NebsResponse message(String message) {
		this.put("message", message);
		return this;
	}

	public NebsResponse data(Object data) {
		this.put("data", data);
		return this;
	}

	@Override
	public NebsResponse put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public String getMessage() {
		return String.valueOf(get("message"));
	}

	public Object getData() {
		return get("data");
	}
}
