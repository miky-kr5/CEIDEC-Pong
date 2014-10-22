package com.gamejolt.mikykr5.poukemon.ecs.systems;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.gamejolt.mikykr5.poukemon.ecs.components.Mappers;
import com.gamejolt.mikykr5.poukemon.ecs.components.SoundComponent;
import com.gamejolt.mikykr5.poukemon.utils.managers.CachedSoundManager;

public class SoundSystem extends IteratingSystem {
	private Queue<InterSystemMessage> ignoredMessages;

	@SuppressWarnings("unchecked")
	public SoundSystem() {
		super(Family.getFor(SoundComponent.class));
		ignoredMessages = new LinkedList<InterSystemMessage>();
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		InterSystemMessage message;
		String path;
		Sound sfx;
		SoundComponent sound = Mappers.soundMapper.get(entity);

		while((message = InterSystemMessagingQueue.popMessage(SoundSystem.class.getCanonicalName())) != null){
			if(message.data.containsKey("PLAY")){
				path = (String) message.data.get("PLAY");

				if(sound.path.compareTo(path) == 0){
					sfx = CachedSoundManager.getInstance().loadSound(path);
					sfx.play();
					CachedSoundManager.freeInstance();
				}else{
					ignoredMessages.add(message);
				}
			}
		}

		for(InterSystemMessage msg : ignoredMessages)
			InterSystemMessagingQueue.pushMessage(msg);
		ignoredMessages.clear();
	}
}
