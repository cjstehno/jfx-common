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

import com.stehno.jfx.EventId
import com.stehno.jfx.ViewResolver
import javafx.scene.control.Dialog
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import java.io.File

class ImporterUiDelegate(private val viewResolver: ViewResolver, private val eventId: EventId, private val label: String) {

    fun importFile() {
        val selectedFile = FileChooser().apply { title = "Import $label File" }.showOpenDialog(null)

        if (selectedFile != null) {
            importWithProgress(selectedFile)
        }
    }

    fun importDirectory() {
        val selectedDirectory = DirectoryChooser().apply { title = "Import $label Directory" }.showDialog(null)

        if (selectedDirectory != null) {
            importWithProgress(selectedDirectory)
        }
    }

    private fun importWithProgress(selectedDirectory: File) {
        val viewAndController = viewResolver.viewAndController<Dialog<Void>, ImportProgressDialogController>("/ui/import_progress_dialog.fxml")
        viewAndController.second.load(selectedDirectory, eventId)
        viewAndController.first.showAndWait()
    }
}