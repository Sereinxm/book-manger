package com.caoximu.bookmanger.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Arrays;

@Schema(
    name = "公共返回体"
)
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 200;
    public static final int FAIL = 500;
    @Schema(
        description = "错误码"
    )
    private int code;
    @Schema(
        description = "错误信息"
    )
    private String msg;
    @Schema(
        description = "错误信息翻译码"
    )
    private String msgCode;
    @Schema(
        description = "错误信息占位参数"
    )
    private Object[] msgParams;
    @Schema(
        description = "数据体"
    )
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, 200, "操作成功", (String)null, (Object[])null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, 200, "操作成功", (String)null, (Object[])null);
    }

    public static <T> R<T> fail() {
        return restResult(null, 500, "操作失败", (String)null, (Object[])null);
    }

    public static <T> R<T> fail(String msgCode) {
        return restResult(null, 500, (String)null, msgCode, (Object[])null);
    }

    public static <T> R<T> fail(String msgCode, Object[] msgParams) {
        return restResult(null, 500, (String)null, msgCode, msgParams);
    }

    public static <T> R<T> fail(int code) {
        return restResult(null, code, (String)null, (String)null, (Object[])null);
    }

    public static <T> R<T> fail(int code, String msgCode) {
        return restResult(null, code, (String)null, msgCode, (Object[])null);
    }

    public static <T> R<T> fail(int code, T data) {
        return restResult(data, code, (String)null, (String)null, (Object[])null);
    }

    public static <T> R<T> fail(int code, String msgCode, Object[] msgParams) {
        return restResult(null, code, (String)null, msgCode, msgParams);
    }

    public static <T> R<T> fail(int code, String msg, String msgCode, Object[] msgParams) {
        return restResult(null, code, msg, msgCode, msgParams);
    }

    public static <T> R<T> warn(String msg) {
        return restResult(null, 500, msg, (String)null, (Object[])null);
    }

    private static <T> R<T> restResult(T data, int code, String msg, String msgCode, Object[] msgParams) {
        R<T> r = new R<T>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        r.setMsgCode(msgCode);
        r.setMsgParams(msgParams);
        return r;
    }

    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(R<T> ret) {
        return 200 == ret.getCode();
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getMsgCode() {
        return this.msgCode;
    }

    public Object[] getMsgParams() {
        return this.msgParams;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public void setMsgParams(Object[] msgParams) {
        this.msgParams = msgParams;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof R)) {
            return false;
        } else {
            R<?> other = (R)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getCode() != other.getCode()) {
                return false;
            } else {
                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if (this$msg == null) {
                    if (other$msg != null) {
                        return false;
                    }
                } else if (!this$msg.equals(other$msg)) {
                    return false;
                }

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
                    Object this$data = this.getData();
                    Object other$data = other.getData();
                    if (this$data == null) {
                        if (other$data != null) {
                            return false;
                        }
                    } else if (!this$data.equals(other$data)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof R;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCode();
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        Object $msgCode = this.getMsgCode();
        result = result * 59 + ($msgCode == null ? 43 : $msgCode.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getMsgParams());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "R(code=" + this.getCode() + ", msg=" + this.getMsg() + ", msgCode=" + this.getMsgCode() + ", msgParams=" + Arrays.deepToString(this.getMsgParams()) + ", data=" + this.getData() + ")";
    }
}