package cn.sinjinsong.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * 用于国际化
 * 注意这是一个静态导入的工具类
 * @author Sinjin Song
 *
 */
@Component
public final  class InternationalizeUtil {
	@Autowired
	private ResourceBundleMessageSource ms;
	private static InternationalizeUtil util;
	private InternationalizeUtil(){}
	@PostConstruct
	public void init() {
		util = this;
		util.ms = this.ms;
	}
	
	public static String getMessage(String message,Locale locale){
		return util.ms.getMessage(message, null, locale);
	}
}
