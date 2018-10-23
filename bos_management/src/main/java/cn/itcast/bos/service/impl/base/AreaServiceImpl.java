package cn.itcast.bos.service.impl.base;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaRepository areaRepository;

    //文件上传
    @Override
    public void upload(List<Area> areas) {
        areaRepository.save(areas);
    }

    @Override
    public Page<Area> findPage(Specification<Area> specification, Pageable pageable) {

        return areaRepository.findAll(specification, pageable);
    }


}
