package cn.sinjinsong.skeleton.domain.dto.user;


import cn.sinjinsong.skeleton.enumeration.user.UserMode;

import javax.validation.constraints.NotNull;

/**
 * Created by SinjinSong on 2017/4/27.
 */
public class LoginDTO {
    
    private String username;
    private String phone;
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String captchaCode;
    @NotNull
    private String captchaValue;
    @NotNull
    private UserMode userMode;
    
    public LoginDTO(){}

    public LoginDTO(String username, String phone, String email, String password, UserMode userMode) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public LoginDTO(String username, String phone, String password, String captchaCode, String captchaValue, UserMode userMode) {
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.captchaCode = captchaCode;
        this.captchaValue = captchaValue;
        this.userMode = userMode;
    }
    
    public String getUsername() {
        return username;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public String getCaptchaValue() {
        return captchaValue;
    }

    public void setCaptchaValue(String captchaValue) {
        this.captchaValue = captchaValue;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", captchaCode='" + captchaCode + '\'' +
                ", captchaValue='" + captchaValue + '\'' +
                ", userMode=" + userMode +
                '}';
    }

}
