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
package griffon.plugins.wslite;

import griffon.annotations.core.Nonnull;
import griffon.annotations.core.Nullable;
import griffon.core.artifact.GriffonService;
import griffon.plugins.wslite.exceptions.RESTException;
import griffon.util.CollectionUtils;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.kordamp.jipsy.annotations.ServiceProviderFor;
import wslite.json.JSONException;
import wslite.json.JSONObject;
import wslite.rest.ContentType;
import wslite.rest.RESTClient;
import wslite.rest.Response;

import javax.inject.Inject;
import java.util.Map;

import static griffon.util.CollectionUtils.newMap;

@ServiceProviderFor(GriffonService.class)
public class CalculatorService extends AbstractGriffonService {
    @Inject
    private WsliteHandler wsliteHandler;

    public Double calculate(final @Nonnull String num1, final @Nonnull String num2) {
        Map<String, Object> params = CollectionUtils.<String, Object>map()
            .e("url", "http://localhost:9988/calculator")
            .e("readTimeout", 1000)
            .e("id", "client");
        return wsliteHandler.withRest(params,
            new RESTClientCallback<Double>() {
                @Nullable
                public Double handle(@Nonnull Map<String, Object> params, @Nonnull RESTClient client) throws RESTException {
                    Response response = client.get(newMap(
                        "path", "/add",
                        "accept", ContentType.JSON,
                        "query", newMap("num1", num1, "num2", num2)));
                    JSONObject json = (JSONObject) response.propertyMissing("json");
                    try {
                        return json.getDouble("result");
                    } catch (JSONException e) {
                        return Double.NaN;
                    }
                }
            });
    }
}