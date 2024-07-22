package com.morning.order.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morning.ordd.model.OrddRepository;
import com.morning.ordd.model.OrddVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Order;

@Service("orderService")
public class OrderService {

    @Autowired
    OrderRepository repository;

    @Autowired
    OrddRepository orddRepository;

    @Autowired
    private SessionFactory sessionFactory;

    public void addOrder(OrderVO orderVO) {
        repository.save(orderVO);
    }

    public void updateOrder(OrderVO orderVO) {
        repository.save(orderVO);
    }

    public void deleteOrder(Integer ordId) {
        if (repository.existsById(ordId)) {
            repository.deleteByOrdId(ordId);
        }
    }

    public OrderVO getOneOrder(Integer ordId) {
        Optional<OrderVO> optional = repository.findById(ordId);
        return optional.orElse(null);
    }

    public List<OrderVO> getAll() {
        return repository.findAll();
    }
  
    //複合查詢
    public List<OrderVO> getAll(Map<String, String[]> map) {
        Session session = sessionFactory.openSession();
        return HibernateUtil_CompositeQuery_Order.getAllC(map, session);
    }

    public List<OrderVO> getOrdersByMemNo(Integer memNo) {
        return repository.findByMemNo(memNo);
    }

    public List<OrderVO> getOrderHistory() {
        return repository.findAll();
    }

    public OrderVO getOrderDetail(Integer ordId) {
        OrderVO order = repository.findById(ordId).orElse(null);
        if (order != null) {
            List<OrddVO> orderDetails = orddRepository.findByOrdId(ordId);
            order.setOrderDetails(orderDetails);
        }
        return order;
    }

    public boolean reorder(Integer ordId) {
        return true;
    }
}
