package cn.sinjinsong.common.domain;

/**
 * Created by SinjinSong on 2017/4/27.
 */
public class Captcha {
    private byte[] image;
    private String value;

    public Captcha(byte[] image, String value) {
        this.image = image;
        this.value = value;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
