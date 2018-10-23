package cn.itcast.bos.web.action.take_deliver.base;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_deliver.WayBillService;
import cn.itcast.bos.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
    //日志记录
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(WayBillAction.class));
    @Autowired
    private WayBillService wayBillService;

    @Action(value = "save_waybill_quick", results = {@Result(name = "success", type = "json")})
    public String saveWayBill() {
        //创建Map集合用来给页面返回json格式数据
        Map<String, Object> map = new HashMap<>();
        Date date = new Date();

        try {
            //手动录入运单，去除没有id的订单
            if (model.getOrder() != null && (model.getOrder().getId() == null
                    || model.getOrder().getId() == 0)) {
                model.setOrder(null);
            }
            //调用service
            wayBillService.save(model);
            map.put("success", true);
            map.put("msg", "保存成功");
            //添加日志信息
            LOGGER.info("保存运单成功，运单号:" + model.getWayBillNum() + ",保存时间:" + date);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "保存失败");
            LOGGER.info("保存运单失败，运单号:" + model.getWayBillNum() + ",保存时间:" + date);
        }
        //将数据压入值栈
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }

    //运单后台分页展示
    @Action(value = "wayBill_Query", results = {@Result(name = "success", type = "json")})
    public String wayBillPage() {
        Pageable pageable = new PageRequest
                (page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
        //调用业务层
        Page<WayBill> page = wayBillService.findAll(model, pageable);
        //将数据压入值栈
        setValueStack(page);
        return SUCCESS;
    }

    //运单数据表格回显
    @Action(value = "loadWayBillShow", results = {@Result(name = "success", type = "json")})
    public String showWayBill() {
        Map<String, Object> map = new HashMap<>();
        //调用业务层查询数据
        WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
        if (wayBill == null) {
            map.put("success", false);
        } else {
            map.put("success", true);
            map.put("wayBillData", wayBill);
        }
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }
}
