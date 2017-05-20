package cn.sinjinsong.common.util;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public final  class ConvertUtil {
    private ConvertUtil(){}
	public static <T> T toBean(Map<String, Object> properties, Class<T> cls) {
		T bean = null;
		try {
			bean = cls.newInstance();
			BeanUtils.populate(bean, properties);
		} catch (Exception e) {
			throw new RuntimeException(e);// 抛出一个运行时异常
		}
		return bean;
	}
}
