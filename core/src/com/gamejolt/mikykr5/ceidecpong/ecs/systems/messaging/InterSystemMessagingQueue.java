package com.gamejolt.mikykr5.ceidecpong.ecs.systems.messaging;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;

/**
 * A messaging queue to communicate two {@link EntitySystem} instances.
 * 
 * @author Miguel Astor
 */
public abstract class InterSystemMessagingQueue{
	/**
	 * The message queue.
	 */
	private static Queue<InterSystemMessage> queue = new LinkedList<InterSystemMessage>();

	/**
	 * Adds a message to the queue.
	 * 
	 * @param message The message to add.
	 * @throws IllegalArgumentException If message is null.
	 */
	public static synchronized void pushMessage(InterSystemMessage message) throws IllegalArgumentException{
		if(message == null)
			throw new IllegalArgumentException("Message is null");

		queue.add(message);
	}

	/**
	 * Fetches a message from the queue if it's intended receiver is the caller of this method. A message is removed from the
	 * queue only if it was successfully retrieved.
	 * 
	 * @param receiver The intended receiver.
	 * @return The message.
	 * @throws IllegalArgumentException If receiver is null.
	 */
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
