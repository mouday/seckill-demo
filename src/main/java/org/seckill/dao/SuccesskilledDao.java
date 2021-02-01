package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * <p>
 * 秒杀成功明细 Mapper 接口
 * </p>
 *
 * @author mouday
 * @since 2020-12-27
 */
public interface SuccesskilledDao {
    /**
     * 插入购买明细，可以过滤重复
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询，并携带秒杀产品对象
     */
    SuccessKilled queryByIdWithSecKill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
