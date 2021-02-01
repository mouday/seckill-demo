package org.seckill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 秒杀成功明细
 * </p>
 *
 * @author mouday
 * @since 2020-12-27
 */

@Data
public class SuccessKilled {

    /**
     * 秒杀商品id
     */
    private Long id;

    /**
     * 用户手机号
     */
    private Long userPhone;

    /**
     * 状态：-1：无效 0：成功 1：已付款
     */
    private Integer state;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 多对一
     */
    private Seckill seckill;

}
