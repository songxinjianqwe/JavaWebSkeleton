package cn.sinjinsong.skeleton.controller.user.handler.impl;

import cn.sinjinsong.skeleton.controller.user.handler.QueryUserHandler;
import cn.sinjinsong.skeleton.domain.entity.user.UserDO;
import cn.sinjinsong.skeleton.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@Component("QueryUserHandler.username")
public class QueryUserByUsernameHandler implements QueryUserHandler {
    @Autowired
    private UserService service;

    @Override
    public UserDO handle(String key) {
        return service.findByUsername(key);
    }
}
