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

package io.ballerina.stdlib.etl.nativeimpl;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.utils.TypeUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BIterator;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BRegexpValue;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTypedesc;
import io.ballerina.stdlib.etl.utils.ErrorUtils;

import static io.ballerina.stdlib.etl.utils.CommonUtils.convertJSONToBArray;
import static io.ballerina.stdlib.etl.utils.CommonUtils.initializeNestedBArray;
import static io.ballerina.stdlib.etl.utils.Constants.CATEGORIZE_SEMANTIC;
import static io.ballerina.stdlib.etl.utils.Constants.CLIENT_CONNECTION_ERROR;
import static io.ballerina.stdlib.etl.utils.Constants.IDLE_TIMEOUT_ERROR;

/**
 * This class hold Java external functions for ETL - data categorization APIs.
 *
 * * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class EtlCategorization {

    public static Object categorizeNumeric(BArray dataset, BString fieldName, BArray rangeArray, BTypedesc returnType) {
        BArray categorizedData = initializeNestedBArray(returnType, rangeArray.size() + 1);
        boolean isFieldExist = false;
        BIterator<?> iterator = dataset.getIterator();
        while (iterator.hasNext()) {
            BMap<BString, Object> data = (BMap<BString, Object>) iterator.next();
            if (!data.containsKey(fieldName)) {
                ((BArray) categorizedData.get(rangeArray.size())).append(data);
                continue;
            }
            float fieldValue = Float.parseFloat(String.valueOf(data.get(fieldName)));
            boolean isMatched = false;
            for (int j = 0; j < rangeArray.size(); j++) {
                BArray range = (BArray) rangeArray.get(j);
                float lowerBound = Float.parseFloat(String.valueOf(range.get(0)));
                float upperBound = Float.parseFloat(String.valueOf(range.get(1)));
                if (fieldValue >= lowerBound && fieldValue <= upperBound) {
                    ((BArray) categorizedData.get(j)).append(data);
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                ((BArray) categorizedData.get(rangeArray.size())).append(data);
            }
            isFieldExist = true;
        }
        return isFieldExist ? categorizedData : ErrorUtils.createFieldNotFoundError(fieldName);
    }

    public static Object categorizeRegex(BArray dataset, BString fieldName, BArray regexArray, BTypedesc returnType) {
        BArray categorizedData = initializeNestedBArray(returnType, regexArray.size() + 1);
        boolean isFieldExist = false;
        BIterator<?> iterator = dataset.getIterator();
        while (iterator.hasNext()) {
            BMap<BString, Object> data = (BMap<BString, Object>) iterator.next();
            if (!data.containsKey(fieldName)) {
                ((BArray) categorizedData.get(regexArray.size())).append(data);
                continue;
            }
            String fieldValue = String.valueOf(data.get(fieldName));
            boolean isMatched = false;
            for (int j = 0; j < regexArray.size(); j++) {
                BRegexpValue regexPattern = (BRegexpValue) regexArray.get(j);
                if (fieldValue.matches(regexPattern.toString())) {
                    ((BArray) categorizedData.get(j)).append(data);
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                ((BArray) categorizedData.get(regexArray.size())).append(data);
            }
            isFieldExist = true;
        }
        return isFieldExist ? categorizedData : ErrorUtils.createFieldNotFoundError(fieldName);

    }

    public static Object categorizeSemantic(Environment env, BArray dataset, BString fieldName, BArray categories,
            BString modelName, BTypedesc returnType) {
        boolean isFieldExist = false;
        for (int i = 0; i < dataset.size(); i++) {
            BMap<BString, Object> data = (BMap<BString, Object>) dataset.get(i);
            if (data.containsKey(fieldName)) {
                isFieldExist = true;
            }
        }
        if (!isFieldExist) {
            return ErrorUtils.createFieldNotFoundError(fieldName);
        }
        Object[] args = new Object[] { dataset, fieldName, categories, modelName, returnType };
        Object clientResponse = env.getRuntime().callFunction(env.getCurrentModule(), CATEGORIZE_SEMANTIC, null,
                args);
        switch (TypeUtils.getType(clientResponse).getName()) {
            case CLIENT_CONNECTION_ERROR:
                return ErrorUtils.createClientConnectionError();
            case IDLE_TIMEOUT_ERROR:
                return ErrorUtils.createIdleTimeoutError();
            default:
                return convertJSONToBArray(clientResponse, returnType);
        }
    }
}
