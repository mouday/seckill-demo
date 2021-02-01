package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 秒杀库存表 Mapper 接口
 * </p>
 *
 * @author mouday
 * @since 2020-12-27
 */
public interface SeckillDao {
    /**
     * 减库存
     * @return 表示更新记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 更id查询秒杀对象
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * command + N 快速生成测试代码
     * @Param 运行期参数, 如果不指定，会抛出异常 BindingException
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
//    java运行时没有保存形参的记录
//    queryAll(int offset, int limit)
//     =》
//    queryAll(int arg0, int arg1)
}
