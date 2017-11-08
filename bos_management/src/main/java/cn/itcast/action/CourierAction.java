package cn.itcast.action;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.service.CourierService;
import cn.itcast.utils.BaseAction;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 快递员的Action
 *
 * @author 刘相磊
 * @version 1.0, 2017-10-27 13:50
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends BaseAction<Courier> {

    /** 通过类型自动注入 */
    @Autowired
    private CourierService courierService;

    /**
     * 保存快递员信息
     *
     * @return 重定向返回到/pages/base/courier.html
     */
    @Action(value = "CourierSave",results = {@Result(name = "success",location = "/pages/base/courier.html",type = "redirect")})
    public String courierSave(){
        courierService.saveCourier(model);
        return SUCCESS;
    }

    /**
     * 复杂条件分页查询快递员信息
     *
     * @return 将数据转为json压栈返回
     */
    @Action(value = "CourierPage",results = {@Result(name = "success",type = "json")})
    public String findPage(){
        // 调用业务层,查询数据结果
        Pageable pageable = new PageRequest(page-1,rows);

        Specification<Courier> specification = new Specification<Courier>() {
            List<Predicate> list = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isNotBlank(model.getCourierNum())){
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCompany())){
                    Predicate p2 = cb.equal(root.get("company").as(String.class), "%"+model.getCompany()+"%");
                    list.add(p2);
                }
                if(StringUtils.isNotBlank(model.getType())){
                    Predicate p3 = cb.equal(root.get("type").as(String.class), model.getType());
                    list.add(p3);
                }
                Join<Object, Object> standard = root.join("standard", JoinType.INNER);
                if (model.getStandard()!=null && StringUtils.isNotBlank(model.getStandard().getName())){
                    Predicate p4 = cb.equal(standard.get("name").as(String.class), "%" + model.getStandard().getName() + "%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Courier> page = courierService.findPageDate(specification,pageable);
        // 返回客户端需要totle和rows
        // 压入值栈
        pushPageDataToValueStack(page);
        return SUCCESS;
    }

    /**
     * 作废快递员
     *
     * @return
     */
    @Action(value = "EditCourier",results = {@Result(name = "success",location = "/pages/base/courier.html",type = "redirect")})
    public String editCourier(){
        String[] ids = ServletActionContext.getRequest().getParameter("ids").split(",");
        String deltag = ServletActionContext.getRequest().getParameter("deltag");

        courierService.editDeltag(ids);
        return SUCCESS;
    }

    /**
     * 还原快递员
     *
     * @return
     */
    @Action(value = "EditCourierRestore",results = {@Result(name = "success",location = "/pages/base/courier.html",type = "redirect")})
    public String editCourierRestore(){
        String[] ids = ServletActionContext.getRequest().getParameter("ids").split(",");
        String deltag = ServletActionContext.getRequest().getParameter("deltag");
        courierService.courierRestore(ids,deltag);
        return SUCCESS;
    }

}
