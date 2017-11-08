package cn.itcast.service;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-28 18:29
 */
public interface SubAreaService {

    void save(SubArea model);

    Page<SubArea> findPageDate(Specification<SubArea> subAreaSpecification, Pageable pageable);
}
