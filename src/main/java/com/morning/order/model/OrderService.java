package com.morning.order.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Order;

@Service("orderService")
public class OrderService {

    @Autowired
    OrderRepository repository;

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

    public List<OrderVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_Order.getAllC(map, sessionFactory.openSession());
    }

    public List<OrderVO> getOrdersByMemNo(Integer memNo) {
        return repository.findByMemNo(memNo);
    }
}
