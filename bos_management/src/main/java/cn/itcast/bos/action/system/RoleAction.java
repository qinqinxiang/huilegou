package cn.itcast.bos.action.system;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.base.BaseAction;
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
public class RoleAction extends BaseAction<Role> {
    @Autowired
    private RoleService roleService;

    //角色数据列表展示
    @Action(value = "role_list", results = {@Result(name = "success", type = "json")})
    public String roleList() {
        List<Role> list = roleService.findAll();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

    //属性驱动接收数据
    private String menuIds;
    private String[] permissionIds;

    public String getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    public String[] getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    //角色添加
    @Action(value = "role_add",
            results = {@Result(name = "success", type = "redirect", location = "pages/system/role.html")})
    public String role_add() {
        roleService.save(model, menuIds, permissionIds);
        return SUCCESS;
    }
}
