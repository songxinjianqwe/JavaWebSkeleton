package cn.sinjinsong.skeleton.controller.user;

import cn.sinjinsong.common.domain.Captcha;
import cn.sinjinsong.common.util.CaptchaUtil;
import cn.sinjinsong.common.util.UUIDUtil;
import cn.sinjinsong.skeleton.properties.AuthenticationProperties;
import cn.sinjinsong.skeleton.security.verification.VerificationManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@CrossOrigin
@RequestMapping("/captchas")
@RestController
@Api(value = "captchas", description = "图片验证码")
@Slf4j
public class CaptchaController {
    @Autowired
    private VerificationManager verificationManager;
    @Autowired
    private AuthenticationProperties authenticationProperties;
    
    /**
     * 如果想在@ResponseBody方法中返回Cookie或者设置响应头，必须先设置Cookie或设置响应头，再设置response的内容（返回值）
     * 另外，返回字节流时需要获取response中的输出流，将字节写入输出流，而非返回字节数组
     *
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "获取验证码，在登录之前请求", response = Byte.class)
    public void getCaptcha(HttpServletResponse response) {
        String uuid = UUIDUtil.uuid();
        Captcha captcha = CaptchaUtil.createRandomCode();

//        返回Cookie
//        Cookie cookie = new Cookie("captchaCode", uuid);
//        cookie.setMaxAge(audience.getCaptchaExpireTime());
//        response.addCookie(cookie);

        //返回响应头
        response.setHeader("captchaCode", uuid);
        //保存
        verificationManager.createVerificationCode(uuid, captcha.getValue(),authenticationProperties.getCaptchaExpireTime());
        log.info("uuid:{}",uuid);
        log.info("value:{}",captcha.getValue());
        //返回图片
        OutputStream os;
        try {
            os = response.getOutputStream();
            os.write(captcha.getImage());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
