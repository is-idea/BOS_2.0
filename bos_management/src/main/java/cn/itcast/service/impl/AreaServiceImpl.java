package cn.itcast.service.impl;

import cn.itcast.dao.AreaDao;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-27 20:57
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    /** 通过类型自动注入 */
    @Autowired
    private AreaDao areaDao;

    @Override
    public void saveBatch(List<Area> areas) {
        areaDao.save(areas);
    }

    @Override
    public List<Area> findAll() {
        return areaDao.findAll();
    }

    @Override
    public Page<Area> findPageDate(Specification<Area> specification, Pageable pageable) {
        return areaDao.findAll(specification,pageable);
    }

    @Override
    public List<String> findCity(String province) {
        return areaDao.findCity(province);
    }

    @Override
    public List<String> findProvince() {
        return areaDao.findProvince();
    }

    @Override
    public List<String> findDistrict(String province, String city) {
        return areaDao.findDistrict(province,city);
    }

    @Override
    public Area findArea(Area model) {
        return areaDao.findByProvinceAndCityAndDistrict(model.getProvince(),model.getCity(),model.getDistrict());
    }

}
