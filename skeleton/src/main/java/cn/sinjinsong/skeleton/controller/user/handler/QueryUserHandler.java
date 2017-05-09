package cn.sinjinsong.skeleton.controller.user.handler;


import cn.sinjinsong.skeleton.domain.entity.user.UserDO;

/**
 * Created by SinjinSong on 2017/4/27.
 */
public interface QueryUserHandler {
    UserDO handle(String key);
}
