package cn.itcast.bos.service.impl.transit;

import cn.itcast.bos.dao.transit.DeliveryInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.DeliveryInfoSrvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoSrvice {
    //注入dao
    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;
    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Override
    public void save(String deliveryId, DeliveryInfo deliveryInfo) {
        deliveryInfoRepository.save(deliveryInfo);
        //调用业务层查询运输配送信息
        if (deliveryId != null) {
            TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(deliveryId));
            transitInfo.setDeliveryInfo(deliveryInfo);
            //修改运输配送信息
            transitInfo.setStatus("开始配送");
        }
    }
}
