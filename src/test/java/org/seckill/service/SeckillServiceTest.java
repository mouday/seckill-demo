package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml",
})
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getById() {
        long seckillId = 1000;

        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long seckillId = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}", exposer);
        // exposer=Exposer{exposed=true, md5='28312864513c6c78248fd82b08ed73a7', seckillId=1000, now=0, start=0, end=0}
    }

    @Test
    public void executeSeckill() {
        long seckillId = 1000L;
        long userPhone = 13511023456L;
        String md5 = "28312864513c6c78248fd82b08ed73a7";

        SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
        logger.info("seckillExecution={}", seckillExecution);
        // seckillExecution=SeckillExecution{seckillId=1000, state=1, stateInfo='秒杀成功', successKilled=SuccessKilled(id=1000, userPhone=13511023456, state=0, createTime=Fri Jan 08 12:42:55 CST 2021, seckill=Seckill(id=1000, name=10000元秒杀iPhone, number=99, startTime=Fri Jan 01 14:00:00 CST 2021, endTime=Sun Dec 26 14:00:00 CST 2021, createTime=Mon Dec 28 10:52:45 CST 2020))}
    }

    // 集成测试逻辑
    @Test
    public void executeSeckillLogic() {
        long seckillId = 1000L;
        long userPhone = 13511023456L;

        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}", exposer);

        if (exposer.isExposed()) {
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(
                        seckillId, userPhone, exposer.getMd5());

                logger.info("seckillExecution={}", seckillExecution);
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }
        }
    }
}