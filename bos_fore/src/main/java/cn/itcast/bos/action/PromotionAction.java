package cn.itcast.bos.action;

import cn.itcast.bos.domain.take_delivery.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
    private int pageSize;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    //宣传活动前台页面分页显示
    /*
    需要向后台传送pageSize 和total所以
    * 因为数据需要从后台系统中查询，所以需要将
    * */
    @Action(value = "promotionPage", results = {@Result(name = "success", type = "json")})
    public String promotionPage() {
        rows = pageSize;
        //webService远程连接
        PageBean<Promotion> pageBean = WebClient.
                create("http://localhost:9090/bos_management/services/promotionService/promotionPage?page=" + page + "&rows=" + rows)
                .accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        //将数据压入值栈
        ActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;
    }

    //点击活动详情跳转页面
    @Action(value = "promotion_detailShow")
    public String showPromotion() throws IOException, TemplateException {
        //根据id判断对应html文件是否存在
        String freemarker = ServletActionContext.getServletContext().getRealPath("freemarker");
        File file = new File(freemarker + "/" + model.getId() + ".html");
        if (!file.exists()) {
            //文件不存在
            //创建configuration
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            //指定模板
            configuration.setDirectoryForTemplateLoading(
                    new File(ServletActionContext.getServletContext().getRealPath("WEB-INF/freemarker_tmp")));
            //获取模板对象
            Template template = configuration.getTemplate("freemarker_tmps.ftl");
            //加载动态数据
            Promotion promotion = WebClient.create("http://localhost:9090/bos_management/services/promotionService/promotionHtml/" + model.getId())
                    .accept(MediaType.APPLICATION_JSON).get(Promotion.class);
            //将数据添加到map集合中
            Map<String, Object> map = new HashMap<>();
            map.put("promotion", promotion);
            //将数据返回
            template.process(map, new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
        }

        //防止页面乱码
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        //文件存在，使用字节输出流将文件写回页面
        FileUtils.copyFile(file, ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }
}
