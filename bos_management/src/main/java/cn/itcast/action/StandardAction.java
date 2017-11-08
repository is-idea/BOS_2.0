package cn.itcast.action;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.service.StandardService;
import cn.itcast.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import java.util.List;

/**
 * 收派标准的Action
 *
 * @author 刘相磊
 * @version 1.0, 2017-10-27 13:35
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends BaseAction<Standard> {

    /** 通过类型自动注入 */
    @Autowired
    private StandardService standardService;

    /**
     * 添加收派标准
     * @return 重定向返回到/pages/base/standard.html
     */
    @Action(value = "StandardSave",results = {@Result(name = "success",location = "/pages/base/standard.html",type = "redirect")})
    public String standardSave(){
        standardService.StandardSave(model);
        return SUCCESS;
    }

    /**
     * 查询所有收派标准信息
     *
     * @return 将查询到的数据通过json压栈返回
     */
    @Action(value = "findAll",results = {@Result(name = "success",type = "json")})
    public String findAll(){
        List<Standard> list = standardService.findAll();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

    /**
     * 无条件分页查询收派标准信息
     *
     * @return 将查询到的数据转为json压栈返回
     */
    @Action(value = "StandardPage",results = {@Result(name = "success",type = "json")})
    public String findPage(){

        // 调用业务层,查询数据结果
        Pageable pageable = new PageRequest(page-1,rows);
        Page<Standard> page = standardService.findPageDate(pageable);

        // 返回客户端需要totle和rows
        // 压入值栈
        pushPageDataToValueStack(page);
        return SUCCESS;
    }

}
