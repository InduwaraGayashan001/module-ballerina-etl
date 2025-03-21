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

package io.ballerina.stdlib.etl.nativeimpl;

import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BRegexpValue;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTypedesc;
import io.ballerina.stdlib.etl.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.Collections;

import static io.ballerina.stdlib.etl.utils.CommonUtils.evaluateCondition;
import static io.ballerina.stdlib.etl.utils.CommonUtils.initializeNestedBArray;

/**
 * This class hold Java external functions for ETL - data filtering APIs.
 *
 * * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class EtlFiltering {

    public static Object filterDataByRatio(BArray dataset, float ratio, BTypedesc returnType) {

        BArray filteredDataset = initializeNestedBArray(returnType, 2);
        ArrayList<Object> suffledDataset = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++) {
            suffledDataset.add(dataset.get(i));
        }
        Collections.shuffle(suffledDataset);

        int splitIndex = (int) (suffledDataset.size() * ratio);
        for (int i = 0; i < suffledDataset.size(); i++) {
            if (i < splitIndex) {
                ((BArray) filteredDataset.get(0)).append(suffledDataset.get(i));
            } else {
                ((BArray) filteredDataset.get(1)).append(suffledDataset.get(i));
            }
        }
        return filteredDataset;
    }

    public static Object filterDataByRegex(BArray dataset, BString fieldName, BRegexpValue regexPattern,
            BTypedesc returnType) {
        boolean isFieldExist = false;
        BArray filteredDataset = initializeNestedBArray(returnType, 2);
        for (int i = 0; i < dataset.size(); i++) {
            BMap<BString, Object> data = (BMap<BString, Object>) dataset.get(i);
            if (data.containsKey(fieldName)) {
                String fieldvalue = data.get(fieldName).toString();
                if (fieldvalue.matches(regexPattern.toString())) {
                    ((BArray) filteredDataset.get(0)).append(data);
                } else {
                    ((BArray) filteredDataset.get(1)).append(data);
                }
                isFieldExist = true;
            } else {
                ((BArray) filteredDataset.get(1)).append(data);
            }
        }
        if (!isFieldExist) {
            return ErrorUtils.createFieldNotFoundError(fieldName);
        }
        return filteredDataset;
    }

    public static Object filterDataByRelativeExp(BArray dataset, BString fieldName, BString operation, float value,
            BTypedesc returnType) {
        boolean isFieldExist = false;
        BArray filteredDataset = initializeNestedBArray(returnType, 2);
        for (int i = 0; i < dataset.size(); i++) {
            BMap<BString, Object> data = (BMap<BString, Object>) dataset.get(i);
            if (data.containsKey(fieldName)) {
                float fieldValue = Float.parseFloat(data.get(fieldName).toString());
                if (evaluateCondition(fieldValue, value, operation.toString())) {
                    ((BArray) filteredDataset.get(0)).append(data);
                } else {
                    ((BArray) filteredDataset.get(1)).append(data);
                }
                isFieldExist = true;
            } else {
                ((BArray) filteredDataset.get(1)).append(data);
            }
        }
        if (!isFieldExist) {
            return ErrorUtils.createFieldNotFoundError(fieldName);
        }
        return filteredDataset;
    }
}
