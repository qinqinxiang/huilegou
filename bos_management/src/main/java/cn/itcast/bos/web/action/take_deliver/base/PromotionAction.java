package cn.itcast.bos.web.action.take_deliver.base;


import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_deliver.PromotionService;
import cn.itcast.bos.web.action.base.BaseAction;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
    //文件上传
    private File titleImgFile;
    private String titleImgFileFileName;

    public File getTitleImgFile() {
        return titleImgFile;
    }

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public String getTitleImgFileFileName() {
        return titleImgFileFileName;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    //注入service
    @Autowired
    private PromotionService promotionService;

    @Action(value = "savePromotion", results = {@Result(name = "success", type = "redirect",
            location = "pages/take_delivery/promotion.html")})
    public String savePromotion() throws IOException {
        //(项目路径)
        String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
        //(根路径)
        String savePath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";

        //生成图片名
        UUID uuid = UUID.randomUUID();
        String subName = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
        String newFileName = uuid + subName;
        //创建服务器端保存图片
        File dest = new File(savePath + "/" + newFileName);
        //保存图片
        FileUtil.copyFile(titleImgFile, dest);
        //设置图片保存路径
        model.setTitleImg(saveUrl + newFileName);
        //调用业务层保存数据
        promotionService.save(model);
        return SUCCESS;
    }

    //后台宣传活动展示
    @Action(value = "pagePromotion", results = {@Result(name = "success", type = "json")})
    public String pages() {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> page = promotionService.findPagePromotion(pageable);
        //压入值栈
        setValueStack(page);
        return SUCCESS;
    }

}
