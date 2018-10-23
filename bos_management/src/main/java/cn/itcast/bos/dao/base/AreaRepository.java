package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AreaRepository extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area> {
    //根据省市区查分区域信息
    Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}
