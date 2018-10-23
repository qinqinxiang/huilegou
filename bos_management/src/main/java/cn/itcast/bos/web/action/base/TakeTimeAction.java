package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime> {
    //注入service
    @Autowired
    private TakeTimeService takeTimeService;

    //关联快递员 选择收派时间
    @Action(value = "taketime", results = {@Result(name = "success", type = "json")})
    public String taketime() {
        List<TakeTime> list = takeTimeService.findAll();
        //压入值栈
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }
}
