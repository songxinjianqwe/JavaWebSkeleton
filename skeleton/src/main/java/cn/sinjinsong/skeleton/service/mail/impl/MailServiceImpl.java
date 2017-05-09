package cn.sinjinsong.skeleton.service.mail.impl;

import cn.sinjinsong.skeleton.dao.mail.MailDOMapper;
import cn.sinjinsong.skeleton.dao.mail.MailTextDOMapper;
import cn.sinjinsong.skeleton.dao.user.UserDOMapper;
import cn.sinjinsong.skeleton.domain.entity.mail.MailDO;
import cn.sinjinsong.skeleton.domain.entity.mail.MailTextDO;
import cn.sinjinsong.skeleton.domain.entity.user.UserDO;
import cn.sinjinsong.skeleton.enumeration.mail.MailStatus;
import cn.sinjinsong.skeleton.exception.mail.MailReceiverNotFoundException;
import cn.sinjinsong.skeleton.service.mail.MailService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by SinjinSong on 2017/5/4.
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private MailDOMapper mailDOMapper;
    @Autowired
    private MailTextDOMapper mailTextDOMapper;
    @Autowired
    private UserDOMapper userDOMapper;
    
    
    @Transactional
    @Override
    public PageInfo<MailDO> findByReceiver(Long receiver, int pageNum, int pageSize, MailStatus mailStatus) {
        PageInfo<MailDO> page = mailDOMapper.findByReceiver(receiver, pageNum, pageSize, mailStatus).toPageInfo();
        if(page.getList().size() == 0){
            return page;
        }
        //设置mail状态为已读
        List<Long> mails = page.getList().stream().map(MailDO::getId).collect(Collectors.toList());
        mailDOMapper.updateStatus(mails, MailStatus.VIEWED);
        return page;
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<MailDO> findBySender(Long sender, int pageNum, int pageSize) {
        return mailDOMapper.findBySender(sender, pageNum, pageSize).toPageInfo();
    }

    @Transactional
    @Override
    public void send(Long sender, List<Long> receivers, String text) {
        if(receivers == null || receivers.size() == 0){
            throw new MailReceiverNotFoundException(sender);
        }
        MailTextDO mailTextDO = new MailTextDO(null, LocalDateTime.now(), text);
        mailTextDOMapper.insert(mailTextDO);
        List<MailDO> mailDOS = new ArrayList<>(receivers.size());
        for(Long receiver:receivers){
            mailDOS.add(new MailDO(null, new UserDO(sender), new UserDO(receiver), MailStatus.NOT_VIEWED, mailTextDO));
        }
        mailDOMapper.insertBatch(mailDOS);
    }
    
    @Transactional
    @Override
    public void broadcast(Long sender, String text){
        send(sender,userDOMapper.findAllUserIds(),text);
    }

    @Transactional
    @Override
    public void deleteMail(Long id) {
        mailDOMapper.deleteByPrimaryKey(id);
    }
}
