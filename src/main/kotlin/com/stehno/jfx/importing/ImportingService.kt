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
package com.stehno.jfx.importing

import com.stehno.jfx.Event
import com.stehno.jfx.EventBus
import com.stehno.jfx.EventId
import java.io.File

class ImportingService(private val eventBus: EventBus) {

    private val importers = mutableListOf<Importer>()

    fun register(importer: Importer) {
        importers.add(importer)
    }

    fun importer(file: File) = importers.find { imp -> imp.canImport(file) }

    fun complete(eventId: EventId) {
        eventBus.publish(Event(eventId))
    }
}
