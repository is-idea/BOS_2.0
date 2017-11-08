package cn.itcast.service;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 收派标准业务层
 *
 * @author 刘相磊
 * @version 1.0, 2017-10-24 21:01
 */
public interface StandardService {

    /**
     * 保存收派标准信息
     *
     * @param standard 收派标准对象
     */
    public void StandardSave(Standard standard);

    /**
     * 根据Standard的name字段查询数据
     *
     * @param s  查询条件
     * @return 查询到的Standard集合
     */
    List<Standard> findByName(String s);

    /**
     * 无条件分页查询
     *
     * @param pageable 封装了当前页(page)和总记录数(rows)
     * @return 将查到的Standard数据封装到Page对象中返回
     */
    Page<Standard> findPageDate(Pageable pageable);

    /**
     * 查询Standard全部数据
     *
     * @return
     */
    List<Standard> findAll();
}
