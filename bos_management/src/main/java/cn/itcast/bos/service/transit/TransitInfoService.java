package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.TransitInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransitInfoService {
    void createTransit(String wayBillIds);

    Page<TransitInfo> findPageDate(Pageable pageable);
}
