package com.april.house.biz.service;

import com.april.house.biz.mapper.UserMapper;
import com.april.house.common.enums.HouseUserTypeEnum;
import com.april.house.common.model.User;
import com.april.house.common.util.BeanHelper;
import com.april.house.common.util.HashUtil;
import com.april.house.common.util.SecureSaltUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private FileService fileService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserMapper userMapper;

    @Value(("${file.prefix:}"))
    private String imgPrefix;

    private final Cache<String, String> registerCache =
            CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES)
                    .removalListener(new RemovalListener<String, String>() {
                        @Override
                        public void onRemoval(RemovalNotification<String, String> notification) {
                            String email = notification.getValue();
                            User user = new User();
                            user.setEmail(email);
                            List<User> targetUser = userMapper.selectUsersByQuery(user);
                            if (!targetUser.isEmpty() && Objects.equal(targetUser.get(0).getEnable(), 0)) {
                                //先判断用户状态为未激活，然后再删除
                                userMapper.deleteByEmail(email);
                            }
                        }
                    }).build();

    private final Cache<String, String> resetCache =
            CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES).build();

    /**
     * 添加用户
     * 1. 插入数据库，非激活，密码加盐md5，保存头像文件到本地
     * 2. 生成key，绑定email
     * 3. 发送邮件给用户
     * @param account
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addAccount(User account) {
        account.setSalt(SecureSaltUtil.genSalt());
        account.setPasswd(HashUtil.encryPassword(account.getPasswd(), account.getSalt()));
        List<String> imgList = fileService.getImgPaths(Lists.newArrayList(account.getAvatarFile()));
        if (!imgList.isEmpty()) {
            account.setAvatar(imgList.get(0));
        } else{
            logger.error("upload user avatar failed, add user account fail");
            return false;
        }
        BeanHelper.setDefaultProp(account, User.class);
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        sendRegisterEmail(account.getEmail());
        return true;
    }

    public boolean enableUser(String key) {
        String email = registerCache.getIfPresent(key);
        if (StringUtils.isBlank(email)) {
            return false;
        }

        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        int result = userMapper.updateByEmail(updateUser);
        if (result == 1) {
            registerCache.invalidate(key);
            return true;
        }
        return false;
    }

    public String invalidRegisterCache(String key) {
        return invalidCache(registerCache, key);
    }

    public String getRegisterCaches() {
        return getCaches(registerCache);
    }

    public String invalidResetCache(String key) {
        return invalidCache(resetCache, key);
    }

    public String getResetCaches() {
        return getCaches(resetCache);
    }

    private String getCaches(Cache<String, String> cache) {
        Map<String, String> caches = cache.asMap();
        caches.forEach((k, v) -> {
            System.out.println(v + "=" + k);
        });
        return Joiner.on("\n").useForNull("").withKeyValueSeparator("=").join(caches);
    }

    private String invalidCache(Cache<String, String> cache, String key) {
        if (StringUtils.isBlank(key)) {
            registerCache.invalidateAll();
            return null;
        } else {
            String value = cache.getIfPresent(key);
            registerCache.invalidate(key);
            return value;
        }
    }


    private void sendRegisterEmail(String email) {
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        registerCache.put(randomKey, email);
        mailService.registerNotify(email, randomKey);
    }

    public List<User> getUserByQuery(User user) {
        List<User> list = userMapper.selectUsersByQuery(user);
        list.forEach(u -> {
            u.setAvatar(imgPrefix + u.getAvatar());
        });
        return list;
    }

    public User auth(String email, String password) {
        User query = new User();
        query.setEmail(email);
        List<User> users = getUserByQuery(query);
        if (!users.isEmpty()) {
            User user = users.get(0);
            if (checkPassword(password, user)) {
                return user;
            }
        }
        return null;
    }

    private boolean checkPassword(String iptPasswd, User user) {
        String salt = user.getSalt();
        String iptSaltPass = HashUtil.encryPassword(iptPasswd, salt);
        if (Objects.equal(user.getPasswd(), iptSaltPass)) {
            return true;
        }
        return false;
    }

    public void updateUser(User updateUser, String email) {
        updateUser.setEmail(email);
        BeanHelper.onUpdate(updateUser);
        userMapper.updateByEmail(updateUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(String newPassword, String email) {
        if (newPassword == null || email == null) {
            return false;
        }
        String salt = SecureSaltUtil.genSalt();
        String hashPasswd = HashUtil.encryPassword(newPassword, salt);
        User updateUser = new User();
        updateUser.setSalt(salt);
        updateUser.setPasswd(hashPasswd);
        updateUser.setEmail(email);
        BeanHelper.onUpdate(updateUser);
        int result = userMapper.updatePassword(updateUser);
        if (result == 1) {
            return true;
        }
        return false;
    }

    public void resetNotify(String email) {
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        resetCache.put(randomKey, email);
        mailService.resetNotify(email, randomKey);
    }

    @Transactional(rollbackFor = Exception.class)
    public User resetPassword(String key, String newPassword) {
        String email = getResetEmail(key);
        if (email == null) {
            return null;
        }
        boolean result = updatePassword(newPassword, email);
        invalidResetCache(key);
        if (result) {
            return getUserByEmail(email);
        } else {
            return null;
        }
    }


    public User getUserByEmail(String email) {
        User queryUser = new User();
        queryUser.setEmail(email);
        List<User> users = getUserByQuery(queryUser);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }
    public String getResetEmail(String key) {
        String email = resetCache.getIfPresent(key);
        return email;
    }


}
