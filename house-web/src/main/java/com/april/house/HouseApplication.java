package com.april.house;

import com.april.house.common.util.SecureSaltUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(HouseApplication.class, args);
        SecureSaltUtil saltUtil = new SecureSaltUtil();
    }
}
