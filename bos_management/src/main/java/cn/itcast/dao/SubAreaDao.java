package cn.itcast.dao;

import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-28 18:32
 */
@Repository
public interface SubAreaDao extends JpaRepository<SubArea,String>,JpaSpecificationExecutor<SubArea>{

}
