package entity.system;

import annotation.DateTime;
import annotation.IsMobile;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import converter.TimeConverter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author number47
 * @date 2019/11/24 20:56
 * @description 用户
 */
@Data
@TableName("t_user")
@Excel("用户信息表")
public class SystemUser implements Serializable {

	private static final long serialVersionUID = -4352868070794165001L;
	/**
	 * 用户状态：有效
	 */
	public static final String STATUS_VALID = "1";
	/**
	 * 用户状态：锁定
	 */
	public static final String STATUS_LOCK = "0";
	/**
	 * 默认头像
	 */
	public static final String DEFAULT_AVATAR = "default.jpg";
	/**
	 * 默认密码
	 */
	public static final String DEFAULT_PASSWORD = "1234qwer";
	/**
	 * 性别男
	 */
	public static final String SEX_MALE = "0";
	/**
	 * 性别女
	 */
	public static final String SEX_FEMALE = "1";
	/**
	 * 性别保密
	 */
	public static final String SEX_UNKNOW = "2";

	/**
	 * 用户 ID
	 */
	@TableId(value = "USER_ID", type = IdType.AUTO)
	private Long userId;

	/**
	 * 用户名
	 */
	@TableField("USERNAME")
	@Size(min = 4, max = 10, message = "{range}")
	@ExcelField(value = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@TableField("PASSWORD")
	private String password;

	/**
	 * 部门 ID
	 */
	@TableField("DEPT_ID")
	private Long deptId;

	/**
	 * 邮箱
	 */
	@TableField("EMAIL")
	@Size(max = 50, message = "{noMoreThan}")
	@Email(message = "{email}")
	@ExcelField(value = "邮箱")
	private String email;

	/**
	 * 联系电话
	 */
	@TableField("MOBILE")
	@IsMobile(message = "{mobile}")
	@ExcelField(value = "联系电话")
	private String mobile;

	/**
	 * 状态 0锁定 1有效
	 */
	@TableField("STATUS")
	@NotBlank(message = "{required}")
	@ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
	private String status;

	/**
	 * 创建时间
	 */
	@TableField("CREATE_TIME")
	@DateTime //日期格式校验
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  //入参格式化
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  //出参格式化
	@ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
	private Date createTime;

	/**
	 * 修改时间
	 */
	@TableField("MODIFY_TIME")
	@DateTime //日期格式校验
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  //入参格式化
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  //出参格式化
	@ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
	private Date modifyTime;

	/**
	 * 最近访问时间
	 */
	@TableField("LAST_LOGIN_TIME")
	@DateTime //日期格式校验
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  //入参格式化
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  //出参格式化
	@ExcelField(value = "最近访问时间", writeConverter = TimeConverter.class)
	private Date lastLoginTime;

	/**
	 * 性别 0男 1女 2 保密
	 */
	@TableField("SSEX")
	@NotBlank(message = "{required}")
	@ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
	private String sex;

	/**
	 * 头像
	 */
	@TableField("AVATAR")
	private String avatar;

	/**
	 * 描述
	 */
	@TableField("DESCRIPTION")
	@Size(max = 100, message = "{noMoreThan}")
	@ExcelField(value = "个人描述")
	private String description;

	/**
	 * 部门名称
	 */
	@TableField(exist = false)
	private String deptName;

	@TableField(exist = false)
	private String createTimeFrom;
	@TableField(exist = false)
	private String createTimeTo;
	/**
	 * 角色 ID
	 */
	@NotBlank(message = "{required}")
	@TableField(exist = false)
	private String roleId;

	@TableField(exist = false)
	private String roleName;

	@TableField(exist = false)
	private String deptIds;

}