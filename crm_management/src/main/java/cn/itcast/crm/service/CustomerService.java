package cn.itcast.crm.service;

import cn.itcast.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-30 19:59
 */
public interface CustomerService {

    // 查询所有未关联客户列表
    @GET
    @Path(value = "/CustomerService")
    @Produces({"application/xml","application/json"})
    public List<Customer> findNoAssociationCustomer();

    // 已经关联到指定定区的客户列表
    @GET
    @Path(value = "/CustomerService/{finxedareaid}")
    @Produces({"application/xml","application/json"})
    public List<Customer> findHasAssociationCustomer(@PathParam("finxedareaid") String finxedareaid);

    //将客户关联到定区上,将所有的客户ID拼成字符串1,2,3
    @Path(value = "/associationcustomerstofixedarea")
    @PUT
    public void associationCustomerToFixedArea(@QueryParam("customerId") String customerId, @QueryParam("fixedAreaId") String fixedAreaId);

    // 保存用户
    @POST
    @Path(value = "/saveCustomer")
    @Consumes({"application/xml","application/json"})
    public void saveCustomer(Customer customer);

    // 查询用户是否已经激活
    @GET
    @Path(value = "/findCustomerType/{telephone}")
    @Produces({"application/xml","application/json"})
    public Customer findCustomerType(@PathParam("telephone") String telephone);

    // 绑定用户邮箱
    @GET
    @Path(value = "/updateCustomerType/{telephone}")
    @Consumes({"application/xml","application/json"})
    public void updateCustomerType(@PathParam("telephone") String telephone);

    // 用户登录
    @GET
    @Path("/customer/login/{telephone}/{password}")
    @Consumes({"application/json","application/xml"})
    @Produces({"application/json","application/xml"})
    public Customer login(@PathParam("telephone")String telephone,@PathParam("password")String password);

}
