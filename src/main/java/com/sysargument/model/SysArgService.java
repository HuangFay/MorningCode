package com.sysargument.model;

import com.reservationcontrol.model.ResCVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("sysArgService")
public class SysArgService {
    @Autowired
    SysArgRepository repository;

//    public SysArgumentVO getOneSysArg(String  sysArgument) {
//        Optional<SysArgumentVO> optional = repository.findById(sysArgument);
////		return optional.get();
//        return optional.orElse(null);
//    }
    public List<SysArgVO> findByColumns(String sysArgument) {
        return repository.findBysysArgument(sysArgument);
    }


}
