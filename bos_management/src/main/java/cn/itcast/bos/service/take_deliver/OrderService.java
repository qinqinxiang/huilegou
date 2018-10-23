package cn.itcast.bos.service.take_deliver;

import cn.itcast.bos.domain.take_delivery.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface OrderService {
    //添加订单
    @Path("/addOrder")
    @POST
    @Consumes({"application/xml,application/json"})
    public void addOrder(Order order);

    Order findByOrderNum(String orderNum);
}
