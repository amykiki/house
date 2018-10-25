package com.april.house.common.util;

import com.april.house.common.model.User;

/**
 * @Description :
 * @Creation Date:  2018-10-24 13:19
 * --------  ---------  --------------------------
 */
public class UserContext {
    private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();

    public static void setUser(User user) {
        USER_HOLDER.set(user);
    }

    public static User getUser() {
        return USER_HOLDER.get();
    }

    public static void removeUser() {
        USER_HOLDER.remove();
    }

}
