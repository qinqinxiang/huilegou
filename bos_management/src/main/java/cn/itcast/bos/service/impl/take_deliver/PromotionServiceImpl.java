package cn.itcast.bos.service.impl.take_deliver;

import cn.itcast.bos.dao.take_deliver.PromotionRepository;
import cn.itcast.bos.domain.take_delivery.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_deliver.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    //注入dao
    @Autowired
    private PromotionRepository promotionRepository;

    //添加宣传活动
    @Override
    public void save(Promotion model) {
        promotionRepository.save(model);
    }

    //后台分页展示添加商品活动
    @Override
    public Page<Promotion> findPagePromotion(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    //宣传活动前台数据分页展示
    @Override
    public PageBean<Promotion> findAll(int rows, int page) {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> pageData = promotionRepository.findAll(pageable);

        PageBean<Promotion> pageBean = new PageBean<>();

        //封装总页数
        pageBean.setTotalCount(pageData.getTotalElements());
        //封装每页显示的数据
        pageBean.setPageData(pageData.getContent());
        return pageBean;
    }

    //生成html页面动态获取数据
    @Override
    public Promotion findPromotion(Integer id) {
        return promotionRepository.findOne(id);
    }

    //定时更改活动日期
    @Override
    public void updateType(Date date) {
        promotionRepository.updateType(date);
    }
}
