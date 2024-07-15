package com.morning.order.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
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
                if (repository.existsById(ordId))
                        repository.deleteByOrdId(ordId);
//                    repository.deleteById(ord_id);
        }

        public OrderVO getOneOrder(Integer ordId) {
                Optional<OrderVO> optional = repository.findById(ordId);
//                return optional.get();
                return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
        }

        public List<OrderVO> getAll() {
                return repository.findAll();
        }

        public List<OrderVO> getAll(Map<String, String[]> map) {
                return HibernateUtil_CompositeQuery_Order.getAllC(map,sessionFactory.openSession());
        }

}