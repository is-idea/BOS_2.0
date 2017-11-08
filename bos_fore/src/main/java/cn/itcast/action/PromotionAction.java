package cn.itcast.action;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 前端页面的活动分页展示
 *
 * @author 刘相磊
 * @version 1.0, 2017-11-07 14:15
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion>{

    /**
     * 向首页展示活动
     *
     * @return
     */
    @Action(value = "promotion_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){

        // 基于WebService获取bos_management的活动列表数据信息
        PageBean<Promotion> pageDate = WebClient
                .create("http://localhost:9001/bos_management/services/promotionService/pageQuery?page=" + page + "&rows=" + rows)
                .accept(MediaType.APPLICATION_JSON)
                .get(PageBean.class);

        ActionContext.getContext().getValueStack().push(pageDate);
        return SUCCESS;
    }

    /**
     * 展示活动详情的
     *
     * @return
     */
    @Action(value = "promotion_showDetail")
    public String showDetail() throws IOException, TemplateException {

        // 先判断id对应的html存不存在,如果存在直接返回
        String htmlPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
        File file = new File(htmlPath+"/"+model.getId()+".html");

        // 如果不存在,查询数据库结合freemarker模板生成页面返回
        if(!file.exists()){
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
            // 获取模板对象
            Template template = configuration.getTemplate("promotion_detail.ftl");
            // 动态数据
            System.out.println(model.getId());
            Promotion promotion = WebClient
                    .create(Constants.BOS_MANAGEMENT_URL+"/services/promotionService/promotion/"+model.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Promotion.class);
            Map<String ,Object> map = new HashMap<>();
            map.put("promotion",promotion);
            // 合并输出
            template.process(map,new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        }
        // 存在 直接将文件返回
        ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
        FileUtils.copyFile(file,ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }

}
