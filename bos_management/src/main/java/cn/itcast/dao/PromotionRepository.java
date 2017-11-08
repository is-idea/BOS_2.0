package cn.itcast.dao;

import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 刘相磊
 * @version 1.0, 2017-11-05 20:36
 */
@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Integer>{

}
