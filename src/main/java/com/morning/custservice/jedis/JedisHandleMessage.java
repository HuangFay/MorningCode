package com.morning.custservice.jedis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;


public class JedisHandleMessage {
	// 此範例key的設計為(發送者名稱:接收者名稱)，實際應採用(發送者會員編號:接收者會員編號)

	private static JedisPool pool = JedisPoolUtil.getJedisPool();

	public static List<String> getHistoryMsg(String sender, String receiver) {
		String key = new StringBuilder(sender).append(":").append(receiver).toString();
		Jedis jedis = null;
		jedis = pool.getResource();
		List<String> historyData = jedis.lrange(key, 0, -1);
		jedis.close();
		return historyData;
	}

	public static void saveChatMessage(String sender, String receiver, String message) {
		// 對雙方來說，都要各存著歷史聊天記錄
		String senderKey = new StringBuilder(sender).append(":").append(receiver).toString();
		String receiverKey = new StringBuilder(receiver).append(":").append(sender).toString();
		Jedis jedis = pool.getResource();
		jedis.rpush(senderKey, message);
		jedis.rpush(receiverKey, message);

		jedis.close();
	}
	
	public static List<String> getMemList() {
		Jedis jedis = pool.getResource();
		
		// 初始光標
        byte[] cursor = ScanParams.SCAN_POINTER_START_BINARY;
        ScanParams scanParams = new ScanParams().count(10).match("emp:*".getBytes());
        List<String> empNumbers = new ArrayList<>();

        do {
            // 執行 SCAN 命令
            ScanResult<byte[]> scanResult = jedis.scan(cursor, scanParams);
            cursor = scanResult.getCursorAsBytes(); // 獲取新的光標

            // 過濾和處理鍵
            for (byte[] key : scanResult.getResult()) {
                String keyString = new String(key);
                System.out.println("Key: " + keyString);

                // 提取數字部分
                String numberPart = keyString.substring(4); // "emp:".length() = 4
                empNumbers.add(numberPart);
            }
        } while (!ScanParams.SCAN_POINTER_START.equals(new String(cursor))); // 當光標為 0 時結束掃描

        jedis.close();

        // 將結果轉換為字符串數組
        String[] empArray = empNumbers.toArray(new String[0]);

        // 輸出結果
        System.out.println("Extracted emp numbers: ");
        for (String num : empArray) {
            System.out.println(num);
        }
        
		return empNumbers;
		
	}

}
