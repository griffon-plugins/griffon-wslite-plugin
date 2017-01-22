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

import griffon.plugins.wslite.RESTClientCallback;
import griffon.plugins.wslite.RESTClientFactory;
import griffon.plugins.wslite.RESTClientStorage;
import griffon.plugins.wslite.SOAPClientCallback;
import griffon.plugins.wslite.SOAPClientFactory;
import griffon.plugins.wslite.SOAPClientStorage;
import griffon.plugins.wslite.WsliteHandler;
import griffon.plugins.wslite.exceptions.RESTException;
import griffon.plugins.wslite.exceptions.SOAPException;
import wslite.rest.RESTClient;
import wslite.soap.SOAPClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;

import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultWsliteHandler implements WsliteHandler {
    private static final String ERROR_PARAMS_NULL = "Argument 'params' must not be null";
    private static final String ERROR_CALLBACK_NULL = "Argument 'callback' must not be null";
    private static final String KEY_ID = "id";
    private static final String ERROR_CLIENTID_BLANK = "Argument 'clientId' must not be blank";

    private final RESTClientFactory restClientFactory;
    private final RESTClientStorage restClientStorage;
    private final SOAPClientFactory soapClientFactory;
    private final SOAPClientStorage soapClientStorage;

    @Inject
    public DefaultWsliteHandler(@Nonnull RESTClientFactory restClientFactory, @Nonnull RESTClientStorage restClientStorage,
                                @Nonnull SOAPClientFactory soapClientFactory, @Nonnull SOAPClientStorage soapClientStorage) {
        this.restClientFactory = restClientFactory;
        this.restClientStorage = restClientStorage;
        this.soapClientFactory = soapClientFactory;
        this.soapClientStorage = soapClientStorage;
    }

    @Nullable
    @Override
    public <R> R withRest(@Nonnull Map<String, Object> params, @Nonnull RESTClientCallback<R> callback) throws RESTException {
        requireNonNull(callback, ERROR_CALLBACK_NULL);
        try {
            RESTClient client = getRESTClient(params);
            return callback.handle(params, client);
        } catch (Exception e) {
            throw new RESTException("An error occurred while executing REST call", e);
        }
    }

    @Nullable
    @Override
    public <R> R withSoap(@Nonnull Map<String, Object> params, @Nonnull SOAPClientCallback<R> callback) throws SOAPException {
        requireNonNull(callback, ERROR_CALLBACK_NULL);
        try {
            SOAPClient client = getSOAPClient(params);
            return callback.handle(params, client);
        } catch (Exception e) {
            throw new RESTException("An error occurred while executing SOAP call", e);
        }
    }

    @Override
    public void destroyRestClient(@Nonnull String clientId) {
        requireNonBlank(clientId, ERROR_CLIENTID_BLANK);
        restClientStorage.remove(clientId);
    }

    @Override
    public void destroySoapClient(@Nonnull String clientId) {
        requireNonBlank(clientId, ERROR_CLIENTID_BLANK);
        soapClientStorage.remove(clientId);
    }

    @Nonnull
    private RESTClient getRESTClient(@Nonnull Map<String, Object> params) {
        requireNonNull(params, ERROR_PARAMS_NULL);
        if (params.containsKey(KEY_ID)) {
            String id = String.valueOf(params.remove(KEY_ID));
            RESTClient client = restClientStorage.get(id);
            if (client == null) {
                client = restClientFactory.create(params);
                restClientStorage.set(id, client);
            }
            return client;
        }
        return restClientFactory.create(params);
    }

    @Nonnull
    private SOAPClient getSOAPClient(@Nonnull Map<String, Object> params) {
        requireNonNull(params, ERROR_PARAMS_NULL);
        if (params.containsKey(KEY_ID)) {
            String id = String.valueOf(params.remove(KEY_ID));
            SOAPClient client = soapClientStorage.get(id);
            if (client == null) {
                client = soapClientFactory.create(params);
                soapClientStorage.set(id, client);
            }
            return client;
        }
        return soapClientFactory.create(params);
    }
}
