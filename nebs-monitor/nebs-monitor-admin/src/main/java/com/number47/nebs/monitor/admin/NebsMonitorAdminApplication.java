package com.number47.nebs.monitor.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class NebsMonitorAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(NebsMonitorAdminApplication.class, args);
	}

}
