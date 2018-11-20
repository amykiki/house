package com.april.house;

import com.april.house.common.util.SecureSaltUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.april.house.biz.mapper")
//开启定时任务
@EnableScheduling
public class HouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(HouseApplication.class, args);
        SecureSaltUtil.genSalt();
    }

}
