package cn.itcast.action;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.service.SubAreaService;
import cn.itcast.utils.BaseAction;
import org.apache.commons.lang.StringUtils;
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
 * 管理分区的Action
 *
 * @author 刘相磊
 * @version 1.0, 2017-10-28 18:24
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class SubAreaAction extends BaseAction<SubArea> {

    /** 通过类型自动注入业务层 */
    @Autowired
    private SubAreaService subAreaService;

    /**
     * 添加分区信息
     *
     * @return
     */
    @Action(value = "AddSubArea",results = {@Result(name = "success",location = "pages/base/sub_area.html",type = "redirect")})
    public String addSubArea(){
        subAreaService.save(model);
        return SUCCESS;
    }

    /**
     * 复杂条件分页查询
     *
     * @return
     */
    @Action(value = "SearchSubArea",results = {@Result(name = "success",type = "json")})
    public String searchArea(){
        Pageable pageable = new PageRequest(page-1,rows);

        Specification<SubArea> subAreaSpecification = new Specification<SubArea>() {

            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if(StringUtils.isNotBlank(model.getKeyWords())){
                    Predicate keyWords = cb.like(root.get("keyWords").as(String.class), "%"+model.getKeyWords()+"%");
                    list.add(keyWords);
                }

                Area area = model.getArea();
                if(area!=null){
                    Join join = root.join("area", JoinType.INNER);

                    if(StringUtils.isNotBlank(area.getProvince())){
                        Predicate province = cb.like(join.get("province"), "%" + area.getProvince() + "%");
                        list.add(province);
                    }

                    if(StringUtils.isNotBlank(area.getCity())){
                        Predicate city = cb.like(join.get("city"), "%" + area.getCity() + "%");
                        list.add(city);
                    }

                    if(StringUtils.isNotBlank("district")){
                        Predicate district = cb.like(join.get("district"), "%" + area.getDistrict() + "%");
                        list.add(district);
                    }

                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 3.调用service查询
        Page<SubArea> page = subAreaService.findPageDate(subAreaSpecification,pageable);
        pushPageDataToValueStack(page);
        return SUCCESS;
    }

}
