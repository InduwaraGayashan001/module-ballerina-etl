/*
 * Copyright (c) 2025, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.stdlib.etl.utils;

import io.ballerina.runtime.api.creators.ErrorCreator;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BString;

/**
 * Represents the errors of ETL module.
 */

public class ErrorUtils {

    private ErrorUtils() {
    }

    /**
     * Creates an error message.
     *
     * @param errorMsg the error message
     * @return an error which will be propagated to ballerina user
     */
    public static BError createError(String errorMsg) {
        return ErrorCreator.createError(StringUtils.fromString(errorMsg));
    }

    public static BError createFieldNotFoundError(BString fieldName) {
        return ErrorCreator
                .createError(StringUtils.fromString("The dataset does not contain the field - " + fieldName));
    }

    public static BError createClientConnectionError() {
        return ErrorCreator.createError(StringUtils.fromString("Operation failed due to client connection error"));
    }
}
