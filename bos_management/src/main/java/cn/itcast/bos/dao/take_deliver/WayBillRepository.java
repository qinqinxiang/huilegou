package cn.itcast.bos.dao.take_deliver;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WayBillRepository extends JpaRepository<WayBill, Integer> {
    //运单数据表格回显
    WayBill findByWayBillNum(String wayBillNum);
}
