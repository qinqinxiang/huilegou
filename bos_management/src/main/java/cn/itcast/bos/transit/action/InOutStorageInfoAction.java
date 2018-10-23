package cn.itcast.bos.transit.action;

import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.service.transit.InOutStorageInfoService;
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
public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo> {

    //出入库操作
    private String inOutStoreId;

    public String getInOutStoreId() {
        return inOutStoreId;
    }

    public void setInOutStoreId(String inOutStoreId) {
        this.inOutStoreId = inOutStoreId;
    }

    @Autowired
    private InOutStorageInfoService inOutStorageInfoService;

    @Action(value = "save_InOutStoragelnfo",
            results = {@Result(name = "success", type = "redirect", location = "pages/transit/transitinfo.html")})
    public String save_InOutStoragelnfo() {
        inOutStorageInfoService.save(inOutStoreId, model);
        return SUCCESS;
    }
}
