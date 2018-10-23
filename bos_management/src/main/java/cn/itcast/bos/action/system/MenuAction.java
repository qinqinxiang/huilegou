package cn.itcast.bos.action.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
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

import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu> {
    @Autowired
    private MenuService menuService;

    @Action(value = "menus_pageQuery", results = {@Result(name = "success", type = "json")})
    public String menu_pageQuery() {
        String pageStr = ServletActionContext.getRequest().getParameter("page");
        Integer pages = Integer.parseInt(pageStr);
     /*   List<Menu> list=menuService.findListMenu();
        ActionContext.getContext().getValueStack().push(list);*/
        Pageable pageable = new PageRequest(pages - 1, rows);
        Page<Menu> page = menuService.findAll(pageable);
        setValueStack(page);
        return SUCCESS;
    }

    //添加菜单 下拉列表选项
    @Action(value = "menus_findAll", results = {@Result(name = "success", type = "json")})
    public String menu_findAll() {

        List<Menu> list = menuService.findListMenu();
        ActionContext.getContext().getValueStack().push(list);

        return SUCCESS;
    }

    //保存菜单
    @Action(value = "save_menu",
            results = {@Result(name = "success", type = "redirect", location = "pages/system/menu.html")})
    public String save_menu() {

        menuService.save(model);

        return SUCCESS;
    }

    //根据当前登录用户显示不同的菜单
    @Action(value = "menuShow",
            results = {@Result(name = "success", type = "json")})
    public String menuShow() {
        //获取单前登录的用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //调用业务层查询数据
        List<Menu> list = menuService.findByUser(user);
        //将数据写回页面
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }
}
