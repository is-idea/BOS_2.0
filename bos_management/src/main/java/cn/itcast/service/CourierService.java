package cn.itcast.service;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * 快递员业务层
 *
 * @author 刘相磊
 * @version 1.0, 2017-10-25 20:36
 */
public interface CourierService {

    /**
     * 保存快递员信息
     *
     * @param courier 快递员对象
     */
    void saveCourier(Courier courier);

    /**
     * 复杂条件分页查询
     *
     * @param specification
     * @param pageable
     * @return
     */
    Page<Courier> findPageDate(Specification<Courier> specification, Pageable pageable);

    /**
     * 通过ID作废快递员
     *
     * @param ids 快递员ID
     */
    void editDeltag(String[] ids);

    /**
     * 通过ID还原快递员
     *
     * @param ids 快递员ID
     * @param deltag 还原标记
     */
    void courierRestore(String[] ids, String deltag);

}

