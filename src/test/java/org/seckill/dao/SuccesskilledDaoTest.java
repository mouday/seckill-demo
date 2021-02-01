package org.seckill.dao;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
// 配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccesskilledDaoTest {

    @Resource
    SuccesskilledDao successkilledDao;

    /**
     * 第一次：insertCount=1
     * 第二次：insertCount=0
     */
    @Test
    public void insertSuccessKilled() {
        long id = 1000L;
        long phone = 13500782295L;

        int insertCount = successkilledDao.insertSuccessKilled(id, phone);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithSecKill() {
        long id = 1000L;
        long phone = 13500782295L;
        SuccessKilled successKilled = successkilledDao.queryByIdWithSecKill(id, phone);

        System.out.println(JSON.toJSONString(successKilled));
    }
}