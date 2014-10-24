package com.gamejolt.mikykr5.ceidecpong.ecs.systems.messaging;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;

public abstract class InterSystemMessagingQueue{
	private static Queue<InterSystemMessage> queue = new LinkedList<InterSystemMessage>();

	public static synchronized void pushMessage(InterSystemMessage message) throws IllegalArgumentException{
		if(message == null)
			throw new IllegalArgumentException("Message is null");

		queue.add(message);
	}

	public static synchronized InterSystemMessage popMessage(String receiver) throws IllegalArgumentException{
		InterSystemMessage message = null;

		if(receiver == null)
			throw new IllegalArgumentException("Target is null.");

		for(InterSystemMessage msg : queue){
			if(msg.target.compareTo(receiver) == 0){
				message = msg;
				break;
			}
		}
		if(message != null && !queue.remove(message)){
			Gdx.app.error("MESSAGING_QUEUE", "Queue did not contain message?");
		}

		return message;
	}
}
