package cn.itcast.crm.service.impl;

import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.dao.CustomerDao;
import cn.itcast.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author 刘相磊
 * @version 1.0, 2017-10-30 20:16
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<Customer> findNoAssociationCustomer() {
        return customerDao.findByFixedAreaIdIsNull();
    }

    @Override
    public List<Customer> findHasAssociationCustomer(String fixedAreaId) {
        return customerDao.findByFixedAreaId(fixedAreaId);
    }

    @Override
    public void associationCustomerToFixedArea(String customerId, String fixedAreaId) {

    }

    @Override
    public void saveCustomer(Customer customer) {
        customerDao.save(customer);
    }

    @Override
    public Customer findCustomerType(String telephone) {
        return customerDao.findByTelephone(telephone);
    }

    @Override
    public void updateCustomerType(String telephone) {
        customerDao.updateType(telephone);
    }

}
