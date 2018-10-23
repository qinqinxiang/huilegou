package cn.itcast.crm.service.impl;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    //注入dao
    @Autowired
    private CustomerRepository customerRepository;

    //查找未关联定区客户
    @Override
    public List<Customer> findnoassociationcustomers() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    //查找已关联定区客户
    @Override
    public List<Customer> findassociationcustomers(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    //将客户绑定到定区
    @Override
    public void associationcustomersCustomerToFixedArea(String customerId, String fixedAreaId) {
        //解除关联信息
        customerRepository.clearFixedArea(fixedAreaId);
        if (customerId == null || "null".equals(customerId)) {
            return;
        }
        //切割客户id
        String[] customerIdArray = customerId.split(",");
        for (String ids : customerIdArray) {
            Integer id = Integer.parseInt(ids);
            Customer customer = customerRepository.findOne(id);
            customer.setFixedAreaId(fixedAreaId);
        }

    }

    //用户注册保存用户
    @Override
    public void customer(Customer customer) {
        customerRepository.save(customer);
    }

    //查询用户是否激活
    @Override
    public Customer findByTelephone(String telephone) {
        return customerRepository.findByTelephone(telephone);
    }

    //激活用户
    @Override
    public void updateType(String telephone) {
        customerRepository.updateType(telephone);
    }

    //用户注册
    @Override
    public Customer findByTelephoneAndPassword(String telephone, String password) {
        return customerRepository.findByTelephoneAndPassword(telephone, password);
    }

    //根据用户地址查询定区地址id
    @Override
    public String findFixedAreaId(String address) {
        return customerRepository.findAddressByFiexdAreaId(address);
    }


}
