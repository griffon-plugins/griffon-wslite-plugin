/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.plugins.wslite

import griffon.core.test.GriffonUnitRule
import org.junit.Rule
import org.mockserver.client.server.MockServerClient
import org.mockserver.junit.MockServerRule
import org.mockserver.model.Header
import spock.lang.Specification
import spock.lang.Unroll
import wslite.rest.ContentType

import javax.inject.Inject

import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

@Unroll
class RESTCapabilitiesSpec extends Specification {
    static {
        System.setProperty('org.slf4j.simpleLogger.defaultLogLevel', 'trace')
    }

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule()

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(9988, this)

    private MockServerClient mockServerClient

    @Inject
    private WsliteHandler wsliteHandler

    void 'Test REST capabilities'() {
        given:
        mockServerClient.when(
            request()
                .withMethod('GET')
                .withPath('/names')
        ).respond(
            response()
                .withStatusCode(200)
                .withHeader(new Header('Content-Type', 'application/json; charset=UTF-8'))
                .withBody('\n\n["Adam", "Jamie", "Tori", "Kary", "Grant"]')
        )

        when:
        List names = wsliteHandler.withRest(url: 'http://localhost:9988', readTimeout: 1000) { params, client ->
            def response = client.get(path: '/names', accept: ContentType.JSON)
            response.json.collect([]) { it }
        }

        then:
        names == ['Adam', 'Jamie', 'Tori', 'Kary', 'Grant']
    }
}
