package cn.itcast.bos.index;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WayBillDaoRepository extends ElasticsearchRepository<WayBill, Integer> {
}
