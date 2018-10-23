package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
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
@Actions
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    private Standard standard = new Standard();

    @Override
    public Standard getModel() {
        return standard;
    }

    //注入service
    @Autowired
    private StandardService standardService;

    //添加收派标准
    @Action(value = "save_from", results = {@Result(name = "success", type = "redirect",
            location = "pages/base/standard.html")})
    public String save() {
        standardService.save(standard);
        return SUCCESS;
    }


    private Integer page; //当前页
    private Integer rows; //每页显示的数据

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    //分页
    @Action(value = "pagination_from", results = {@Result(name = "success", type = "json")})
    public String pagination() {
        //查询分页
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Standard> page = standardService.findListStandard(pageable);
        //创建Map集合
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());
        //将数据写会到页面
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }

    //快递员取派标准查询
    @Action(value = "findAll", results = {@Result(name = "success", type = "json")})
    public String find() {
        List<Standard> standards = standardService.findAll();
        //将数据写回到页面
        ActionContext.getContext().getValueStack().push(standards);
        return SUCCESS;
    }
}
