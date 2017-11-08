package cn.itcast.service.impl;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.dao.PromotionRepository;
import cn.itcast.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author 刘相磊
 * @version 1.0, 2017-11-05 20:35
 */
@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public void save(Promotion model) {
        promotionRepository.save(model);
    }

    @Override
    public Page<Promotion> findByPage(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    @Override
    public PageBean<Promotion> findPageDate(int page, int rows) {

        Pageable pageable = new PageRequest(page-1,rows);
        Page<Promotion> pageDate = promotionRepository.findAll(pageable);

        // 封装到Page对象里
        PageBean<Promotion> pageBean = new PageBean<>();
        pageBean.setTotalElements(pageDate.getTotalElements());
        pageBean.setContent(pageDate.getContent());
        return pageBean;
    }

    @Override
    public Promotion FindById(Integer id) {
        return promotionRepository.findOne(id);
    }
}
