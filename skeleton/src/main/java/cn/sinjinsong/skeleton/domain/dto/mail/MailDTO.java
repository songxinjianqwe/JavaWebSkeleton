package cn.sinjinsong.skeleton.domain.dto.mail;


import cn.sinjinsong.skeleton.enumeration.mail.SendMode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by SinjinSong on 2017/5/5.
 */
public class MailDTO {
    @NotNull
    private Long sender;
    private List<Long> receivers;
    @NotNull
    private String text;
    @NotNull
    private SendMode sendMode;
        
    public MailDTO(){}

    public SendMode getSendMode() {
        return sendMode;
    }

    public void setSendMode(SendMode sendMode) {
        this.sendMode = sendMode;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public List<Long> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Long> receivers) {
        this.receivers = receivers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MailDTO{" +
                "sender=" + sender +
                ", receivers=" + receivers +
                ", text='" + text + '\'' +
                ", sendMode=" + sendMode +
                '}';
    }
}
