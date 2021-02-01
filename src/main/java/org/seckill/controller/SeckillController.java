package org.seckill.controller;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


// url: 模块/资源/{id}/细分
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    // 列表
    @GetMapping(value = "/list")
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        // list.jsp + Model = ModelAndView
        return "list";  // 等价于 /WEB-INF/jsp/list.jsp
    }

    // 详情 返回html
    @GetMapping(value = "/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") Long seckillId,
                         Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list"; // 重定向
        }

        Seckill seckill = seckillService.getById(seckillId);

        if (seckill == null) {
            return "forward:/seckill/list"; // 转发
        }

        model.addAttribute("seckill", seckill); // model
        return "detail"; // view
    }

    // 返回json数据，访问cookie
    @PostMapping(value = "/{seckillId}/exposer",
            produces = {"application/json;charset=UTF-8"}
    )
    @ResponseBody
    public SeckillResult<Exposer> exportSeckillUrl(
            @PathVariable("seckillId") Long seckillId) {

        SeckillResult<Exposer> result;

        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);

            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }

        return result;
    }

    // 执行秒杀
    @PostMapping(value = "/{seckillId}/{md5}/execution",
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(
            @PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "phone", required = false) Long killPhone) {

        // springmvc valid
        if (killPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }

        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, killPhone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.CLOSE_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillResult<SeckillExecution>(true, e.getMessage());
        }

    }

    @GetMapping("/now/time")
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }
}
