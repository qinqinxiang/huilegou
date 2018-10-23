package cn.itcast.bos.service.impl.base;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
    //添加定区
    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
    }

    //定区条件查询
    @Override
    public Page<FixedArea> fiexPageQuery(Specification specification, Pageable pageable) {

        return fixedAreaRepository.findAll(specification, pageable);
    }

    //关联快递员
    /*
     * 查询出定区，快递员，收派时间在做关联
     * */
    //注入takeTimeRepository,courierRepository
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;

    @Override
    public void associationCourier(FixedArea model, Integer courierId, Integer takeTimeId) {
        FixedArea fixedArea = fixedAreaRepository.findOne(model.getId());
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
        //定区关联快递员
        fixedArea.getCouriers().add(courier);
        //快递员关联收派时间
        courier.setTakeTime(takeTime);
    }
}
