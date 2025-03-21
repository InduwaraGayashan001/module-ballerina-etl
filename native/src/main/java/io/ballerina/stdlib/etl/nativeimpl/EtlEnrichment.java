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
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTypedesc;
import io.ballerina.stdlib.etl.utils.ErrorUtils;

import static io.ballerina.stdlib.etl.utils.CommonUtils.initializeBArray;
import static io.ballerina.stdlib.etl.utils.CommonUtils.initializeBMap;

/**
 * This class hold Java external functions for ETL - data enrichment APIs.
 *
 * * @since 1.0.0
 */

@SuppressWarnings("unchecked")
public class EtlEnrichment {

    public static Object joinData(BArray dataset1, BArray dataset2, BString primaryKey, BTypedesc returnType) {

        BArray joinedDataset = initializeBArray(returnType);
        boolean isFieldExistForDataset1 = false;
        for (int i = 0; i < dataset1.size(); i++) {
            BMap<BString, Object> data1 = (BMap<BString, Object>) dataset1.get(i);
            if (data1.containsKey(primaryKey)) {
                isFieldExistForDataset1 = true;
                boolean isFieldExistForDataset2 = false;
                for (int j = 0; j < dataset2.size(); j++) {
                    BMap<BString, Object> data2 = (BMap<BString, Object>) dataset2.get(j);
                    if (data2.containsKey(primaryKey)) {
                        isFieldExistForDataset2 = true;
                        if (data1.get(primaryKey).equals(data2.get(primaryKey))) {
                            BMap<BString, Object> newData = initializeBMap(returnType);
                            for (BString key : data1.getKeys()) {
                                newData.put(key, data1.get(key));
                            }
                            for (BString key : data2.getKeys()) {
                                newData.put(key, data2.get(key));
                            }
                            joinedDataset.append(newData);
                        }
                    }  
                }
                if (!isFieldExistForDataset2) {
                    return ErrorUtils.createError("Primary key field does not exist in dataset2");
                }
            }
        }
        if (!isFieldExistForDataset1) {
            return ErrorUtils.createError("Primary key field does not exist in dataset1");
        }
        if (joinedDataset.size() == 0) {
            return ErrorUtils.createError("No matching records found");
        }
        return joinedDataset;
    }

    public static Object mergeData(BArray datasets, BTypedesc returnType) {

        BArray mergedDataset = initializeBArray(returnType);
    
        for (int i = 0; i < datasets.size(); i++) {
            BArray dataset = (BArray) datasets.get(i);
            for (int j = 0; j < dataset.size(); j++) {
                mergedDataset.append(dataset.get(j));
            }
        }

        return mergedDataset;
    }
}
