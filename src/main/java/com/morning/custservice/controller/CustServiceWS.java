package com.morning.custservice.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.morning.custservice.config.SpringContext;
import com.morning.custservice.jedis.JedisHandleMessage;
import com.morning.custservice.model.ChatMessage;
import com.morning.custservice.model.State;
import com.morning.mem.model.MemRepository;
import com.morning.mem.model.MemVO;


@Component
@ServerEndpoint("/CustServiceWS/{userName}")
public class CustServiceWS {
	private static Map<String, Session> sessionsMap = new ConcurrentHashMap<>();
	private static Map<String, String> usrNameMap = new ConcurrentHashMap<>();
	
	private MemRepository memRepository;
	
	private static Session empSession = null;
	
	Gson gson = new Gson();

	@OnOpen
	public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {
//		System.out.println("userName = " + userName);
//		sessionsMap.put(userName, userSession);
//		Set<String> userNames = sessionsMap.keySet();
//		State stateMessage = new State("open", userName, userNames);
//		String stateMessageJson = gson.toJson(stateMessage);
//		Collection<Session> sessions = sessionsMap.values();
//		String text = String.format("Session ID = %s, connected; userName = %s%nusers: %s", userSession.getId(),
//				userName, userNames);
//		System.out.println(text);
		
		System.out.println("userName = " + userName);
		
		sessionsMap.remove(userName);
		sessionsMap.put(userName, userSession);
		
		
		if ( userName.equals("emp") ) {
			empSession = userSession;
			usrNameMap.put(userName, "emp");
			
			Set<String> userNames = sessionsMap.keySet();
			State stateMessage = new State("open", userName, userNames, usrNameMap);
			String stateMessageJson = gson.toJson(stateMessage);
			if ( empSession != null ) {
				empSession.getAsyncRemote().sendText(stateMessageJson);
			}
		}
		else {
			System.out.println(userName);
			memRepository = SpringContext.getBean(MemRepository.class);
			Integer memNo = Integer.parseInt(userName);
	        Optional<MemVO> memVO = memRepository.findById(memNo);
			if( memVO.isPresent() ) {
				usrNameMap.put(userName, memVO.get().getMemName() );
			} else {
				usrNameMap.put(userName, "");
			}
			
			Set<String> userNames = sessionsMap.keySet();
			State stateMessage = new State("open", userName, userNames, usrNameMap);
			String stateMessageJson = gson.toJson(stateMessage);
			if ( empSession != null && empSession.isOpen() ) {
				empSession.getAsyncRemote().sendText(stateMessageJson);
			}
			
		}


	}

	@OnMessage
	public void onMessage(Session userSession, String message) {
		ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
		String sender = chatMessage.getSender();
		String receiver = chatMessage.getReceiver();
		
		System.out.println("onMessage---");
		
		if ("history".equals(chatMessage.getType())) {
			List<String> historyData = JedisHandleMessage.getHistoryMsg(sender, receiver);
			String historyMsg = gson.toJson(historyData);
			ChatMessage cmHistory = new ChatMessage("history", sender, receiver, historyMsg);
			if (userSession != null && userSession.isOpen()) {
				userSession.getAsyncRemote().sendText(gson.toJson(cmHistory));
				System.out.println("history = " + gson.toJson(cmHistory));
				return;
			}
		}
		

		Session receiverSession = sessionsMap.get(receiver);
		if (receiverSession != null && receiverSession.isOpen()) {
			receiverSession.getAsyncRemote().sendText(message);
		}
		if ( userSession != null ) {
			userSession.getAsyncRemote().sendText(message);
		}
		JedisHandleMessage.saveChatMessage(sender, receiver, message);
		System.out.println("Message received: " + message);
	}

	@OnError
	public void onError(Session userSession, Throwable e) {
		System.out.println("Error: " + e.toString());
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		String userNameClose = null;
		Set<String> userNames = sessionsMap.keySet();
		
		for (String userName : userNames) {
			if (sessionsMap.get(userName).equals(userSession)) {
				userNameClose = userName;
				if ( userName.equals("emp") ) {
					sessionsMap.remove(userName);
				}
				break;
			}
		}

		if (userNameClose != null) {
			//Set<String> usersNames = sessionsMap.keySet();
			//State stateMessage = new State("close", userNameClose, userNames, usersNames);
			//String stateMessageJson = gson.toJson(stateMessage);
			Collection<Session> sessions = sessionsMap.values();
			for (Session session : sessions) {
				if ( session.isOpen() ) {
					//session.getAsyncRemote().sendText(stateMessageJson);
				}
			}
		}

		String text = String.format("session ID = %s, disconnected; close code = %d%nusers: %s", userSession.getId(),
				reason.getCloseCode().getCode(), userNames);
		System.out.println(text);
	}
}
