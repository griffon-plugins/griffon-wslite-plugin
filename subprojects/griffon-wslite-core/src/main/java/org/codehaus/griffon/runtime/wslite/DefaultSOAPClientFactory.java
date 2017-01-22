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
package org.codehaus.griffon.runtime.wslite;

import griffon.core.GriffonApplication;
import griffon.plugins.wslite.SOAPClientFactory;
import wslite.soap.SOAPClient;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

import static griffon.util.GriffonClassUtils.setProperties;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultSOAPClientFactory extends AbstractClientFactory<SOAPClient> implements SOAPClientFactory {
    @Inject
    public DefaultSOAPClientFactory(@Nonnull GriffonApplication application) {
        super(application, SOAPClient.class);
    }

    @Nonnull
    public SOAPClient create(@Nonnull Map<String, Object> params) {
        requireNonNull(params, ERROR_PARAMS_NULL);
        Map<String, Object> httpParams = getHttpParams(params);
        SOAPClient client = createClient(params);
        setProperties(client.getHttpClient(), httpParams);
        return client;
    }
}