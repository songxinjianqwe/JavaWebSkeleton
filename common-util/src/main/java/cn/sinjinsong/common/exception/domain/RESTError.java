package cn.sinjinsong.common.exception.domain;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * 所有异常都将返回该错误对象
 */
public class RESTError {
	private HttpStatus status;
	private int code;
	private List<RESTFieldError> fieldErrors;
	private String moreInfoURL;
	
	public RESTError() {
	}

	public RESTError(HttpStatus status, int code, List<RESTFieldError> fieldErrors, String moreInfoURL
			) {
		this.status = status;
		this.code = code;
		this.fieldErrors = fieldErrors;
		this.moreInfoURL = moreInfoURL;
	}

	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<RESTFieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<RESTFieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public String getMoreInfoURL() {
		return moreInfoURL;
	}

	public void setMoreInfoURL(String moreInfoURL) {
		this.moreInfoURL = moreInfoURL;
	}

	@Override
	public String toString() {
		return "RESTError [status=" + status + ", code=" + code + ", fieldErrors=" + fieldErrors + ", moreInfoURL="
				+ moreInfoURL + "]";
	}
	
	
	
}
