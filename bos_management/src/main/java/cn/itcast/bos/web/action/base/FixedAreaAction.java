package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
    //添加定区
    @Autowired
    private FixedAreaService fixedAreadService;

    @Action(value = "fixedFrom_save", results = {@Result(name = "success", type = "redirect",
            location = "pages/base/fixed_area.html")})
    public String save() {
        fixedAreadService.save(model);
        return SUCCESS;
    }

    //定区按条件查询+分页
    @Action(value = "fixedPageQuery", results = {@Result(name = "success", type = "json")})
    public String query() {
        Pageable pageable = new PageRequest(page - 1, rows);
        //构建条件
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                //根据定区编码查询
                if (StringUtils.isNotBlank(model.getId())) {
                    Predicate p1 = cb.equal(root.get("id"), model.getId());
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCompany())) {
                    Predicate p2 = cb.like(root.get("company").as(String.class), model.getCompany());
                    list.add(p2);
                }

                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        Page<FixedArea> page = fixedAreadService.fiexPageQuery(specification, pageable);
        //压入值栈
        setValueStack(page);
        return SUCCESS;
    }

    //查询未关联地区客户
    @Action(value = "findnoassociationcustomersid", results = {@Result(name = "success", type = "json")})
    public String findnoassociationcustomersid() {
        Collection<? extends Customer> collection = WebClient.create("http://localhost:9091/crm_management/services/customerService/noassociationcustomers")
                .accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        //将数据压入栈顶
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }


    //查询已关联地区客户
    @Action(value = "findhasassociationcustomersid", results = {@Result(name = "success", type = "json")})
    public String associationcustomers() {
        Collection<? extends Customer> collection1 =
                WebClient.create("http://localhost:9091/crm_management/services/customerService/associationcustomers/" + model.getId())
                        .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        //将数据压入栈顶
        ActionContext.getContext().getValueStack().push(collection1);
        return SUCCESS;
    }

    //关联客户
    private String[] customerIds;

    public String[] getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    @Action(value = "decidedzone_assigncustomerstodecidedzone",
            results = {@Result(name = "success", type = "redirect", location = "pages/base/fixed_area.html")})
    public String associationCustomertoFiexdArea() {
        System.out.println(model.getId());
        //将customerid拼接成字符串
        String customerids = StringUtils.join(customerIds, ",");
        WebClient.create("http://localhost:9091/crm_management/services/customerService/associationcustomerstofixedArea?customerId=" +
                customerids + "&fixedAreaId=" + model.getId()).put(null);
        return SUCCESS;
    }

    //关联快递员
    //接收快递员id和收派标准id
    private Integer courierId;
    private Integer takeTimeId;

   /* id: 123
    courierId: 43
    takeTimeId: 2*/

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public Integer getTakeTimeId() {
        return takeTimeId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    @Action(value = "fixedArea_associationCourierToFixedArea",
            results = {@Result(name = "success", type = "redirect", location = "pages/base/fixed_area.html")})
    public String associationCourier() {

        fixedAreadService.associationCourier(model, courierId, takeTimeId);
        return SUCCESS;
    }
}
