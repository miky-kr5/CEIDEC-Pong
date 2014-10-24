package com.gamejolt.mikykr5.ceidecpong.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class SoundComponent extends Component implements Poolable {
	public String path = "";

	@Override
	public void reset() {
		this.path = "";
	}
}
