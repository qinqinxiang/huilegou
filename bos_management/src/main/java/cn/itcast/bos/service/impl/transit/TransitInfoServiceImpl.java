package cn.itcast.bos.service.impl.transit;

import cn.itcast.bos.dao.take_deliver.WayBillRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillDaoRepository;
import cn.itcast.bos.service.transit.TransitInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {
    //注入wayBillRepository
    @Autowired
    private WayBillDaoRepository wayBillDaoRepository;
    @Autowired
    private TransitInfoRepository transitInfoRepository;
    @Autowired
    private WayBillRepository wayBillRepository;

    //开启中转
    @Override
    public void createTransit(String wayBillIds) {
        if (StringUtils.isNotBlank(wayBillIds)) {
            String[] wayBillStr = wayBillIds.split(",");
            for (String wayBillNum : wayBillStr) {
                //调用dao查询数据是否存在
                WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillNum));
                if (wayBill != null && wayBill.getSignStatus() == 1) {
                    //创建TransitInfo
                    //生成配送信息
                    TransitInfo transitInfo = new TransitInfo();
                    transitInfo.setWayBill(wayBill);
                    transitInfo.setStatus("出入库中转");
                    //保存配送信息
                    transitInfoRepository.save(transitInfo);
                    //设置订单状态
                    wayBill.setSignStatus(2);//派送中
                    //将数据同步更新到索引库
                    wayBillDaoRepository.save(wayBill);
                }
            }
        }
    }

    //物流信息分页查询
    @Override
    public Page<TransitInfo> findPageDate(Pageable pageable) {
        return transitInfoRepository.findAll(pageable);
    }
}
