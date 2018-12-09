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

import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextArea
import javafx.scene.image.ImageView
import java.io.File
import java.io.FileFilter

class ImportProgressDialogController(private val imageResource: String, private val importingService: ImportingService) {

    lateinit var importProgressDialog: Dialog<Void>
    lateinit var progressBar: ProgressBar
    lateinit var progressText: TextArea

    fun initialize() {
        importProgressDialog.graphic = ImageView(imageResource)
        importProgressDialog.dialogPane.lookupButton(ButtonType.CLOSE).isDisable = true
    }

    fun load(file: File, eventId: String) {
        Platform.runLater {
            val files = when (file.isDirectory) {
                true -> file.listFiles(FileFilter { !it.isDirectory }).toList()
                else -> listOf(file)
            }

            var count = 0

            files.forEach { f ->
                val importer = importingService.importer(f)

                progressText.appendText("${when (importer?.doImport(f)) {
                    true -> "Imported"
                    else -> "Skipped"
                }} $f...\n")

                progressBar.progress = (++count).toDouble() / files.size
            }


            progressText.appendText("Done.")
            progressBar.progress = 1.0

            importProgressDialog.dialogPane.lookupButton(ButtonType.CLOSE).isDisable = false

            importingService.complete(eventId)
        }
    }
}