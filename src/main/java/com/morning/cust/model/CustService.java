package com.morning.cust.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("custService")
public class CustService {

    @Autowired
    CustRepository repository;

    public void addCust(CustVO custVO) {
        repository.save(custVO);
    }

    public void updateCust(CustVO custVO) {
        repository.save(custVO);
    }

    public void deleteCust(Integer custId) {
        if (repository.existsById(custId))
            repository.deleteByCustId(custId);
    }

    public CustVO getOneCust(Integer custId) {
        return repository.findById(custId).orElse(null);
    }

    public List<CustVO> getAll() {
        return repository.findAll();
    }

    public List<CustVO> getAllActiveOptions() {
        return repository.findByCustStatus((byte) 1);
    }
}
