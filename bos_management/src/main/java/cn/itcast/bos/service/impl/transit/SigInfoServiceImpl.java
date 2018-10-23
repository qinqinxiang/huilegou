package cn.itcast.bos.service.impl.transit;

import cn.itcast.bos.dao.transit.SigInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillDaoRepository;
import cn.itcast.bos.service.transit.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SigInfoServiceImpl implements SignInfoService {
    @Autowired
    private SigInfoRepository sigInfoRepository;
    @Autowired
    private TransitInfoRepository transitInfoRepository;
    @Autowired
    private WayBillDaoRepository wayBillDaoRepository;

    @Override
    public void save(String signInfoIds, SignInfo signInfo) {
        sigInfoRepository.save(signInfo);
        //运输信息关联签收信息
        if (signInfoIds != null) {
            TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(signInfoIds));
            transitInfo.setSignInfo(signInfo);
            if (signInfo.getSignType().equals("正常")) {
                //设置运输信息
                transitInfo.setStatus("正常");
                //设置运单信息
                transitInfo.getWayBill().setSignStatus(3);
                //更新索引库
                wayBillDaoRepository.save(transitInfo.getWayBill());
            } else {
                signInfo.getSignType().equals("异常");
                //设置运输信息
                transitInfo.setStatus("异常");
                //设置运单信息
                transitInfo.getWayBill().setSignStatus(4);
                //更新索引库
                wayBillDaoRepository.save(transitInfo.getWayBill());
            }
        }
    }
}
