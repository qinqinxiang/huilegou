package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface FixedAreaService {
    void save(FixedArea model);

    Page<FixedArea> fiexPageQuery(Specification specification, Pageable pageable);


    void associationCourier(FixedArea model, Integer courierId, Integer takeTimeId);
}
