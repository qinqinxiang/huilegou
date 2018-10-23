import cn.itcast.bos.domain.base.Courier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        final Courier courier = new Courier();
        Specification<Courier> specification = new Specification<Courier>() {
            /*
             * 利用CriteriaBuilder构造复杂查询条件
             * */
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //查询单前用户的根用户
                //单表查询

                //首先查询派件员的工号StringUtils.isNoneBlank()判断是否为空
                //创建list集合
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNoneBlank(courier.getCourierNum())) {
                    //利用cb构造查询条件
                    Predicate p1 = cb.equal(root.get("courierNum"), courier.getCourierNum());
                    list.add(p1);
                }
                //查询公司
                if (StringUtils.isNoneBlank(courier.getCompany())) {
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
                    list.add(p2);
                }
                //查询快递员类型
                if (StringUtils.isNoneBlank(courier.getType())) {
                    Predicate p3 = cb.equal(root.get("type"), courier.getType());
                    list.add(p3);
                }

                //多表查询
                //根据取派标准查询
                //利用Courier（root），关联Standard
                Join<Object, Object> predicate = root.join(courier.getStandard().getName());
                if (courier.getStandard() != null && StringUtils.isNoneBlank(courier.getStandard().getName())) {
                    Predicate p4 = cb.like(predicate.get("name").as(String.class), "%" +
                            courier.getStandard().getName() + "%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
    }
}
