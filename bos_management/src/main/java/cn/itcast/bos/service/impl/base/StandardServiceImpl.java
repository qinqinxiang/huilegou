package cn.itcast.bos.service.impl.base;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.service.base.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {
    @Autowired
    private StandardRepository standardRepository;

    //添加收派标准
    @Override
    @CacheEvict(cacheNames = "standard", allEntries = true)//清除缓存
    public void save(Standard standard) {
        standardRepository.save(standard);
    }

    //分页
    @Override
    @Cacheable(value = "standard", key = "#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<Standard> findListStandard(Pageable pageable) {
        return (Page<Standard>) standardRepository.findAll(pageable);
    }

    //快递员取派标准查询
    @Override
    @Cacheable("standard")//添加缓存
    public List<Standard> findAll() {
        return standardRepository.findAll();
    }
}
