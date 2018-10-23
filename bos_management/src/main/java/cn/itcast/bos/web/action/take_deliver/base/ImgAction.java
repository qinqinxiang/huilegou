package cn.itcast.bos.web.action.take_deliver.base;

import cn.itcast.bos.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.aspectj.util.FileUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImgAction extends BaseAction<Object> {

    //图片上传
    private File imgFile;
    private String imgFileFileName;
    private String imgFileContentType;

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public String getImgFileFileName() {
        return imgFileFileName;
    }

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public String getImgFileContentType() {
        return imgFileContentType;
    }

    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }

    @Action(value = "imgUpload", results = {@Result(name = "none", type = "json")})
    public String upload() throws IOException {
        String urlPath = ServletActionContext.getRequest().getContextPath() + "/upload/";
        String saveUpload = ServletActionContext.getServletContext().getRealPath("/upload/");
    /*    System.out.println(urlPath);
        System.out.println(saveUpload);*/
        //文件名称'
//        System.out.println(imgFileFileName);
        UUID uuid = UUID.randomUUID();
        //生成上传文件名称
        String subimgName = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
        String uploadFileName = uuid + subimgName;
        //保存文件
        File dest = new File(saveUpload + "/" + uploadFileName);
        FileUtil.copyFile(imgFile, dest);
        System.out.println(uploadFileName + urlPath);
        //文件上传成功返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("error", 0);
        map.put("url", urlPath + uploadFileName);
        //将是数据压入值栈
        ActionContext.getContext().getValueStack().push(map);
        return NONE;
    }

    //图片空间管理
    @Action(value = "fileManager", results = {@Result(name = "success", type = "json")})
    public String fileManager() {
        //根目录路径，可以指定绝对路径，比如 /var/www/attached/
        String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
        //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
        //图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
        //目录不存在或不是目录
        File currentPathFile = new File(rootPath);
        //遍历目录取的文件信息
        List<Map<String, Object>> fileList = new ArrayList<>();
        for (File file : currentPathFile.listFiles()) {
            Map<String, Object> map = new HashMap<>();
            String fileName = file.getName();
            if (file.isDirectory()) {
                map.put("is_dir", true);
                map.put("has_file", (file.listFiles() != null));
                map.put("filesize", 0L);
                map.put("is_photo", false);
                map.put("filetype", "");
            } else if (file.isFile()) {
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                map.put("is_dir", false);
                map.put("has_file", false);
                map.put("filesize", file.length());
                map.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                map.put("filetype", fileExt);
            }
            map.put("filename", fileName);
            map.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
            fileList.add(map);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("moveup_dir_path", "");
        result.put("current_dir_path", rootPath);
        result.put("current_url", rootUrl);
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);
        //压入值栈
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
