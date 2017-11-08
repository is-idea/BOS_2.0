package cn.itcast.service.impl;

import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.dao.SubAreaDao;
import cn.itcast.service.SubAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-28 18:30
 */
@Service
public class SubAreaServiceImpl implements SubAreaService {

    /** 通过类型自动注入Dao层 */
    @Autowired
    private SubAreaDao subAreaDao;

    @Override
    public void save(SubArea model) {
        subAreaDao.save(model);
    }

    @Override
    public Page<SubArea> findPageDate(Specification<SubArea> subAreaSpecification, Pageable pageable) {
        return subAreaDao.findAll(subAreaSpecification,pageable);
    }

}
