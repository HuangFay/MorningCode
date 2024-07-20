package com.morning.ordd.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morning.meal.model.MealService;
import com.morning.meal.model.MealVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Orddetails;

@Service("orddService")
public class OrddService {

    @Autowired
    OrddRepository repository;

    @Autowired
    private MealService mealService;
    
    @Autowired
    private SessionFactory sessionFactory;

    public void addOrdd(OrddVO orddVO) {
        repository.save(orddVO);
    }

    public void updateOrdd(OrddVO orddVO) {
        repository.save(orddVO);
    }

    public void deleteOrdd(Integer orddId) {
        if (repository.existsById(orddId)) {
            repository.deleteByOrddId(orddId);
        }
    }

    public OrddVO getOneOrdd(Integer orddId) {
        Optional<OrddVO> optional = repository.findById(orddId);
        return optional.orElse(null);
    }

    public List<OrddVO> getAll() {
        return repository.findAll();
    }

    public List<OrddVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_Orddetails.getAllC(map, sessionFactory.openSession());
    }

    public List<OrddVO> findByOrdId(Integer ordId) {
        return repository.findByOrdId(ordId);
    }

  //拿來找最新的訂單分數
    public OrddVO getLatestOrdd() {
        return repository.findTopByOrderByOrddIdDesc();
    }


    // 新增的方法，查詢訂單明細及其對應的客製化選項
    public List<OrddVO> getOrderDetailsWithCustomizations(int orderId) {
        List<OrddVO> orderDetails = findByOrdId(orderId);
        for (OrddVO detail : orderDetails) {
            List<MealVO> customizations = mealService.findByOrderDetailId(detail.getOrddId());
            detail.setMealCustomizationDetailsVOList(customizations);
        }
        return orderDetails;

    }

	public OrddVO getOrderDetail(Integer orddId) {
		// TODO Auto-generated method stub
		return null;
	}
}
