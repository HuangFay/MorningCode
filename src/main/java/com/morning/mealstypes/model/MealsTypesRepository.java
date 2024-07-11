package com.morning.mealstypes.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MealsTypesRepository extends JpaRepository<MealsTypesVO, Integer> {


}
