package com.number47.nebs.server.system.configure;

import org.apache.commons.lang3.StringUtils;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import util.DateUtil;

import java.time.LocalDateTime;

/**
 * @author number47
 * @date 2019/12/1 23:36
 * @description  自定义 p6spy sql输出格式
 */
public class P6spySqlFormatConfigure implements MessageFormattingStrategy {

	@Override
	public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
		return StringUtils.isNotBlank(sql) ? DateUtil.formatFullTime(LocalDateTime.now(), DateUtil.FULL_TIME_SPLIT_PATTERN)
				+ " | 耗时 " + elapsed + " ms | SQL 语句：" + StringUtils.LF + sql.replaceAll("[\\s]+", StringUtils.SPACE) + ";" : StringUtils.EMPTY;
	}
}