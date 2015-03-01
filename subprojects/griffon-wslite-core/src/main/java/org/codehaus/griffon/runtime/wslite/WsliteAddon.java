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

import griffon.core.GriffonApplication;
import griffon.core.env.Metadata;
import griffon.plugins.monitor.MBeanManager;
import griffon.plugins.wslite.RESTClientStorage;
import griffon.plugins.wslite.SOAPClientStorage;
import griffon.plugins.wslite.WsliteHandler;
import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon;
import org.codehaus.griffon.runtime.jmx.RESTClientStorageMonitor;
import org.codehaus.griffon.runtime.jmx.SOAPClientStorageMonitor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andres Almiray
 * @since 1.1.0
 */
@Named("wslite")
public class WsliteAddon extends AbstractGriffonAddon {
    @Inject
    private RESTClientStorage restClientStorage;

    @Inject
    private SOAPClientStorage soapClientStorage;

    @Inject
    private WsliteHandler wsliteHandler;

    @Inject
    private MBeanManager mbeanManager;

    @Inject
    private Metadata metadata;

    @Override
    public void init(@Nonnull GriffonApplication application) {
        mbeanManager.registerMBean(new RESTClientStorageMonitor(metadata, restClientStorage));
        mbeanManager.registerMBean(new SOAPClientStorageMonitor(metadata, soapClientStorage));
    }

    @Override
    public void onShutdown(@Nonnull GriffonApplication application) {
        for (String clientId : restClientStorage.getKeys()) {
            wsliteHandler.destroyRestClient(clientId);
        }
        for (String clientId : soapClientStorage.getKeys()) {
            wsliteHandler.destroySoapClient(clientId);
        }
    }
}
