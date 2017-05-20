package cn.sinjinsong.common.exception.annotation;


import cn.sinjinsong.common.exception.base.BaseRESTException;

/**
 * Created by SinjinSong on 2017/3/27.
 */
public class RESTExceptionAnnotationUtil {
    public static void setAttr(BaseRESTException e) {
        RESTResponseStatus status = e.getClass().getAnnotation(RESTResponseStatus.class);
        if (status == null) {
            throw new RESTResponseStatusAnnotationNotFoundException();
        }
        e.setStatus(status.value());
        e.setCode(Integer.parseInt(status.value().value() + "" + String.format("%02d", status.code())));
        e.setMoreInfoURL(status.url());

    }

    public static String getMsgKey(BaseRESTException e) {
        RESTResponseStatus status = e.getClass().getAnnotation(RESTResponseStatus.class);
        if (status == null) {
            throw new RESTResponseStatusAnnotationNotFoundException();
        }
        if (status.msgKey().equals("")) {
            String simpleName = e.getClass().getSimpleName();
            return simpleName.substring(0, simpleName.lastIndexOf("Exception"));
        } else {
            return status.msgKey();
        }
    }

    public static String getFieldName(BaseRESTException e) {
        RESTField field = e.getClass().getAnnotation(RESTField.class);
        if (field == null) {
            throw new RESTFieldAnnotationNotFoundException();
        }
        return field.value();
    }
}
