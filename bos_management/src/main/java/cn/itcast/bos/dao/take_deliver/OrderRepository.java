package cn.itcast.bos.dao.take_deliver;

import cn.itcast.bos.domain.take_delivery.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    //订单数据表格回显
    Order findByOrderNum(String orderNum);
}
