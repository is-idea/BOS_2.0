package cn.itcast.action;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.crm.domain.Customer;
import cn.itcast.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * 定区关联客户的Action
 *
 * @author 刘相磊
 * @version 1.0, 2017-11-01 11:49
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea> {

    /**
     * 查询已关联定区的列表
     *
     * @return
     */
    @Action(value = "fixedArea_findHasAssociationCustomers",results = {@Result(name = "success",type = "json")})
    public String fixedArea_findHasAssociationCustomers(){

        Collection<? extends Customer> collection = WebClient
                .create("http://localhost:9999/crm_management/services/customerService/CustomerService/"+model.getId())
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);

        ActionContext.getContext().getValueStack().push(collection);

        return SUCCESS;
    }

    /**
     * 查询所有 未关联定区客户列表
     *
     * @return
     */
    @Action(value = "fixedArea_findNoAssociationCustomers",results = {@Result(name = "success",type = "json")})
    public String fixedArea_findNoAssociationCustomers(){

        Collection<? extends Customer> collection = WebClient
                .create("http://localhost:9999/crm_management/services/customerService/CustomerService/")
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);

        ActionContext.getContext().getValueStack().push(collection);

        return SUCCESS;
    }

    // 提供属性驱动
    private String[] customersId;

    public void setCustomersId(String[] customersId) {
        this.customersId = customersId;
    }

    /**
     * 关联客户到定区
     *
     * @return
     */
    @Action(value = "associationCustomerToFixedArea",results = {@Result(name = "success",location = "./pages/base/fixed_area.html",type = "redirect")})
    public String associationCustomerToFixedArea(){

        String customersId = StringUtils.join("customersId", ",");
        WebClient
                .create("http://localhost:9999/crm_management/services/customerService/associationcustomerstofixedarea?customerIdStr="+customersId+"&fixedAreaId="+model.getId())
                .put(null);
        return SUCCESS;
    }

}
