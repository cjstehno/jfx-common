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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Supplier

internal class ContextTest {

    private lateinit var context: Context

    @BeforeEach fun beforeEach() {
        context = DummyContext()
    }

    @Test fun `registering and resolving (Class)`() {
        val entity = Foo("alpha")
        val alpha = context.register(entity)

        assertThat(alpha, equalTo(entity))

        assertThat(context.resolve(Foo::class.java), equalTo(entity))

        assertThat(context.resolveAny(Foo::class.java), equalTo(entity as Any))
    }

    @Test fun `registering and resolving with Factory`() {
        val a = Foo("alpha")
        val b = Foo("bravo")

        assertThat(context.register(Supplier { a }), equalTo(a))
        assertThat(context.resolve(Foo::class.java), equalTo(a))

        assertThat(context.register({ b }, "b-foo"), equalTo(b))
        assertThat(context.resolve(Foo::class.java, "b-foo"), equalTo(b))
    }

    @Test fun `registering and resolving (String)`() {
        val a = Foo("alpha")
        val b = Foo("bravo")

        val alpha = context.register(a, "a_foo")
        val bravo = context.register(b, "b_foo")

        assertThat(alpha, equalTo(a))
        assertThat(bravo, equalTo(b))

        assertThat(context.resolve(Foo::class.java, "a_foo"), equalTo(a))
        assertThat(context.resolve(Foo::class.java, "b_foo"), equalTo(b))

        assertThat(context.resolveAny("a_foo"), equalTo(a as Any))
        assertThat(context.resolveAny("b_foo"), equalTo(b as Any))
    }

    @Test fun `loading assets`() {
        val text = context.asset("/something.txt").readText()
        assertThat(text, equalTo("Something."))
    }
}

class DummyContext : Context()

data class Foo(val value: String)