package cn.itcast.bos.service.impl.transit;

import cn.itcast.bos.dao.transit.InOutStorageInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.InOutStorageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {
    @Autowired
    private InOutStorageInfoRepository inOutStorageInfoRepository;
    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Override
    public void save(String inOutStoreId, InOutStorageInfo inOutStorageInfo) {
        //保存物流信息
        inOutStorageInfoRepository.save(inOutStorageInfo);
        /*
         * 运输配送信息包含定单的出入库信息，当生成订单的出入库信息，运输配送信息关联出入库信息
         * */
        if (inOutStorageInfo != null) {
            TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(inOutStoreId));
            //运输配送信息关联出入库信息
            transitInfo.getInOutStorageInfos().add(inOutStorageInfo);
            //当出入库信息为到达网点的同时修改配送信息，同时跟新地址信息
            if (inOutStorageInfo.getOperation().equals("到达网点")) {
                transitInfo.setStatus("到达网点");
                //更新地址 信息
                transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
            }
        }

    }
}
