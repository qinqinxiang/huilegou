package cn.itcast.bos.service.impl.base;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
    //注入courierRepository
    @Autowired
    private CourierRepository courierRepository;

    //添加派件员
    @Override
    @RequiresPermissions("courier_add")
    public void save(Courier courier) {
        courierRepository.save(courier);
    }


    //有条件查询
    @Override
    public Page<Courier> findAll(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification, pageable);
    }

    //批量作废
    @Override
    public void updateltag(String[] ids) {
        //遍历ids数组
        for (String idss : ids) {
            Integer id = Integer.parseInt(idss);
            courierRepository.updateltag(id);
        }
    }

    //查询未关联定区快递员
    @Override
    public List<Courier> findNoAssociationCourier() {
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
                return p;
            }
        };
        return courierRepository.findAll(specification);
    }
}
