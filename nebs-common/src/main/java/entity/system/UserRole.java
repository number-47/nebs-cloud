package entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author number47
 * @date 2019/12/1 23:50
 * @description
 */
@Data
@TableName("t_user_role")
public class UserRole implements Serializable {

	private static final long serialVersionUID = -3166012934498268403L;

	@TableField(value = "USER_ID")
	private Long userId;

	@TableField(value = "ROLE_ID")
	private Long roleId;

}
