package cn.itcast.bos.quartz;

import cn.itcast.bos.service.take_deliver.PromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PromotionJob implements Job {
    //注入service
    @Autowired
    private PromotionService promotionService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        promotionService.updateType(new Date());
    }
}
