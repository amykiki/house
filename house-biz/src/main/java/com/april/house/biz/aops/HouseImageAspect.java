package com.april.house.biz.aops;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class HouseImageAspect {
    private static final Logger logger = LogManager.getLogger(HouseImageAspect.class);

    @Pointcut("execution(* com.april.house.common.model.House.setImages(String))")
    public void houseImages() {

    }

    @After("houseImages() && args(images)")
    public void setImagesFileds(JoinPoint joinPoint, String images) {
        System.out.println("INTO AOP!");
        if (!Strings.isNullOrEmpty(images)) {
            List<String> list = Splitter.on(",").splitToList(images);
            if (list.size() > 0) {
                Object target = joinPoint.getTarget();
                Object tObject = joinPoint.getThis();
            }
        }

    }
}
