package cn.itcast.bos.transit.action;

import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.service.transit.SignInfoService;
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
public class SignInfoAction extends BaseAction<SignInfo> {
    private String signInfoIds;

    public String getSignInfoIds() {
        return signInfoIds;
    }

    public void setSignInfoIds(String signInfoIds) {
        this.signInfoIds = signInfoIds;
    }

    @Autowired
    private SignInfoService signInfoService;

    @Action(value = "save_sigInfo",
            results = {@Result(name = "success", type = "redirect", location = "pages/transit/transitinfo.html")})
    public String save_sigInfo() {
        signInfoService.save(signInfoIds, model);
        return SUCCESS;
    }
}
