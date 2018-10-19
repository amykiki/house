package com.april.house.common.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class BeanHelper {
    private static final Logger logger = LogManager.getLogger(BeanHelper.class);
    private static final String updateTimeKey = "updateTime";
    private static final String createTimeKey = "createTime";

    public static <T> void onUpdate(T target) {
        try {
            PropertyUtils.setProperty(target, updateTimeKey, System.currentTimeMillis());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("setProperty " + updateTimeKey + "failed for " + target.getClass());
            return;
        }

    }
}
