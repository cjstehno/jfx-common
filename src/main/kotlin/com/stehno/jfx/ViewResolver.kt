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

import javafx.fxml.FXMLLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class ViewResolver(@Autowired private val resourceLoader: ResourceLoader,
                   @Autowired private val context: ApplicationContext) {

    inline fun <reified V> view(path: String): V {
        return loaderFor("classpath:$path").load()
    }

    inline fun <reified V, C> viewAndController(path: String): Pair<V, C> {
        val loader = loaderFor(path)
        return Pair(loader.load(), loader.getController())
    }

    fun loaderFor(path: String): FXMLLoader {
        val loader = FXMLLoader(resourceLoader.getResource(path).url)
        loader.setControllerFactory { type -> context.getBean(type) }
        return loader
    }
}
