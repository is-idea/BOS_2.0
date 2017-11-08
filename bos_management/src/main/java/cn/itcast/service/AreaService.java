package cn.itcast.service;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List; /**
 * @author 刘相磊
 * @version 1.0, 2017-10-27 20:15
 */
public interface AreaService {

    /**
     * 批量保存Execl表格数据
     *
     * @param areas 装有封装好数据的Area集合
     */
    void saveBatch(List<Area> areas);

    /**
     * 查询全部省市区
     *
     * @return 装有全部省市区数据的List集合
     */
    List<Area> findAll();

    /***
     * 复杂条件分页查询
     *
     * @param specification
     * @param pageable
     */
    Page<Area> findPageDate(Specification<Area> specification, Pageable pageable);

    List<String> findCity(String id);

    /**
     * 查询省
     *
     * @return
     */
    List<String> findProvince();

    List<String> findDistrict(String province, String city);

    Area findArea(Area model);
}
