package com.kkk.exception;


import com.kkk.entity.enums.ResponseCodeEnum;
import lombok.Getter;

/**
 * ClassName: BusinessException
 * Description:
 *
 * @Author kkk
 * @Create 2023/10/28 17:38
 */
@Getter
public class BusinessException extends RuntimeException{
    private ResponseCodeEnum codeEnum;
    private Integer code;
    private String message;

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(Throwable e) {
        super(e);
    }

    public BusinessException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 重写fillInStackTrace 业务异常不需要堆栈信息，提高效率
     * @return
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
