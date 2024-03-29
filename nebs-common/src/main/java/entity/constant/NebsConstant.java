package entity.constant;

/**
 * FEBS系统常量类
 *
 * @author MrBird
 */
public class NebsConstant {

    /**
     * 排序规则：降序
     */
    public static final String ORDER_DESC = "descending";
    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "ascending";

    /**
     * Gateway请求头TOKEN名称（不要有空格）
     */
    public static final String GATEWAY_TOKEN_HEADER = "GatewayToken";
    /**
     * Gateway请求头TOKEN值
     */
    public static final String GATEWAY_TOKEN_VALUE = "nebsGateway123456";

    /**
     * 允许下载的文件类型，根据需求自己添加（小写）
     */
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    /**
     * 验证码 key前缀
     */
    public static final String CODE_PREFIX = "nebs.captcha.";

    /**
     * 异步线程池名称
     */
    public static final String ASYNC_POOL = "nebsAsyncThreadPool";

    /**
     * OAUTH2 令牌类型 https://oauth.net/2/bearer-tokens/
     */
    public static final String OAUTH2_TOKEN_TYPE = "bearer";
    /**
     * Java默认临时目录
     */
    public static final String JAVA_TEMP_DIR = "java.io.tmpdir";
    /**
     * utf-8
     */
    public static final String UTF8 = "utf-8";
    /**
     * 注册用户角色ID
     */
    public static final Long REGISTER_ROLE_ID = 2L;

    /**
     * 令牌存储策略 InMemoryTokenStore,JdbcTokenStore,JwtTokenStore,RedisTokenStore
     */
    public static final String IN_MEMORY_TOKEN_STORE = "InMemoryTokenStore";
    public static final String JDBC_TOKEN_STORE = "JdbcTokenStore";
    public static final String JWT_TOKEN_STORE = "JwtTokenStore";
    public static final String REDIS_TOKEN_STORE = "RedisTokenStore";
}
