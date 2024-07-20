package com.reservation.model;

import java.util.List;
import java.util.Optional;

import com.morning.mem.model.MemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("resService")
public class ResService {
	@Autowired
	ResRepository repository;

	public void addRes(ResVO resVO) {
		repository.save(resVO);
	}
	public void updateRes(ResVO resVO) {
		repository.save(resVO);
	}
	public ResVO getOneRes(Integer reservvationId) {
		Optional<ResVO> optional=repository.findById(reservvationId);
		return optional.orElse(null);
	}


	public List<ResVO> getMemRes(MemVO memVO) {
		return repository.findByMemVO(memVO);
	}
	public List<ResVO>getAll(){
		return repository.findAll();
	}


	 /**
	 * 取得更新後位置
	 * @param originalSeat 原本的座位字串
	 * @param updateSeat 更新的座位數字
	 * @return
	 */

	public String getUpdateSeat(String originalSeat, int updateSeat) {
		// 1. 先將原始座位號碼轉成陣列，座位長度固定10
		String[] originalSeatArray = new String[originalSeat.length()];
		for (int i = 0; i < originalSeatArray.length; i++) {
			if (i == updateSeat - 1) {
				originalSeatArray[i] = "1";
			} else {
				originalSeatArray[i] = String.valueOf(originalSeat.charAt(i));
			}
		}

		return String.join("", originalSeatArray);
	}

	//驗證座位是否足夠
	public Integer checkSit(Integer totalSitNumber, Integer useSitNumber, Integer addNumber   ){
		if(totalSitNumber+addNumber <= useSitNumber){
			return totalSitNumber + addNumber;
		}
		return null;
	}

	//字串比較 如果使用座位增加後 沒有超過設定值 則更新字串
	public  String compareLastTwoDigits(String totalSit, String useSit, Integer addNumber,Integer tableTimeId) {
		// 提取字串的後兩位

		int lastTwoDigits1 = Integer.parseInt(totalSit.substring((tableTimeId * 2) - 2, tableTimeId * 2));
		int lastTwoDigits2 = Integer.parseInt(useSit.substring((tableTimeId * 2) - 2, tableTimeId * 2));
		System.out.println(tableTimeId * 2 - 2);

		if (Integer.compare(lastTwoDigits1, lastTwoDigits2 + addNumber) >= 0) {
			lastTwoDigits2 = lastTwoDigits2 + addNumber;

			// 使用 StringBuilder 來更新 useSit 的相對應字串
			StringBuilder updatedUseSit = new StringBuilder(useSit);
			String newLastTwoDigits = String.format("%02d", lastTwoDigits2); // 確保兩位數格式

			// 回填更新後的 lastTwoDigits2 數字到 useSit 的相對應位置
			updatedUseSit.replace((tableTimeId * 2) - 2, tableTimeId * 2, newLastTwoDigits);

			return updatedUseSit.toString();
		}

		return useSit; // 如果沒有更新，返回原始字串
	}
	/*
	 * 更新時先恢復ＤＢ中能用座位
	 */
	public String restoreset( String useSit, Integer addNumber,Integer tableTimeId) {
		// 提取字串的後兩位


		int lastTwoDigits2 = Integer.parseInt(useSit.substring((tableTimeId * 2) - 2, tableTimeId * 2));
		System.out.println(tableTimeId * 2 - 2);


		lastTwoDigits2 = lastTwoDigits2 - addNumber;

		// 使用 StringBuilder 來更新 useSit 的相對應字串
		StringBuilder updatedUseSit = new StringBuilder(useSit);
		String newLastTwoDigits = String.format("%02d", lastTwoDigits2); // 確保兩位數格式

		// 回填更新後的 lastTwoDigits2 數字到 useSit 的相對應位置
		updatedUseSit.replace((tableTimeId * 2) - 2, tableTimeId * 2, newLastTwoDigits);

		return updatedUseSit.toString();

	}
	public void cancelReservation(Integer reservationId) {
		Optional<ResVO> optionalReservation = repository.findById(reservationId);
		if (optionalReservation.isPresent()) {
			ResVO resVO = optionalReservation.get();
			resVO.setReservationStatus((byte) 2);

			repository.save(resVO);
		}
	}


}
