/*
 * Copyright (c) 2014, Miguel Angel Astor Romero
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * Read the LICENSE file for more details.
 */
package com.gamejolt.mikykr5.poukemon.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class VelocityComponent extends Component implements Poolable {
	public float vx = 0;
	public float vy = 0;

	public void setXY(float vx, float vy){
		this.vx = vx;
		this.vy = vy;
	}

	@Override
	public void reset() {
		vx = 0;
		vy = 0;
	}
}
