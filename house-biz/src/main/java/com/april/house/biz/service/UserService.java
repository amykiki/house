package com.april.house.biz.service;

import com.april.house.biz.mapper.UserMapper;
import com.april.house.common.model.User;
import com.april.house.common.util.BeanHelper;
import com.april.house.common.util.HashUtil;
import com.april.house.common.util.SecureSaltUtil;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private FileService fileService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserMapper userMapper;

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
        }
        BeanHelper.setDefaultProp(account, User.class);
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        sendRegisterEmail(account.getEmail());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean enableUser(String key) {
        String email = registerCache.getIfPresent(key);
        if (StringUtils.isBlank(email)) {
            return false;
        }

        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        userMapper.updateByEmail(updateUser);
        registerCache.invalidate(key);
        return true;
    }


    private void sendRegisterEmail(String email) {
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        registerCache.put(randomKey, email);
        mailService.registerNotify(email, randomKey);
    }


}
