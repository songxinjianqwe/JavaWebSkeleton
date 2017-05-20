package cn.sinjinsong.skeleton.security.verification.impl;

import cn.sinjinsong.common.cache.CacheManager;
import cn.sinjinsong.skeleton.security.verification.VerificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by SinjinSong on 2017/5/6.
 */
@Component
class VerificationManagerImpl implements VerificationManager {
    @Autowired
    private CacheManager cacheManager;
    
    @Override
    public void createVerificationCode(String key, String value,long expireTime) {
        cacheManager.putWithExpireTime(key,value,expireTime);
    }

    @Override
    public boolean checkVerificationCode(String key, String value) {
        String realValue = cacheManager.get(key, String.class);
        if(realValue == null){
            return false;
        }
        if(!value.equals(realValue)){
            return false;
        }
        return true;
    }

    @Override
    public void deleteVerificationCode(String key) {
        cacheManager.delete(key);
    }
}
