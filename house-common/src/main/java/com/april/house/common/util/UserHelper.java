package com.april.house.common.util;

import com.april.house.common.model.User;
import com.april.house.common.result.ResultMsg;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class UserHelper {
    public static ResultMsg validate(User account) {
        if(StringUtils.isBlank(account.getName())) {
            return ResultMsg.errorMsg("用户名为空");
        }

        if (StringUtils.isBlank(account.getEmail())) {
            return ResultMsg.errorMsg("邮箱为空");
        }

        if (StringUtils.isBlank(account.getPasswd()) || StringUtils.isBlank(account.getConfirmPasswd())) {
            return ResultMsg.errorMsg("密码为空");
        }

        if (!account.getPasswd().equals(account.getConfirmPasswd())) {
            return ResultMsg.errorMsg("两次输入密码不一致");
        }

        if (account.getPasswd().length() < 6) {
            return ResultMsg.errorMsg("密码应该大于等于6位");
        }
        return ResultMsg.successMsg("");
    }

    public static ResultMsg validateResetPassword(String key, String passwd, String confirmPassword) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(passwd) || StringUtils.isBlank(confirmPassword)) {
            return ResultMsg.errorMsg("参数为空");
        }

        if (!Objects.equals(passwd, confirmPassword)) {
            return ResultMsg.errorMsg("密码必须与确认密码一致");
        }
        return ResultMsg.successMsg("");
    }
}
