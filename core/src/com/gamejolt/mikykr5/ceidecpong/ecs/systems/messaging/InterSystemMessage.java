package com.gamejolt.mikykr5.ceidecpong.ecs.systems.messaging;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.EntitySystem;

/**
 * A message from a {@link EntitySystem }to another.
 * 
 * @author Miguel Astor
 */
public class InterSystemMessage{
	/**
	 * A string to identify the receiver of the message. Can contain anything so long as the intended receiver
	 * knows.
	 */
	public final String target;

	/**
	 * A holder for arbitrary data.
	 */
	public final Map<String, Object> data;

	/**
	 * Creates a new message object.
	 * 
	 * @param target The receiver of the message.
	 */
	public InterSystemMessage(String target){
		this.target = target;
		this.data = new HashMap<String, Object>();
	}
}