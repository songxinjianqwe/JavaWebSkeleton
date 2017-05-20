package cn.sinjinsong.skeleton.security.domain;

import cn.sinjinsong.common.exception.base.BaseRESTException;

/**
 * Created by SinjinSong on 2017/5/9.
 */
public class TokenCheckResult {
    private boolean isValid;
    private String username;
    private BaseRESTException exception;

    public TokenCheckResult(boolean isValid, String username, BaseRESTException exception) {
        this.isValid = isValid;
        this.username = username;
        this.exception = exception;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BaseRESTException getException() {
        return exception;
    }

    public void setException(BaseRESTException exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "TokenCheckResult{" +
                "isValid=" + isValid +
                ", username='" + username + '\'' +
                ", exception=" + exception +
                '}';
    }

    public static class TokenCheckResultBuilder {
        private boolean isValid;
        private String username;
        private BaseRESTException exception;

        public TokenCheckResultBuilder() {
        }

        public TokenCheckResultBuilder valid() {
            this.isValid = true;
            return this;
        }

        public TokenCheckResultBuilder inValid() {
            this.isValid = false;
            return this;
        }

        public TokenCheckResultBuilder username(String username) {
            this.username = username;
            return this;
        }

        public TokenCheckResultBuilder exception(BaseRESTException exception) {
            this.exception = exception;
            return this;
        }

        public TokenCheckResult build() {
            return new TokenCheckResult(isValid, username, exception);
        }

    }
}
