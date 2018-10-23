package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.base.PinYin4jUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    //注入service
    @Autowired
    private AreaService areaService;

    //文件上传
    @Action(value = "upload")
    public String upload() throws IOException {
        //xls解析用HSSF
        //获取解析文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        //读取sheet
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        List<Area> areas = new ArrayList<Area>();
        //遍历sheet
        for (Row row : sheet) {
            //判断是否是第一行
            if (row.getRowNum() == 0) {
                continue;
            }
            //判断最后一行是否为空
            if (row.getCell(0) == null && StringUtils.isBlank
                    (row.getCell(0).getStringCellValue())) {
                continue;
            }
            //创建Area对象
            Area area = new Area();
            area.setId(row.getCell(0).getStringCellValue());
            System.out.println(row.getCell(0).getStringCellValue());
            area.setProvince(row.getCell(1).getStringCellValue());
            area.setCity(row.getCell(2).getStringCellValue());
            area.setDistrict(row.getCell(3).getStringCellValue());
            area.setPostcode(row.getCell(4).getStringCellValue());
            areas.add(area);
            //生成区域简码
            String province = area.getProvince();//获取省份
            String city = area.getCity();//获取城市
            String district = area.getDistrict();//

            String p1 = province.substring(0, province.length() - 1);
            String c1 = city.substring(0, city.length() - 1);
            String d1 = district.substring(0, district.length() - 1);
            String[] headByString = PinYin4jUtils.getHeadByString(p1 + c1 + d1);
            StringBuilder sb = new StringBuilder();
            for (String head : headByString) {
                sb.append(head);
            }
            String s = sb.toString();
            area.setShortcode(s);
            //生成城市编码
            String ct = PinYin4jUtils.hanziToPinyin(c1, "");
            area.setCitycode(ct);
        }

        //调用业务层保存
        areaService.upload(areas);
        return NONE;
    }

    //分页
    @Action(value = "pagesQuery", results = {@Result(name = "success", type = "json")})
    public String queryPage() {
        Pageable pageable = new PageRequest(page - 1, rows);
        //构造查询条件
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                //查询省
                if (StringUtils.isNotBlank(model.getProvince())) {
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    list.add(p1);
                }
                //查询市
                if (StringUtils.isNotBlank(model.getCity())) {
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
                }

                //查询区域
                if (StringUtils.isNotBlank(model.getDistrict())) {
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    list.add(p3);
                }

                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Area> page = areaService.findPage(specification, pageable);
        //将数据写回页面
        setValueStack(page);
        return SUCCESS;
    }
}
