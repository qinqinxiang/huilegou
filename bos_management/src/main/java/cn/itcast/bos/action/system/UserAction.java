package cn.itcast.bos.action.system;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
public class UserAction extends BaseAction<User> {
    @Autowired
    private UserService userService;

    //后台用户登录
    @Action(value = "user_login", results =
            {@Result(name = "success", type = "redirect", location = "index.html "),
                    @Result(name = "login", type = "redirect", location = "login.html")})
    public String login() {
        //基于shiro登录
        Subject subject = SecurityUtils.getSubject();
        //保存用户名和密码
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());

        try {
            //用户登录
            subject.login(token);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LOGIN;
        }
    }

    //用户退出登录
    @Action(value = "user_logout",
            results = {@Result(name = "success", type = "redirect", location = "login.html")})
    public String logout() {
        //获取
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }

    //用户后台数据展示
    @Action(value = "user_list", results = {@Result(name = "success", type = "json")})
    public String user_list() {
        List<User> list = userService.findAll();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }


    //用户添加
    private String[] roleIds;

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    @Action(value = "user_save",
            results = {@Result(name = "success", type = "redirect", location = "pages/system/userlist.html")})
    public String user_save() {
        userService.save(model, roleIds);
        return SUCCESS;
    }
}
