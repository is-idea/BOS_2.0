package cn.itcast.dao;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-27 20:58
 */
@Repository
public interface AreaDao extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area> {

    @Query("select distinct t.city from Area t where t.province = ?")
    @Modifying
    List<String> findCity(String province);

    @Query("select distinct t.province from Area t")
    List<String> findProvince();

    @Query("select distinct t.district from Area t where t.province = ? and city = ?")
    List<String> findDistrict(String province, String city);

    Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}
