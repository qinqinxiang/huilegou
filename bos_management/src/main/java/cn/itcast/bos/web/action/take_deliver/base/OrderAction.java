package cn.itcast.bos.web.action.take_deliver.base;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_deliver.OrderService;
import cn.itcast.bos.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
    //注入service
    @Autowired
    private OrderService orderService;

    //订单数据表格回显
    @Action(value = "loadOrderShow", results = {@Result(name = "success", type = "json")})
    public String showOrder() {
        Map<String, Object> map = new HashMap<>();
        //调用业务层查询订单
        Order order = orderService.findByOrderNum(model.getOrderNum());
        if (order == null) {
            map.put("success", false);
        } else {
            map.put("success", true);
            map.put("orderData", order);
        }
        //将数据回写
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }
}
