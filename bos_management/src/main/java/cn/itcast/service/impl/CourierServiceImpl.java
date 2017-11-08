package cn.itcast.service.impl;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.dao.CourierDao;
import cn.itcast.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-25 20:36
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierDao courierDao;

    @Override
    public void saveCourier(Courier courier) {
        courier.setDeltag('0');
        courierDao.save(courier);

    }

    @Override
    public Page<Courier> findPageDate(Specification<Courier> specification, Pageable pageable) {
        return courierDao.findAll(specification,pageable);
    }

    @Override
    public void editDeltag(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            courierDao.editDeltag(Integer.parseInt(ids[i]));
        }
    }

    @Override
    public void courierRestore(String[] ids, String deltag) {
        char c = deltag.charAt(0);
        for (int i = 0; i < ids.length; i++) {
            courierDao.editDeltag(Integer.parseInt(ids[i]),c);
        }
    }

}
