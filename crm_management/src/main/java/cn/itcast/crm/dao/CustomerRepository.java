package cn.itcast.crm.dao;

import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    //查询未关联定区客户
    public List<Customer> findByFixedAreaIdIsNull();

    //查询已关联定区客户
    public List<Customer> findByFixedAreaId(String fixedAreaId);

    //将客户关联定区
    @Query("update Customer set fixedAreaId=? where id=?")
    @Modifying
    public void associationCustomertoFixedArea(String fixedAreaId, Integer id);

    //将已关联定区客户解除绑定关系
    @Query("update Customer set fixedAreaId=null where fixedAreaId=?")
    @Modifying
    public void clearFixedArea(String fixedAreaId);

    //查询用户是否激活
    Customer findByTelephone(String telephone);

    //激活用户
    @Query("update Customer set type=1 where telephone=?")
    @Modifying
    public void updateType(String telephone);

    //根据用户电话和密码查询用户
    Customer findByTelephoneAndPassword(String telephone, String password);

    //用户地址查询定区id
    @Query("select fixedAreaId from Customer where address=?")
    String findAddressByFiexdAreaId(String address);
}
