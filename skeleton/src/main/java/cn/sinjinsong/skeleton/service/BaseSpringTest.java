package cn.sinjinsong.skeleton.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by SinjinSong on 2017/5/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { 
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml",
        "classpath:spring/spring-security.xml",
        "classpath:spring/spring-redis.xml",
        "classpath:spring/spring-util.xml",
        "classpath:spring/spring-email.xml",
        "classpath:spring/spring-client.xml"
})
@Transactional
public class BaseSpringTest {
    
}
