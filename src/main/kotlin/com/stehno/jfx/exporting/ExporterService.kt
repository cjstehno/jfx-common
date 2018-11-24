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
package com.stehno.jfx.exporting

import java.io.File
import java.io.OutputStream

class ExporterService {

    private val exporters: MutableMap<Pair<Class<*>, ExportType>, Exporter> = mutableMapOf()

    fun register(entityType: Class<*>, type: ExportType, exporter: Exporter) {
        exporters[Pair(entityType, type)] = exporter
    }

    fun export(entity: Any, type: ExportType, stream: OutputStream) {
        exporters[Pair(entity.javaClass, type)]?.export(entity, stream)
    }

    fun exportFile(entity: Any, type: ExportType, file: File) {
        file.outputStream().use { output ->
            export(entity, type, output)
        }
    }
}

enum class ExportType { HTML, PDF, BINARY, TEXT }

