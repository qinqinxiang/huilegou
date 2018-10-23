package cn.itcast.bos.dao.take_deliver;

;
import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    //定时修改宣传活动过期
    @Query("update Promotion set status='2' where endDate<? and status='1'")
    @Modifying
    void updateType(Date date);
}
