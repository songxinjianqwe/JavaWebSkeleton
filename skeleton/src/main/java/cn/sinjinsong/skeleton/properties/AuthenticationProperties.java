package cn.sinjinsong.skeleton.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@Component
public class AuthenticationProperties {
    @Value("${token.secretKey}")
    private String secretKey;
    @Value("${token.expireTime}")
    private Integer expireTime;
    @Value("${captcha.expireTime}")
    private Integer captchaExpireTime;
    @Value("${activationCode.expireTime}")
    private Integer activationCodeExpireTime;
    @Value("${forgetNameCode.expireTime}")
    private Integer forgetNameExpireTime;

    public static final String AUTH_HEADER = "Authentication";
    public static final String USER_ID = "id";
    public static final String LOGIN_URL = "/tokens";
    public static final String EXCEPTION_ATTR_NAME = "BaseRESTException";
    public Integer getCaptchaExpireTime() {
        return captchaExpireTime;
    }

    public void setCaptchaExpireTime(Integer captchaExpireTime) {
        this.captchaExpireTime = captchaExpireTime;
    }

    public Integer getActivationCodeExpireTime() {
        return activationCodeExpireTime;
    }

    public void setActivationCodeExpireTime(Integer activationCodeExpireTime) {
        this.activationCodeExpireTime = activationCodeExpireTime;
    }


    public Integer getForgetNameExpireTime() {
        return forgetNameExpireTime;
    }

    public void setForgetNameExpireTime(Integer forgetNameExpireTime) {
        this.forgetNameExpireTime = forgetNameExpireTime;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
