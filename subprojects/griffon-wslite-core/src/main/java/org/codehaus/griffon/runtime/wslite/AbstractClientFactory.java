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
package org.codehaus.griffon.runtime.wslite;

import griffon.annotations.core.Nonnull;
import griffon.core.GriffonApplication;
import griffon.exceptions.NewInstanceException;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;

import static griffon.util.GriffonClassUtils.setProperties;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public abstract class AbstractClientFactory<T> {
    protected static final String ERROR_PARAMS_NULL = "Argument 'params' must not be null";

    private static final String[] HTTP_PROPERTIES = {
        "connectTimeout", "readTimeout",
        "followRedirects", "useCaches",
        "sslTrustAllCerts", "sslTrustStoreFile",
        "sslTrustStorePassword", "proxy",
        "httpConnectionFactory", "authorization"
    };

    protected final GriffonApplication application;
    protected final Class<T> clientClass;

    @Inject
    public AbstractClientFactory(@Nonnull GriffonApplication application, @Nonnull Class<T> clientClass) {
        this.application = requireNonNull(application, "Argument 'application' must not be null");
        this.clientClass = requireNonNull(clientClass, "Argument 'clientClass' must not be null");
    }

    @Nonnull
    protected T createClient(@Nonnull Map<String, Object> params) throws NewInstanceException {
        T client = null;
        try {
            client = clientClass.newInstance();
            setProperties(client, params);
            return client;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new NewInstanceException(clientClass, e);
        }
    }

    @Nonnull
    protected Map<String, Object> getHttpParams(@Nonnull Map<String, Object> params) {
        Map<String, Object> httpParams = new LinkedHashMap<>();
        for (String param : HTTP_PROPERTIES) {
            Object value = params.remove(param);
            if (value != null) {
                httpParams.put(param, value);
            }
        }
        return httpParams;
    }
}