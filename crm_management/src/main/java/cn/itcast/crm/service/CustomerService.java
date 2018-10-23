package cn.itcast.crm.service;

import cn.itcast.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * 客户操作
 *
 * @author itcast
 */
public interface CustomerService {

    /*	// 查询所有未关联客户列表
        @Path("/noassociationcustomers")
        @GET
        @Produces({ "application/xml", "application/json" })
        public List<Customer> findNoAssociationCustomers();

        // 已经关联到指定定区的客户列表
        @Path("/associationfixedareacustomers/{fixedareaid}")
        @GET
        @Produces({ "application/xml", "application/json" })
        public List<Customer> findHasAssociationFixedAreaCustomers(
                @PathParam("fixedareaid") String fixedAreaId);

        // 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3
        @Path("/associationcustomerstofixedarea")
        @PUT
        public void associationCustomersToFixedArea(
                @QueryParam("customerIdStr") String customerIdStr,
                @QueryParam("fixedAreaId") String fixedAreaId);*/
    //查询没有关联定区用户
    @Path("/noassociationcustomers")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findnoassociationcustomers();

    //查询已关联定区客户
    @Path("/associationcustomers/{fixedAreaId}")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findassociationcustomers(
            @PathParam("fixedAreaId") String fixedAreaId
    );

    //将客户关联到定区
    @Path("/associationcustomerstofixedArea")
    @PUT
    public void associationcustomersCustomerToFixedArea(
            @QueryParam("customerId") String customerId,
            @QueryParam("fixedAreaId") String fixedAreaId
    );

    //用户注册保存用户
    @Path("/customer")
    @POST
    @Consumes({"application/xml", "application/json"})
    public void customer(Customer customer);

    //判断用户是否激活
    @Path("/noactivecustomer/{telephone}")
    @GET
    @Produces({"application/xml", "application/json"})
    public Customer findByTelephone(
            @PathParam("telephone") String telephone
    );

    //激活用户
    @Path("/updatecustomertype/{telephone}")
    @GET
    @Consumes({"application/xml", "application/json"})
    public void updateType(
            @PathParam("telephone") String telephone
    );

    //用户注册
    @Path("/customerLogin")
    @GET
    @Produces({"application/xml,application/json"})
    Customer findByTelephoneAndPassword(
            @QueryParam("telephone") String telephone, @QueryParam("password") String password);

    //根据用地址询定区编码
    @Path("/findFixedAreaId")
    @GET
    @Consumes({"application/xml,application/json"})
    public String findFixedAreaId(@QueryParam("address") String address);
}
