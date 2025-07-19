//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.caoximu.bookmanger.exception;

import java.util.Arrays;

public abstract class BaseBizException extends RuntimeException {
    private int code;
    private String msgCode;
    private Object[] msgParams;

    public BaseBizException(Integer code, String msgCode, Object[] args) {
        this.code = code;
        this.msgCode = msgCode;
        this.msgParams = args;
    }

    public BaseBizException(Integer code, String msgCode, Object[] args, Exception ex) {
        super(ex);
        this.code = code;
        this.msgCode = msgCode;
        this.msgParams = args;
    }

    @Override
    public String getMessage() {
        return this.getErrorMessage();
    }

    protected abstract String getErrorMessage();

    public int getCode() {
        return this.code;
    }

    public String getMsgCode() {
        return this.msgCode;
    }

    public Object[] getMsgParams() {
        return this.msgParams;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public void setMsgParams(Object[] msgParams) {
        this.msgParams = msgParams;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseBizException)) {
            return false;
        } else {
            BaseBizException other = (BaseBizException)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getCode() != other.getCode()) {
                return false;
            } else {
                Object this$msgCode = this.getMsgCode();
                Object other$msgCode = other.getMsgCode();
                if (this$msgCode == null) {
                    if (other$msgCode != null) {
                        return false;
                    }
                } else if (!this$msgCode.equals(other$msgCode)) {
                    return false;
                }

                if (!Arrays.deepEquals(this.getMsgParams(), other.getMsgParams())) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseBizException;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCode();
        Object $msgCode = this.getMsgCode();
        result = result * 59 + ($msgCode == null ? 43 : $msgCode.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getMsgParams());
        return result;
    }

    @Override
    public String toString() {
        return "BaseBizException(code=" + this.getCode() + ", msgCode=" + this.getMsgCode() + ", msgParams=" + Arrays.deepToString(this.getMsgParams()) + ")";
    }
}
