package cn.itcast.crm.dao;


import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-30 20:17
 */

public interface CustomerDao extends JpaRepository<Customer,Integer>{

    public List<Customer> findByFixedAreaIdIsNull();

    public List<Customer> findByFixedAreaId(String fixedAreaId);

    public Customer findByTelephone(String telephone);

    @Query("update Customer set type=1 where telephone= ?")
    @Modifying
    public void updateType(String telephone);

    Customer findByTelephoneAndPassword(String telephone, String password);
}
