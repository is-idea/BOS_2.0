package cn.itcast.action;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.service.PromotionService;
import cn.itcast.utils.BaseAction;
import org.apache.commons.io.FileUtils;
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
import org.springframework.stereotype.Controller;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 宣传活动的保存Action
 *
 * @author 刘相磊
 * @version 1.0, 2017-11-05 20:31
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {

    private File titleImgFile;
    private String titleImgFileFileName;

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    @Autowired
    private PromotionService promotionService;

    @Action(value = "promotion_save",results = {@Result(name = "success",location = "/pages/take_delivery/promotion_add.html",type = "redirect")})
    public String save(){

        try {
            String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");

            String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

            // 随机生成文件名
            UUID uuid = UUID.randomUUID();

            String fileName = uuid + titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));

            // 保存图片
            FileUtils.copyFile(titleImgFile,new File(savePath + "/" +fileName));
            // 将保存路径 相对工程web访问路径 保存model中
            model.setTitleImg(ServletActionContext.getRequest().getContextPath()+"/upload/"+fileName);
            // 调用业务层保存数据
            promotionService.save(model);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    @Action(value = "promotion_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){

        Pageable pageable = new PageRequest(page -1,rows);

        Page<Promotion> page = promotionService.findByPage(pageable);

        pushPageDataToValueStack(page);

        return SUCCESS;
    }

}
