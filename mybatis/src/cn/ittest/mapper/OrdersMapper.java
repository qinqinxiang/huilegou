package cn.ittest.mapper;

import cn.ittest.domain.Orders;

import java.util.List;

public interface OrdersMapper {
    Orders findOrdersById(Integer id);

    List<Orders> findOrderAll();

    List<Orders> findAllOrders();
}
