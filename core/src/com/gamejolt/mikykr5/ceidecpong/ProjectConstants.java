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
package com.gamejolt.mikykr5.ceidecpong;

/**
 * This class holds some project-wise constants.
 * 
 * @author Miguel Astor
 */
public abstract class ProjectConstants{
	/**
	 * What to return when the application terminates successfully.
	 */
	public static final int     EXIT_SUCCESS               = 0;

	/**
	 * What to return when the application terminates closes due to an error. 
	 */
	public static final int     EXIT_FAILURE               = 1;

	/**
	 * Enable/disable logging.
	 */
	public static final boolean DEBUG                      = false;

	/**
	 * Logical screen width.
	 */
	public static final int     FB_WIDTH = 1920;

	/**
	 * Logical screen height.
	 */
	public static final int     FB_HEIGHT = 1080;
}