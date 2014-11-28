/*
 * Copyright 2014 the original author or authors.
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
package griffon.plugins.wslite;

import griffon.core.GriffonApplication;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import griffon.plugins.wslite.exceptions.RESTException;
import griffon.util.CollectionUtils;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import wslite.json.JSONException;
import wslite.json.JSONObject;
import wslite.rest.ContentType;
import wslite.rest.RESTClient;
import wslite.rest.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;

import static griffon.util.CollectionUtils.newMap;

@ArtifactProviderFor(GriffonService.class)
public class CalculatorService extends AbstractGriffonService {
    @Inject
    private WsliteHandler wsliteHandler;

    @Inject
    public CalculatorService(@Nonnull GriffonApplication application) {
        super(application);
    }

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