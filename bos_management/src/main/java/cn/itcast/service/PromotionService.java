package cn.itcast.service;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import javax.ws.rs.*;

/**
 * @author 刘相磊
 * @version 1.0, 2017-11-05 20:35
 */
public interface PromotionService {

    void save(Promotion model);

    Page<Promotion> findByPage(Pageable pageable);

    // 根据page和rows返回数据
    @GET
    @Path("/pageQuery")
    @Produces({"application/xml","application/json"})
    PageBean<Promotion> findPageDate(@QueryParam("page")int page, @QueryParam("rows")int rows);

    // 根据ID查询
    @Path("/promotion/{id}")
    @GET
    @Produces({"application/json","application/xml"})
    Promotion FindById(@PathParam("id")Integer id);

}
