package com.april.house;

import com.april.house.common.util.SecureSaltUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HouseApplication {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(HouseApplication.class);
        SpringApplication.run(HouseApplication.class, args);
        SecureSaltUtil.genSalt();
    }
}
