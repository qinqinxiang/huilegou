package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@Actions
public class CourierAction extends BaseAction<Courier> {
    //注入service
    @Autowired
    private CourierService courierService;


    //添加派件员
    @Action(value = "save_courier", results = {@Result(name = "success", type = "redirect",
            location = "pages/base/courier.html")})
    public String save() {
        courierService.save(model);
        return SUCCESS;
    }

    //改造成有条件查询
    @Action(value = "pageQuery", results = {@Result(name = "success", type = "json")})
    public String page() {
        //构造条件
        Pageable pageable = new PageRequest(page - 1, rows);

        //构建查询条件
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                //根据工号查询
                if (StringUtils.isNotBlank(model.getCourierNum())) {
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
                    list.add(p1);
                }

                //根据公司查询
                if (StringUtils.isNotBlank(model.getCompany())) {
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
                    list.add(p2);
                }

                //根据派件员类型查询
                if (StringUtils.isNotBlank(model.getType())) {
                    Predicate p3 = cb.equal(root.get("type").as(String.class), model.getType());
                    list.add(p3);
                }

                //根据收派标准查询
                Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);
                if (model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())) {
                    Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%" + model.getStandard().getName() + "%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Courier> page = courierService.findAll(specification, pageable);
        //将数据写回页面
        setValueStack(page);
        return SUCCESS;
    }


    //派送员作废和批量作废
    private String idStr;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    @Action(value = "updltag", results = {@Result(name = "success", type = "redirect",
            location = "pages/base/courier.html")})
    public String updateltag() {
        System.out.println(idStr);
        String[] ids = idStr.split(",");
        //调用业务层
        courierService.updateltag(ids);
        return SUCCESS;
    }

    //关联快递员 选择快递员
    @Action(value = "findNoAssociationCourier", results = {@Result(name = "success", type = "json")})
    public String findcourier() {
        //查找未关联定区快递员
        List<Courier> courier = courierService.findNoAssociationCourier();
        //将数据压入值栈
        ActionContext.getContext().getValueStack().push(courier);
        return SUCCESS;

    }
}
