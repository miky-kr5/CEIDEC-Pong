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

/**
 * This package contains the {@link com.badlogic.ashley.core.Entity} initializers. These initializers are responsible for
 * setting all initial permanent and transitory entities in each respective game. All initializers should subclass
 * {@link com.gamejolt.mikykr5.ceidecpong.ecs.entities.EntityInitializerBase} to ensure that the 
 * {@link com.gamejolt.mikykr5.ceidecpong.states.InGameState} implementation can use them without modifications.
 */
package com.gamejolt.mikykr5.ceidecpong.ecs.entities;
