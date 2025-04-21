/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 * 
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.stdlib.etl.utils;

/**
 * Constants for ETL operations.
 */
public class Constants {

    public static final String IDLE_TIMEOUT_ERROR = "IdleTimeoutError";
    public static final String CLIENT_CONNECTOR_ERROR = "ClientConnectorError";
    public static final String CLIENT_REQUEST_ERROR = "ClientRequestError";
    public static final String STRING = "string";
    public static final String INT = "int";
    public static final String FLOAT = "float";
    public static final String INT_OR_FLOAT = "int or float";
    public static final String REGEX_MULTIPLE_WHITESPACE = "\\s+";
    public static final String SINGLE_WHITESPACE = " ";
    public static final String GROUP_APPROXIMATE_DUPLICATES = "groupApproximateDuplicatesFunc";
    public static final String STANDARDIZE_DATA = "standardizeDataFunc";
    public static final String CATEGORIZE_SEMANTIC = "categorizeSemanticFunc";
    public static final String EXTRACT_FROM_UNSTRUCTURED_DATA = "extractFromUnstructuredDataFunc";
    public static final String GET_UNIQUE_DATA = "getUniqueData";
    public static final String ASCENDING = "ascending";
}
