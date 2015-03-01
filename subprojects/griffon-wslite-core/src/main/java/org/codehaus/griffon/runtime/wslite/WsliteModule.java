/*
 * Copyright 2014-2015 the original author or authors.
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

import griffon.core.addon.GriffonAddon;
import griffon.core.injection.Module;
import griffon.plugins.wslite.RESTClientFactory;
import griffon.plugins.wslite.RESTClientStorage;
import griffon.plugins.wslite.SOAPClientFactory;
import griffon.plugins.wslite.SOAPClientStorage;
import griffon.plugins.wslite.WsliteHandler;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.jipsy.ServiceProviderFor;

import javax.inject.Named;

/**
 * @author Andres Almiray
 */
@Named("wslite")
@ServiceProviderFor(Module.class)
public class WsliteModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        // tag::bindings[]
        bind(RESTClientStorage.class)
            .to(DefaultRESTClientStorage.class)
            .asSingleton();

        bind(RESTClientFactory.class)
            .to(DefaultRESTClientFactory.class)
            .asSingleton();

        bind(SOAPClientStorage.class)
            .to(DefaultSOAPClientStorage.class)
            .asSingleton();

        bind(SOAPClientFactory.class)
            .to(DefaultSOAPClientFactory.class)
            .asSingleton();

        bind(WsliteHandler.class)
            .to(DefaultWsliteHandler.class)
            .asSingleton();

        bind(GriffonAddon.class)
            .to(WsliteAddon.class)
            .asSingleton();
        // end::bindings[]
    }
}
