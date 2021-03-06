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

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

internal class EventBusTest {

    @Test fun `subscribe and publish`() {
        val bus = EventBus()

        val captured = mutableListOf<Map<String, *>>()
        bus.subscribe("testing") { evt ->
            captured.add(evt)
        }

        bus.publish("testing", mapOf("alpha" to 42))

        assertThat(captured.size, equalTo(1))
        assertThat(captured[0], equalTo(mapOf("alpha" to 42) as Map<String, *>))
    }
}