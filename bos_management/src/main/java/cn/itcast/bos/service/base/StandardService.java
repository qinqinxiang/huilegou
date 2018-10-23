package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StandardService {

    void save(Standard standard);


    Page<Standard> findListStandard(Pageable pageable);


    List<Standard> findAll();

}
