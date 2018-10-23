package cn.itcast.bos.service.impl.take_deliver;

import cn.itcast.bos.dao.take_deliver.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillDaoRepository;
import cn.itcast.bos.service.take_deliver.WayBillService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    //调用dao
    @Autowired
    private WayBillRepository wayBillRepository;
    //注入elasticsearch
    @Autowired
    private WayBillDaoRepository wayBillDaoRepository;

    //保存运单
    @Override
    public void save(WayBill wayBill) {
        //调用业务层查询运单是否存在
        WayBill oldWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if (oldWayBill == null) {
            //不存在
            wayBill.setSignStatus(1);
            wayBillRepository.save(wayBill);
            //保存到索引库
            wayBillDaoRepository.save(wayBill);
        } else {
            if (oldWayBill.getSignStatus() == 1) {
                Integer id = oldWayBill.getId();
                BeanUtils.copyProperties(wayBill, oldWayBill);
                oldWayBill.setId(id);
                oldWayBill.setSignStatus(1);
                //保存到索引库
                wayBillDaoRepository.save(wayBill);
            }
        }
    }

    //运单分页查询
    @Override
    public Page<WayBill> findAll(WayBill wayBill, Pageable pageable) {
        //判断表单中是否添加查询条件
        if (StringUtils.isBlank(wayBill.getWayBillNum())
                && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress())
                && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            return wayBillRepository.findAll(pageable);
        } else {
            //构造有条件查询
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();//布尔查询 多条件组合
            if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
                //运单号查询(等值查询)
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                boolQueryBuilder.must(termQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
                //在次构造布尔查询匹配词条
                BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                //情况一:发货地查询(模糊查询)
                WildcardQueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("sendAddress",
                        "*" + wayBill.getSendAddress() + "*");
                //情况二:词条匹配查询
                QueryStringQueryBuilder sendAddress = new QueryStringQueryBuilder(wayBill.getSendAddress()).
                        field("sendAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);
                //将上面两种条件构建
                boolQueryBuilder1.should(wildcardQueryBuilder);
                boolQueryBuilder1.should(sendAddress);
                //添加到最终构建条件
                boolQueryBuilder.must(boolQueryBuilder1);
            }
            if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                //收货地查询(模糊查询)
                //情况一:
                WildcardQueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("recAddress",
                        "*" + wayBill.getRecAddress() + "*");
                //情况二:
                QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress())
                        .field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);
                //上面两种情况只要有一种符合查询条件即可
                boolQueryBuilder1.should(wildcardQueryBuilder);
                boolQueryBuilder1.should(queryStringQueryBuilder);

                boolQueryBuilder.must(boolQueryBuilder1);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                //在次构造布尔查询匹配词条
                BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                //情况一:(模糊查询)
                WildcardQueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("sendProNum",
                        "*" + wayBill.getSendProNum() + "*");
                //情况二:词条匹配查询
                QueryStringQueryBuilder sendAddress = new QueryStringQueryBuilder(wayBill.getSendProNum()).
                        field("sendProNum").defaultOperator(QueryStringQueryBuilder.Operator.AND);
                //将上面两种条件构建
                boolQueryBuilder1.should(wildcardQueryBuilder);
                boolQueryBuilder1.should(sendAddress);
                //添加到最终构建条件
                boolQueryBuilder.must(boolQueryBuilder1);
            }

            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                //签收状态
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                boolQueryBuilder.must(termQueryBuilder);
            }

            NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
            //分页
            nativeSearchQuery.setPageable(pageable);
            //调用索引库查询
            return wayBillDaoRepository.search(nativeSearchQuery);
        }

    }

    //运单表格数据回显
    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }

    //更新索引库
    @Override
    public void sysIndex() {
        List<WayBill> wayBills = wayBillRepository.findAll();
        //更新新索引库
        wayBillDaoRepository.save(wayBills);
    }
}
