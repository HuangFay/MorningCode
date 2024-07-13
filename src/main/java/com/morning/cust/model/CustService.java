package com.morning.cust.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Customization_options;


@Service("custService")
public class CustService {

        @Autowired
        CustRepository repository;
        
        @Autowired
        private SessionFactory sessionFactory;

        public void addCust(CustVO custVO) {
                repository.save(custVO);
        }

        public void updateCust(CustVO custVO) {
                repository.save(custVO);
        }

        public void deleteCust(Integer custId) {
                if (repository.existsById(custId))
                        repository.deleteByCustId(custId);
//                    repository.deleteById(custId);
        }

        public CustVO getOneCust(Integer custId) {
                Optional<CustVO> optional = repository.findById(custId);
//                return optional.get();
                return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
        }

        public List<CustVO> getAll() {
                return repository.findAll();
        }

        public List<CustVO> getAll(Map<String, String[]> map) {
                return HibernateUtil_CompositeQuery_Customization_options.getAllC(map,sessionFactory.openSession());
        }

}