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
package griffon.plugins.wslite;

import griffon.annotations.core.Nonnull;
import griffon.annotations.core.Nullable;
import griffon.plugins.wslite.exceptions.RESTException;
import griffon.plugins.wslite.exceptions.SOAPException;

import java.util.Map;

/**
 * @author Andres Almiray
 */
public interface WsliteHandler {
    //tag::methods[]
    @Nullable
    <R> R withRest(@Nonnull Map<String, Object> params, @Nonnull RESTClientCallback<R> callback) throws RESTException;

    @Nullable
    <R> R withSoap(@Nonnull Map<String, Object> params, @Nonnull SOAPClientCallback<R> callback) throws SOAPException;

    void destroyRestClient(@Nonnull String clientId);

    void destroySoapClient(@Nonnull String clientId);
    //end::methods[]
}