package com.morning.ordd.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Orddetails;


@Service("orddService")
public class OrddService {

        @Autowired
        OrddRepository repository;
        
        @Autowired
        private SessionFactory sessionFactory;

        public void addOrdd(OrddVO orddVO) {
                repository.save(orddVO);
        }

        public void updateOrdd(OrddVO orddVO) {
                repository.save(orddVO);
        }

        public void deleteOrdd(Integer orddId) {
                if (repository.existsById(orddId))
                        repository.deleteByOrddId(orddId);
//                    repository.deleteById(orddId);
        }

        public OrddVO getOneOrdd(Integer orddId) {
                Optional<OrddVO> optional = repository.findById(orddId);
//                return optional.get();
                return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
        }

        public List<OrddVO> getAll() {
                return repository.findAll();
        }

        public List<OrddVO> getAll(Map<String, String[]> map) {
                return HibernateUtil_CompositeQuery_Orddetails.getAllC(map,sessionFactory.openSession());
        }

}