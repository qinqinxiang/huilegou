package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CourierService {

    void save(Courier courier);

    Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

    void updateltag(String[] ids);

    List<Courier> findNoAssociationCourier();

}
