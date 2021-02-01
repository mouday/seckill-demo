package org.seckill.exception;

/**
 * 秒杀业务相关的异常
 */
public class SeckillException extends RuntimeException{
    public SeckillException() {
    }

    public SeckillException(String message) {
        super(message);
    }
}
