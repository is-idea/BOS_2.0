package cn.itcast.action;

import cn.itcast.crm.domain.Customer;
import cn.itcast.utils.BaseAction;
import cn.itcast.utils.IndustrySMS;
import cn.itcast.utils.MailUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘相磊
 * @version 1.0, 2017-11-02 22:00
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
@Actions
public class CustomerAction extends BaseAction<Customer>{

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    /**
     * 发送验证码的Action
     *
     * @return
     */
    @Action(value = "Customer_SendSms")
    public String Customer_SendSms(){

        // 随机生成的六位验证码
        String random = RandomStringUtils.randomNumeric(6);
        // 将验证码存入Session中
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(),random);
        // 编辑短信内容并发送
        String execute = IndustrySMS.execute(model.getTelephone(), "【itcast】您的验证码为" + random + "，请于1分钟内正确输入，如非本人操作，请忽略此短信。");
        // 将状态码存入session中
        Map map = new HashMap();
        map = JSONObject.parseObject(execute);
        String code = (String) map.get("respCode");
        System.out.println("验证码为:"+random);
        ServletActionContext.getRequest().getSession().setAttribute("code",code);
        return NONE;
    }

    // 异步校验的用户输入的验证码
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 异步校验的Action
     *
     * @return
     */
    @Action(value = "async")
    public String async(){

        // 从Session中取出随机生成的六位验证码
        String random = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
        System.out.println("随机生成的验证码为:"+random);
        System.out.println(model.getTelephone());
        System.out.println("用户输入的验证码为:"+ this.code);
        if(this.code != null && random.equals(this.code)){
            //String msg = "00000";
        }else{
            throw new RuntimeException();
        }
        return NONE;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户注册的成功后发送邮件的Action
     *
     * @return
     */
    @Action(value = "customer_regist", results = {
            @Result(name = "success", type = "redirect", location = "signup-success.html"),
            @Result(name = "error", type = "redirect", location = "signup.html") })
    public String customer_regist(){

        // 从Session中取出短信验证的状态码
        String msg = (String) ServletActionContext.getRequest().getSession().getAttribute("code");

        if(null != msg && "00000".equals(msg)){
            WebClient
                    .create("http://localhost:9002/crm_management/services/customerService/saveCustomer")
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .post(model);
            System.out.println("注册成功");
            // 随机生成的三十二位激活码
            String random = RandomStringUtils.randomNumeric(32);

            // 将激活码保存到redis,保存24小时
            redisTemplate.opsForValue().set(model.getTelephone(), random, 24, TimeUnit.HOURS);

            // 调用MailUtils发送邮件
            String mailText = "请与24小时之内点击下方链接进行账号绑定<br/>" +
                    "<a href='" + MailUtils.activeUrl + "?telephone=" + model.getTelephone() + "&activecode=" + random + "'>速运快递绑定地址</a>";

            // 发送
            MailUtils.sendMail("速运快递激活邮件",mailText,model.getEmail());
            return SUCCESS;
        }else{
            return ERROR;
        }
    }

    /** 邮箱激活码的属性驱动 */
    private String activecode;

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }

    /**
     * 客户注册后,邮箱绑定激活功能
     *
     * @return
     */
    @Action(value = "customer_activeMail")
    public String activeMail() throws IOException {
        // 获取用户手机号
        String telephone = model.getTelephone();
        // 获取用户点击邮箱激活码
        String emailCode = activecode;
        // 获取存在redis中的激活码
        String redisTelephone = redisTemplate.opsForValue().get(model.getTelephone());
        // 判断激活码是否过期

        if(null != redisTelephone && !("".equals(redisTelephone))){
            // 如果激活码有效,判断是否重复激活
            Customer customer = WebClient
                    .create("http://localhost:9002/crm_management/services/customerService/findCustomerType/" + telephone)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Customer.class);
            System.out.println(customer.getType());
            if(customer.getType() == null || 1 != customer.getType()){
                // 没有激活,则激活
                WebClient
                        .create("http://localhost:9002/crm_management/services/customerService/updateCustomerType/"+telephone)
                        .get();

                    ServletActionContext.getResponse().getWriter().print("绑定成功");
                }
            }else {
                // 已经激活,告知用户
                ServletActionContext.getResponse().getWriter().print("邮箱已经绑定,请勿重复绑定");
            }
        return NONE;
    }

}
