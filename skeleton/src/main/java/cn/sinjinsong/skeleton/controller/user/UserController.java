package cn.sinjinsong.skeleton.controller.user;

import cn.sinjinsong.common.exception.ValidationException;
import cn.sinjinsong.common.util.FileUtil;
import cn.sinjinsong.common.util.SpringContextUtil;
import cn.sinjinsong.common.util.UUIDUtil;
import cn.sinjinsong.skeleton.controller.user.handler.QueryUserHandler;
import cn.sinjinsong.skeleton.domain.entity.user.UserDO;
import cn.sinjinsong.skeleton.enumeration.user.UserStatus;
import cn.sinjinsong.skeleton.exception.token.ActivationCodeValidationException;
import cn.sinjinsong.skeleton.exception.user.QueryUserModeNotFoundException;
import cn.sinjinsong.skeleton.exception.user.UserNotFoundException;
import cn.sinjinsong.skeleton.exception.user.UsernameExistedException;
import cn.sinjinsong.skeleton.properties.AuthenticationProperties;
import cn.sinjinsong.skeleton.security.domain.JWTUser;
import cn.sinjinsong.skeleton.security.verification.VerificationManager;
import cn.sinjinsong.skeleton.service.email.EmailService;
import cn.sinjinsong.skeleton.service.user.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@CrossOrigin
@RequestMapping("/users")
@RestController
@Api(value = "users", description = "用户API")
@Slf4j
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private VerificationManager verificationManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthenticationProperties authenticationProperties;
    
    /**
     * mode 支持id、username、email、手机号
     * 只有管理员或自己才可以查询某用户的完整信息
     *
     * @param key
     * @param mode id、username、email、手机号
     * @return
     */
    @RequestMapping(value = "/query/{key}", method = RequestMethod.GET)
    @PostAuthorize("hasRole('ADMIN') or (returnObject.username ==  principal.username)")
    @ApiOperation(value = "按某属性查询用户", notes = "属性可以是id或username或email或手机号", response = UserDO.class, authorizations = {@Authorization("登录权限")})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "未登录"),
            @ApiResponse(code = 404, message = "查询模式未找到"),
            @ApiResponse(code = 403, message = "只有管理员或用户自己能查询自己的用户信息"),
    })
    public UserDO findByKey(@PathVariable("key") @ApiParam(value = "查询关键字", required = true) String key, @RequestParam("mode") @ApiParam(value = "查询模式，可以是id或username或phone或email", required = true) String mode) {
        
        QueryUserHandler handler = SpringContextUtil.getBean("QueryUserHandler", StringUtils.lowerCase(mode));
        if (handler == null) {
            throw new QueryUserModeNotFoundException(mode);
        }
        UserDO userDO = handler.handle(key);
        if (userDO == null) {
            throw new UserNotFoundException(key);
        }
        return userDO;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "创建用户，为用户发送验证邮件，等待用户激活，若24小时内未激活需要重新注册", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "用户名已存在"),
            @ApiResponse(code = 400, message = "用户属性校验失败")
    })
    public void createUser(@RequestBody @Valid @ApiParam(value = "用户信息，用户的用户名、密码、昵称、邮箱不可为空", required = true) UserDO user, BindingResult result) {
        log.info("{}",user);
        if (isUsernameDuplicated(user.getUsername())) {
            throw new UsernameExistedException(user.getUsername());
        } else if (result.hasErrors()) {
            throw new ValidationException(result.getFieldErrors());
        }
        
        //生成邮箱的激活码
        String activationCode = UUIDUtil.uuid();
        //保存用户
        service.save(user);
        
        verificationManager.createVerificationCode(activationCode, String.valueOf(user.getId()), authenticationProperties.getActivationCodeExpireTime());
        log.info("{}     {}",user.getEmail(),user.getId());
        //发送邮件
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("activationCode", activationCode);
        emailService.sendHTML(user.getEmail(), "activation", params, null);
    }


    @RequestMapping(value = "/{id}/avatar", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户的头像图片", response = Byte.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "文件不存在"),
            @ApiResponse(code = 400, message = "文件传输失败")
    })
    public void getUserAvatar(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        String relativePath = service.findAvatarById(id);
        FileUtil.download(relativePath, request.getServletContext(), response);
    }

    @RequestMapping(value = "/{id}/activation", method = RequestMethod.GET)
    @ApiOperation(value = "用户激活，前置条件是用户已注册且在24小时内", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "未注册或超时或激活码错误")
    })
    public void activate(@PathVariable("id") @ApiParam(value = "用户Id", required = true) Long id, @RequestParam("activationCode") @ApiParam(value = "激活码", required = true) String activationCode) {
        UserDO user = service.findById(id);
        //获取Redis中的验证码
        if (!verificationManager.checkVerificationCode(activationCode, String.valueOf(id))) {
            verificationManager.deleteVerificationCode(activationCode);
            throw new ActivationCodeValidationException(activationCode);
        }
        user.setUserStatus(UserStatus.ACTIVATED);
        verificationManager.deleteVerificationCode(activationCode);
        service.update(user);
    }
    
    // 更新
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("#user.username == principal.username or hasRole('ADMIN')")
    @ApiOperation(value = "更新用户信息", response = Void.class, authorizations = {@Authorization("登录权限")})
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "未登录"),
            @ApiResponse(code = 404, message = "用户属性校验失败"),
            @ApiResponse(code = 403, message = "只有管理员或用户自己能更新用户信息"),

    })
    public void updateUser(@RequestBody @Valid @ApiParam(value = "用户信息，用户的用户名、密码、昵称、邮箱不可为空", required = true) UserDO user, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result.getFieldErrors());
        }
        service.update(user);
    }

    @RequestMapping(value = "/{key}/password/reset_validation", method = RequestMethod.GET)
    @ApiOperation(value = "发送忘记密码的邮箱验证", notes = "属性可以是id,sername或email或手机号", response = UserDO.class)
    public void forgetPassword(@PathVariable("key") @ApiParam(value = "关键字", required = true) String key, @RequestParam("mode") @ApiParam(value = "验证模式，可以是username或phone或email", required = true) String mode) {
        UserDO user = findByKey(key, mode);
        //user 一定不为空
        String forgetPasswordCode = UUIDUtil.uuid();
        verificationManager.createVerificationCode(forgetPasswordCode, String.valueOf(user.getId()), authenticationProperties.getForgetNameExpireTime());
        log.info("{}   {}",user.getEmail(),user.getId());
        //发送邮件
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("forgetPasswordCode", forgetPasswordCode);
        emailService.sendHTML(user.getEmail(), "forgetPassword", params, null);
    }

    /**
     * 注解@AuthenticationPrincipal在spring配置文件中加入了AuthenticationPrincipalArgumentResolver之后，
     * 可以将实现了UserDetails接口的实体类作为controller的参数传入，而且可以直接写实际的子类类型
     * @param jwtUser
     * @return
     */
    @RequestMapping(value="/principles",method = RequestMethod.GET)
    public String getPrinciples(@AuthenticationPrincipal JWTUser jwtUser){
        return jwtUser.getAuthorities().toString();
    }
    
    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    @ApiOperation(value = "忘记密码后可以修改密码")
    public void resetPassword(@PathVariable("id") Long id, @RequestParam("forgetPasswordCode") @ApiParam(value = "验证码", required = true) String forgetPasswordCode, @RequestParam("password") @ApiParam(value = "新密码", required = true) String password) {
        //获取Redis中的验证码
        if (!verificationManager.checkVerificationCode(forgetPasswordCode, String.valueOf(id))) {
            verificationManager.deleteVerificationCode(forgetPasswordCode);
            throw new ActivationCodeValidationException(forgetPasswordCode);
        }
        verificationManager.deleteVerificationCode(forgetPasswordCode);
        service.resetPassword(id,password);
    }
    
    @RequestMapping(value = "/{username}/duplication", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户名是否重复", response = Boolean.class)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "未登录")})
    public boolean isUsernameDuplicated(@PathVariable("username") String username) {
        if (service.findByUsername(username) == null) {
            return false;
        }
        return true;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "分页查询用户信息", response = PageInfo.class, authorizations = {@Authorization("登录权限")})
    @ApiResponses(value = {@ApiResponse(code = 401, message = "未登录")})
    public PageInfo<UserDO> findAllUsers(@RequestParam("pageNum") @ApiParam(value = "页码，从1开始", defaultValue = "1") Integer pageNum, @RequestParam("pageSize") @ApiParam(value = "每页记录数", defaultValue = "5") Integer pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication:{}",authentication.getAuthorities());
        return service.findAll(pageNum, pageSize);
    }
}
