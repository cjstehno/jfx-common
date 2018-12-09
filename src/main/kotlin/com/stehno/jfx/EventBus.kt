/**
 * Copyright (C) 2018 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stehno.jfx

import org.springframework.stereotype.Component
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Simple application event bus.
 */
@Component
class EventBus {

    private val subscribers = mutableMapOf<String, MutableList<(p: Map<String, Any>) -> Unit>>()
    private val lock = ReentrantReadWriteLock()

    fun publish(eventId: String, payload: Map<String, Any> = mapOf()) {
        listSubscribers(eventId)?.forEach { it.invoke(payload) }
    }

    fun subscribe(eventId: String, op: (p: Map<String, Any>) -> Unit) {
        lock.write {
            subscribers.computeIfAbsent(eventId) { mutableListOf() }.add(op)
        }
    }

    private fun listSubscribers(eventId: String): List<(p: Map<String, Any>) -> Unit>? {
        return lock.read {
            subscribers[eventId]?.toList()
        }
    }
}
