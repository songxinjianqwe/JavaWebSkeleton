package cn.sinjinsong.skeleton.service.mail;


import cn.sinjinsong.skeleton.domain.entity.mail.MailDO;
import cn.sinjinsong.skeleton.enumeration.mail.MailStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by SinjinSong on 2017/5/4.
 */
public interface MailService {
    PageInfo<MailDO> findByReceiver(Long receiver, int pageNum, int pageSize, MailStatus mailStatus);
    
    PageInfo<MailDO> findBySender(Long sender, int pageNum, int pageSize);


    void send(Long sender, List<Long> receivers, String text);

    void broadcast(Long sender, String text);

    void deleteMail(Long id);
}
