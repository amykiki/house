package com.april.house.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Description : 产生安全的随机盐，使用SecureRandom
 * @Creation Date:  2018-10-17 17:09
 * --------  ---------  --------------------------
 */
public class SecureSaltUtil {
    private static final Logger LOGGER = LogManager.getLogger(SecureSaltUtil.class);
    private static SecureRandom rnd;
    static {
        long t1 = System.currentTimeMillis();
        try {
            rnd = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            rnd = new SecureRandom();
        }
        //第一次调用nextBytes[]函数时，如果此前没有初始化rnd的种子，rnd会强制给自己设置种子
        //因此可以先设置种子
        byte[] seed = rnd.generateSeed(64);
        rnd.setSeed(seed);
        long t2 = System.currentTimeMillis();
        LOGGER.info("SecureSaltUtil 工具类初始化消耗时间=" + (t2 - t1) + "毫秒");
    }

    public static String genSalt() {

        byte[] bytes = new byte[32];
        rnd.nextBytes(bytes);

        return null;
    }

}
