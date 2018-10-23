package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.DeliveryInfo;

public interface DeliveryInfoSrvice {
    void save(String deliveryId, DeliveryInfo model);
}
