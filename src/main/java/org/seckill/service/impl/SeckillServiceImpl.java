package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccesskilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


@Service
public class SeckillServiceImpl implements SeckillService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // md5盐值，混淆结果
    private final String salt = "sfasfw3r3423423";

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccesskilledDao successkilledDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 5);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);

        if (seckill == null) {
            return new Exposer(false, seckillId);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        // 系统当前时间
        Date now = new Date();
        if (now.getTime() < startTime.getTime()
                || now.getTime() > endTime.getTime()) {

            return new Exposer(false, seckillId,
                    now.getTime(), startTime.getTime(), endTime.getTime());
        }

        String md5 = this.getMd5(seckillId); // TODO
        return new Exposer(true, seckillId, md5);
    }

    /**
     * 获取md5值
     *
     * @param seckillId
     * @return
     */
    private String getMd5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        logger.debug(md5);
        return md5;
    }

    /**
     * 执行秒杀
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws SeckillCloseException
     * @throws RepeatKillException
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
        if (md5 == null || !md5.equals(this.getMd5(seckillId))) {
            throw new SeckillException("签名校验失败");
        }

        Date now = new Date();

        // 减库存 + 购买行为记录
        try {
            // 1、减库存
            int updateCount = seckillDao.reduceNumber(seckillId, now);

            if (updateCount <= 0) {
                // 库存不够，秒杀结束
                throw new SeckillCloseException("秒杀结束");
            } else {
                //2、写入秒杀记录
                int insertCount = successkilledDao.insertSuccessKilled(seckillId, userPhone);

                if (insertCount <= 0) {
                    // 插入失败，重复秒杀
                    throw new RepeatKillException("重复秒杀");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successkilledDao.queryByIdWithSecKill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            // 所有编译异常，转化为运行期异常
            throw new SeckillException("秒杀失败");
        }
    }
}
