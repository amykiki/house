package com.april.house.common.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;

/**
 * 密码+盐 sha256
 */
public class HashUtil {
    private static final Logger logger = LogManager.getLogger(HashUtil.class);
    private static final String publicSalt = "c6uT5Dm9z5WHX+vvWUVzYtz4MZGoOoCQ";
    private static final HashFunction FUNCTION = Hashing.sha256();

    public static String encryPassword(String password, String privateSalt) {
        if (null == password || null == privateSalt) {
            return null;
        }
        //盐 = 公盐 + 私盐
        String salt = publicSalt + privateSalt;
        HashCode hashCode = FUNCTION.hashString(password + salt, Charset.forName("UTF-8"));
        return hashCode.toString();
    }

}
