package cn.itcast.dao;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-24 21:03
 */
@Repository
public interface StandardDao extends JpaRepository<Standard,Integer>{

    List<Standard> findByName(String s);

}
