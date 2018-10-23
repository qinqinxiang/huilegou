package cn.itcast.bos.transit.action;

import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.service.transit.DeliveryInfoSrvice;
import cn.itcast.bos.web.action.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class DeliveryInfoAction extends BaseAction<DeliveryInfo> {
    private String deliveryId;

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    @Autowired
    private DeliveryInfoSrvice deliveryInfoSrvice;

    @Action(value = "save_signInfo",
            results = {@Result(name = "success", type = "redirect", location = "pages/transit/transitinfo.html")})
    public String save_signInfo() {
        //保存配送信息
        deliveryInfoSrvice.save(deliveryId, model);
        return SUCCESS;
    }
}
