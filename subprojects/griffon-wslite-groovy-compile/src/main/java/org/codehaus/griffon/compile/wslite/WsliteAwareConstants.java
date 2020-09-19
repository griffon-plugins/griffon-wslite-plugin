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
package org.codehaus.griffon.compile.wslite;

import org.codehaus.griffon.compile.core.BaseConstants;
import org.codehaus.griffon.compile.core.MethodDescriptor;

import static org.codehaus.griffon.compile.core.MethodDescriptor.annotatedMethod;
import static org.codehaus.griffon.compile.core.MethodDescriptor.annotatedType;
import static org.codehaus.griffon.compile.core.MethodDescriptor.annotations;
import static org.codehaus.griffon.compile.core.MethodDescriptor.args;
import static org.codehaus.griffon.compile.core.MethodDescriptor.method;
import static org.codehaus.griffon.compile.core.MethodDescriptor.throwing;
import static org.codehaus.griffon.compile.core.MethodDescriptor.type;
import static org.codehaus.griffon.compile.core.MethodDescriptor.typeParams;
import static org.codehaus.griffon.compile.core.MethodDescriptor.types;

/**
 * @author Andres Almiray
 */
public interface WsliteAwareConstants extends BaseConstants {
    String WSLITE_HANDLER_TYPE = "griffon.plugins.wslite.WsliteHandler";
    String WSLITE_HANDLER_PROPERTY = "wsliteHandler";
    String WSLITE__HANDLER_FIELD_NAME = "this$" + WSLITE_HANDLER_PROPERTY;
    String REST_CLIENT_CALLBACK_TYPE = "griffon.plugins.wslite.RESTClientCallback";
    String SOAP_CLIENT_CALLBACK_TYPE = "griffon.plugins.wslite.SOAPClientCallback";
    String REST_EXCEPTION_TYPE = "griffon.plugins.wslite.exceptions.RESTException";
    String SOAP_EXCEPTION_TYPE = "griffon.plugins.wslite.exceptions.SOAPException";

    String METHOD_WITH_REST = "withRest";
    String METHOD_WITH_SOAP = "withSoap";
    String METHOD_DESTROY_REST_CLIENT = "destroyRestClient";
    String METHOD_DESTROY_SOAP_CLIENT = "destroySoapClient";

    String CALLBACK = "callback";
    String PARAMS = "params";

    MethodDescriptor[] METHODS = new MethodDescriptor[]{
        annotatedMethod(
            types(type(ANNOTATION_NULLABLE)),
            type(R),
            typeParams(R),
            METHOD_WITH_REST,
            args(
                annotatedType(annotations(ANNOTATION_NONNULL), JAVA_UTIL_MAP, JAVA_LANG_STRING, JAVA_LANG_OBJECT),
                annotatedType(annotations(ANNOTATION_NONNULL), REST_CLIENT_CALLBACK_TYPE, R)),
            throwing(type(REST_EXCEPTION_TYPE))
        ),

        annotatedMethod(
            types(type(ANNOTATION_NULLABLE)),
            type(R),
            typeParams(R),
            METHOD_WITH_SOAP,
            args(
                annotatedType(annotations(ANNOTATION_NONNULL), JAVA_UTIL_MAP, JAVA_LANG_STRING, JAVA_LANG_OBJECT),
                annotatedType(annotations(ANNOTATION_NONNULL), SOAP_CLIENT_CALLBACK_TYPE, R)),
            throwing(type(SOAP_EXCEPTION_TYPE))
        ),

        method(
            type(VOID),
            METHOD_DESTROY_REST_CLIENT,
            args(annotatedType(types(type(ANNOTATION_NONNULL)), JAVA_LANG_STRING))
        ),
        method(
            type(VOID),
            METHOD_DESTROY_SOAP_CLIENT,
            args(annotatedType(types(type(ANNOTATION_NONNULL)), JAVA_LANG_STRING))
        )
    };
}
