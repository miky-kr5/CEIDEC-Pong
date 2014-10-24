package com.gamejolt.mikykr5.ceidecpong.ecs.systems.messaging;

import java.util.HashMap;
import java.util.Map;

public class InterSystemMessage{
	public final String target;
	public final Map<String, Object> data;

	public InterSystemMessage(String target){
		this.target = target;
		this.data = new HashMap<String, Object>();
	}
}