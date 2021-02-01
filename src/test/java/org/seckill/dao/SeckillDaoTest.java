package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 配置spring和junit整合，junit启动时加载SpringIOC容器
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
// 配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //依赖注入
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() {
        Date date = new Date();

        int updateCount = seckillDao.reduceNumber(1000L, date);
        System.out.println(updateCount);
    }

    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        List<Seckill> list = seckillDao.queryAll(0, 100);

        list.forEach(System.out::println);

    }
}