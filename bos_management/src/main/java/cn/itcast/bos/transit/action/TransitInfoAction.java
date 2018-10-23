package cn.itcast.bos.transit.action;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.TransitInfoService;
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
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo> {
    //属性驱动
    private String wayBillIds;

    public String getWayBillIds() {
        return wayBillIds;
    }

    public void setWayBillIds(String wayBillIds) {
        this.wayBillIds = wayBillIds;
    }

    //注入service
    @Autowired
    private TransitInfoService transitInfoService;

    //开启中转
    @Action(value = "createTransit", results = {@Result(name = "success", type = "json")})
    public String createTransit() {
        transitInfoService.createTransit(wayBillIds);
        //创建map集合封装消息
        Map<String, Object> map = new HashMap<>();
        //封装结果数据
        try {
            map.put("success", true);
            map.put("msg", "开始中转成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "开始中转失败");
        }
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }

    //后台物流信息查询
    @Action(value = "page_transitinfo", results = {@Result(name = "success", type = "json")})
    public String page_transitinfo() {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<TransitInfo> page = transitInfoService.findPageDate(pageable);
        setValueStack(page);
        return SUCCESS;
    }
}
