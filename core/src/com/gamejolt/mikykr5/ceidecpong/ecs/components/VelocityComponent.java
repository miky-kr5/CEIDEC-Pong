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
package com.gamejolt.mikykr5.ceidecpong.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * A 2D velocity {@link Component}
 * 
 * @author Miguel Astor
 */
public class VelocityComponent extends Component implements Poolable {
	/**
	 * The velocity in the X axis.
	 */
	public float vx = 0;

	/**
	 * The velocity in the Y axis.
	 */
	public float vy = 0;

	/**
	 * Sets both fields simultaneously.
	 * @param vx
	 * @param vy
	 */
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
