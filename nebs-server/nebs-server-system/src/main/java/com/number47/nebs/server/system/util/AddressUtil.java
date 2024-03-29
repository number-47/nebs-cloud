package com.number47.nebs.server.system.util;


import entity.constant.NebsConstant;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author number47
 * @date 2019/12/13 01:47
 * @description 根据 IP获取地址
 */
public class AddressUtil {

	private static Logger log = LoggerFactory.getLogger(AddressUtil.class);

	public static String getCityInfo(String ip) {
		DbSearcher searcher = null;
		try {
			String dbPath = AddressUtil.class.getResource("/ip2region/ip2region.db").getPath();
			File file = new File(dbPath);
			if (!file.exists()) {
				String tmpDir = System.getProperties().getProperty(NebsConstant.JAVA_TEMP_DIR);
				dbPath = tmpDir + "ip.db";
				file = new File(dbPath);
				InputStream resourceAsStream = AddressUtil.class.getClassLoader().getResourceAsStream("classpath:ip2region/ip2region.db");
				if (resourceAsStream != null) {
					FileUtils.copyInputStreamToFile(resourceAsStream, file);
				}
			}
			DbConfig config = new DbConfig();
			searcher = new DbSearcher(config, file.getPath());
			Method method = searcher.getClass().getMethod("btreeSearch", String.class);
			if (!Util.isIpAddress(ip)) {
				log.error("Error: Invalid ip address");
			}
			DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);
			return dataBlock.getRegion();
		} catch (Exception e) {
			log.error("获取地址信息异常，{}", e.getMessage());
			return StringUtils.EMPTY;
		} finally {
			if (searcher != null) {
				try {
					searcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
