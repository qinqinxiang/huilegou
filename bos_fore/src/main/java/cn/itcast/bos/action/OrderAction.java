package cn.itcast.bos.action;

import cn.itcast.bos.conston.HttPUrl;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.utils.BaseAction;
import cn.itcast.crm.domain.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
    //用户添加订单
    //属性驱动接收收件人和发件人地址信息
    private String sendAreaInfo;
    private String recAreaInfo;

    public String getSendAreaInfo() {
        return sendAreaInfo;
    }

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public String getRecAreaInfo() {
        return recAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    @Action(value = "addOrder", results = {@Result(name = "success", type = "redirect", location = "index.html")})
    public String addOrder() {
        //创建地址对象
        //设置发件人地址信息
        Area sendArea = new Area();
        String[] sendAreaInfoStr = sendAreaInfo.split("/");
        sendArea.setProvince(sendAreaInfoStr[0]); //省
        sendArea.setCity(sendAreaInfoStr[1]);//
        sendArea.setDistrict(sendAreaInfoStr[2]);
        //设置收件人地址信息
        Area recArea = new Area();
        String[] recAreaInfoStr = recAreaInfo.split("/");
        recArea.setProvince(recAreaInfoStr[0]);
        recArea.setCity(recAreaInfoStr[1]);
        recArea.setDistrict(recAreaInfoStr[2]);
        //将收、发件人信息设置到订单中
        model.setSendArea(sendArea);
        model.setRecArea(recArea);
        //封装订单属于的用户
        Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
        model.setCustomer_id(customer.getId());
        //调用webservice
        WebClient.create(HttPUrl.BOS_MANAGEMENT_URL + "/bos_management/services/orderService/addOrder").
                type(MediaType.APPLICATION_JSON).post(model);
        return SUCCESS;
    }
}
