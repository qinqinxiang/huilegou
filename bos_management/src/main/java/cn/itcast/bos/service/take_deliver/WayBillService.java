package cn.itcast.bos.service.take_deliver;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WayBillService {
    void save(WayBill wayBill);

    /*Page<WayBill> findAll(Pageable pageable);*/
    //运单分页查询
    Page<WayBill> findAll(WayBill model, Pageable pageable);

    WayBill findByWayBillNum(String wayBillNum);

    void sysIndex();
}
