/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2021 The author and/or original authors.
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

import griffon.annotations.core.Nonnull
import griffon.core.artifact.GriffonService
import org.kordamp.jipsy.annotations.ServiceProviderFor
import wslite.rest.ContentType
import wslite.rest.RESTClient
import wslite.rest.Response

import javax.inject.Inject

@ServiceProviderFor(GriffonService)
class GroovyCalculatorService {
    @Inject
    private WsliteHandler wsliteHandler

    Double calculate(@Nonnull String num1, @Nonnull String num2) {
        Map args = [
            url        : 'http://localhost:9988/calculator',
            readTimeout: 1000,
            id         : 'client'
        ]
        wsliteHandler.withRest(args, { Map<String, Object> params, RESTClient client ->
            Response response = client.get(
                path: '/add',
                accept: ContentType.JSON,
                query: [num1: num1, num2: num2])
            response.json.result
        })
    }
}