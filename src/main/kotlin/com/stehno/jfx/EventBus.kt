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

import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Simple application event bus.
 */
class EventBus {

    private val subscribers = mutableMapOf<String, MutableList<(e: Event) -> Unit>>()
    private val executorService = Executors.newFixedThreadPool(1)
    private val lock = ReentrantReadWriteLock()

    fun publish(event: Event) {
        listSubscribers(event.id)?.forEach { it.invoke(event) }
    }

    fun publishAsync(event: Event) {
        listSubscribers(event.id)?.forEach { op ->
            executorService.submit { op.invoke(event) }
        }
    }

    fun subscribe(eventId: String, op: (e: Event) -> Unit) {
        lock.write {
            subscribers.computeIfAbsent(eventId) { mutableListOf() }.add(op)
        }
    }

    fun subscribe(eventId: EventId, op: (e: Event) -> Unit) {
        lock.write {
            subscribers.computeIfAbsent(eventId.getId()) { mutableListOf() }.add(op)
        }
    }

    private fun listSubscribers(eventId: String): List<(e: Event) -> Unit>? {
        return lock.read {
            subscribers[eventId]?.toList()
        }
    }
}

interface EventId {

    fun getId(): String
}

data class Event(val id: String, val payload: Map<String, *> = emptyMap<String, Any>()) {

    constructor(ident: EventId, attrs: Map<String, *> = emptyMap<String, Any>()) : this(ident.getId(), attrs)
}