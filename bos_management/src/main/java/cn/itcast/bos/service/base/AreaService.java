package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;

public interface AreaService {
    void upload(List<Area> areas);

    Page<Area> findPage(Specification<Area> specification, Pageable pageable);
}
