package cn.itcast.service.impl;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.service.StandardService;
import cn.itcast.dao.StandardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 收派标准业务层实现类
 *
 * @author 刘相磊
 * @version 1.0, 2017-10-24 21:02
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService {

    /** 根据类型自动注入Dao */
    @Autowired
    private StandardDao standardDao;
    
    @Override
    public void StandardSave(Standard standard) {
        standardDao.save(standard);
    }

    @Override
    public List<Standard> findByName(String s) {
        return standardDao.findByName(s);
    }

    @Override
    public Page<Standard> findPageDate(Pageable pageable) {
        return standardDao.findAll(pageable);
    }

    @Override
    public List<Standard> findAll() {
        return standardDao.findAll();
    }

}
