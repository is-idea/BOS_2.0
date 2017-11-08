package cn.itcast.dao;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-25 20:37
 */
@Repository
public interface CourierDao extends JpaRepository<Courier,Integer>,JpaSpecificationExecutor<Courier> {

    @Query("update Courier set deltag = '0' where id = ?")
    @Modifying
    void editDeltag(Integer i);

    @Query("update Courier set deltag = ?2 where id = ?1")
    @Modifying
    void editDeltag(Integer i, char deltag);

}
