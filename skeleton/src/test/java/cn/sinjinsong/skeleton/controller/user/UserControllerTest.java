package cn.sinjinsong.skeleton.controller.user;

import cn.sinjinsong.common.cache.CacheManager;
import cn.sinjinsong.skeleton.BaseSpringTest;
import cn.sinjinsong.skeleton.domain.dto.mail.MailDTO;
import cn.sinjinsong.skeleton.domain.entity.user.UserDO;
import cn.sinjinsong.skeleton.enumeration.mail.SendMode;
import cn.sinjinsong.skeleton.enumeration.user.UserMode;
import cn.sinjinsong.skeleton.service.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by SinjinSong on 2017/5/9.
 */
public class UserControllerTest extends BaseSpringTest{
    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;
    @Autowired
    private CacheManager cacheManager;
    @Test
    public void findByKey() throws Exception {
        UserDO user = userService.findById(1L);
        System.out.println(user);
    }

    @Test
    public void createUser() throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("user1");
        System.out.println(password);
        
    }

    @Test
    public void getUserAvatar() throws Exception {
        System.out.println(UserMode.valueOf("USERNAME"));
    }
    
    @Test
    public void spEL(){
//        cacheManager.put("39E82013BAF14F67A34A835EFFB663F4","MJXV"); 
        EvaluationContext context = new StandardEvaluationContext();
        MailDTO mailDTO  = new MailDTO();
        mailDTO.setSendMode(SendMode.BROADCAST);
        context.setVariable("mailDTO",mailDTO);
        ExpressionParser parser = new SpelExpressionParser();
        Object value = parser.parseExpression("#mailDTO.sendMode.toString()  != BROADCAST").getValue();
        System.out.println(value);
    }
    
    @Test
    public void activate() throws Exception {
        
    }

    @Test
    public void updateUser() throws Exception {
    }

    @Test
    public void forgetPassword() throws Exception {
    }

    @Test
    public void resetPassword() throws Exception {
    }

    @Test
    public void isUsernameDuplicated() throws Exception {
    }

    @Test
    public void findAllUsers() throws Exception {
    }


}
