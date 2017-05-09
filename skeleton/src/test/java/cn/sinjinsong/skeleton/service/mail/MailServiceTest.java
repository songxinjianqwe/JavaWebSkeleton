package cn.sinjinsong.skeleton.service.mail;

import cn.sinjinsong.skeleton.BaseSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by SinjinSong on 2017/5/9.
 */

public class MailServiceTest extends BaseSpringTest{
    @Autowired
    private MailService mailService;
    
    @Test
    public void findByReceiver() throws Exception {
    }

    @Test
    public void findBySender() throws Exception {
    }

    @Test
    public void send() throws Exception {
    }

    @Test
    public void broadcast() throws Exception {
        mailService.broadcast(1L,"hh");
    }

    @Test
    public void deleteMail() throws Exception {
    }

   

}
