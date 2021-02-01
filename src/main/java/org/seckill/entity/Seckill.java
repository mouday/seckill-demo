package org.seckill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 秒杀库存表
 * </p>
 *
 * @author mouday
 * @since 2020-12-27
 */

@Data
public class Seckill {
    /**
     * 商品库存id
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 库存数量
     */
    private Integer number;

    /**
     * 秒杀开始时间
     */
    private Date startTime;

    /**
     * 秒杀结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
