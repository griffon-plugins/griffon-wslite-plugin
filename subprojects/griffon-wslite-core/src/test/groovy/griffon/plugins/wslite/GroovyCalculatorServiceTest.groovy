/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2020 The author and/or original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.plugins.wslite

import griffon.test.core.GriffonUnitRule
import griffon.test.core.TestFor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockserver.client.MockServerClient
import org.mockserver.junit.MockServerRule
import org.mockserver.model.Header

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

@TestFor(GroovyCalculatorService)
class GroovyCalculatorServiceTest {
    static {
        System.setProperty('org.slf4j.simpleLogger.defaultLogLevel', 'trace')
    }

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule()

    @Rule
    public final MockServerRule mockServerRule = new MockServerRule(this, 9988)

    private MockServerClient mockServerClient

    private GroovyCalculatorService service

    @Before
    void setup() {
        mockServerClient.when(
            request()
                .withMethod('GET')
                .withPath('/calculator/add')
        ).respond(
            response()
                .withStatusCode(200)
                .withHeader(new Header('Content-Type', 'application/json charset=UTF-8'))
                .withBody('\n\n{"result": 42.0}')
        )
    }

    @Test
    void addTwoNumbers() {
        // when:
        Double result = service.calculate('21.0', '21.0')

        // then:
        assertNotNull(result)
        assertEquals(42d, result, 0d)
    }
}
