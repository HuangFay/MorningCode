package com.morning.shop.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("shopService")
public class ShopService {

    @Autowired
    private ShopRepository repository;

    public void addItem(ShopVO shopVO) {
        repository.save(shopVO);
    }

    public void updateItem(ShopVO shopVO) {
        repository.save(shopVO);
    }

    public void deleteItem(Integer itemId) {
        if (repository.existsById(itemId))
            repository.deleteById(itemId);
    }

    public ShopVO getOneItem(Integer itemId) {
        Optional<ShopVO> optional = repository.findById(itemId);
        return optional.orElse(null);
    }

    public List<ShopVO> getAllItems() {
        return repository.findAll();
    }

    public List<ShopVO> getItemsByIds(List<Integer> itemIds) {
        return repository.findAllById(itemIds);
    }

    public void clearCart() {
        repository.deleteAll();
    }

    public List<ShopVO> getCartItems(Integer memNo) {
        return repository.findByMemNo(memNo);
    }
    
    public void addItemToCart(Integer mealsId) {
        // 檢查購物車中是否已經存在該商品
        Optional<ShopVO> optional = repository.findById(mealsId);
        ShopVO shopVO;
        if (optional.isPresent()) {
            shopVO = optional.get();
            shopVO.setQuantity(shopVO.getQuantity() + 1); // 商品數量加一
        } else {
            shopVO = new ShopVO();
            shopVO.setMealsId(mealsId);
            shopVO.setQuantity(1); // 第一次加入購物車，設置數量為一
        }
        // 假設 ShopVO 中有設置單價和總價的相關邏輯，這裡需要相應設置
        shopVO.setTotalPrice(shopVO.getUnitPrice() * shopVO.getQuantity());
        
        repository.save(shopVO); // 儲存或更新至資料庫
    }
    
    
}
