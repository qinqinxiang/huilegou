package cn.itcast.bos.service.take_deliver;

import cn.itcast.bos.domain.take_delivery.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.ws.rs.*;
import java.util.Date;

public interface PromotionService {
    void save(Promotion model);

    Page<Promotion> findPagePromotion(Pageable pageable);

    //宣传活动前台分页展示数据
    @Path("/promotionPage")
    @GET
    @Produces({"application/xml,application/json"})
    PageBean<Promotion> findAll(@QueryParam("rows") int rows, @QueryParam("page") int page);

    //动态生成html页面根据id获取数据
    @Path("/promotionHtml/{id}")
    @GET
    @Produces({"application/xml,application/json"})
    Promotion findPromotion(@PathParam("id") Integer id);

    void updateType(Date date);
}
