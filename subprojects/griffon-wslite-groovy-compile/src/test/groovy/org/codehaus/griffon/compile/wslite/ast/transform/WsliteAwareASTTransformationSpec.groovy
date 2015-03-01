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
package org.codehaus.griffon.compile.wslite.ast.transform

import griffon.plugins.wslite.WsliteHandler
import spock.lang.Specification

import java.lang.reflect.Method

/**
 * @author Andres Almiray
 */
class WsliteAwareASTTransformationSpec extends Specification {
    def 'WsliteAwareASTTransformation is applied to a bean via @WsliteAware'() {
        given:
        GroovyShell shell = new GroovyShell()

        when:
        def bean = shell.evaluate('''
        @griffon.transform.WsliteAware
        class Bean { }
        new Bean()
        ''')

        then:
        bean instanceof WsliteHandler
        WsliteHandler.methods.every { Method target ->
            bean.class.declaredMethods.find { Method candidate ->
                candidate.name == target.name &&
                candidate.returnType == target.returnType &&
                candidate.parameterTypes == target.parameterTypes &&
                candidate.exceptionTypes == target.exceptionTypes
            }
        }
    }

    def 'WsliteAwareASTTransformation is not applied to a WsliteHandler subclass via @WsliteAware'() {
        given:
        GroovyShell shell = new GroovyShell()

        when:
        def bean = shell.evaluate('''
        import griffon.plugins.wslite.*
        import griffon.plugins.wslite.exceptions.*

        import javax.annotation.Nonnull
        @griffon.transform.WsliteAware
        class WsliteHandlerBean implements WsliteHandler {
            @Override
            public <R> R withRest(@Nonnull Map<String,Object> params,@Nonnull RESTClientCallback<R> callback) throws RESTException{
                return null
            }
            @Override
            public <R> R withSoap(@Nonnull Map<String,Object> params,@Nonnull SOAPClientCallback<R> callback) throws SOAPException{
                return null
            }
            @Override
            void destroyRestClient(@Nonnull String clientId) {}
            @Override
            void destroySoapClient(@Nonnull String clientId) {}
        }
        new WsliteHandlerBean()
        ''')

        then:
        bean instanceof WsliteHandler
        WsliteHandler.methods.every { Method target ->
            bean.class.declaredMethods.find { Method candidate ->
                candidate.name == target.name &&
                    candidate.returnType == target.returnType &&
                    candidate.parameterTypes == target.parameterTypes &&
                    candidate.exceptionTypes == target.exceptionTypes
            }
        }
    }
}
