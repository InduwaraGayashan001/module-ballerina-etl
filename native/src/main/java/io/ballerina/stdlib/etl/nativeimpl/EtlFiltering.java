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
        BArray filteredDataset = initializeNestedBArray(returnType, 2);
        for (int i = 0; i < dataset.size(); i++) {
            BMap<BString, Object> data = (BMap<BString, Object>) dataset.get(i);
            if (!data.containsKey(fieldName)) {
                return ErrorUtils.createError("The dataset does not contain the field - " + fieldName);
            }
            if (data.get(fieldName) == null) {
                continue;
            }
            String fieldvalue = data.get(fieldName).toString();
            if (fieldvalue.matches(regexPattern.toString())) {
                ((BArray) filteredDataset.get(0)).append(data);
            } else {
                ((BArray) filteredDataset.get(1)).append(data);
            }
        }
        return filteredDataset;
    }

    public static Object filterDataByRelativeExp(BArray dataset, BString fieldName, BString operation, float value,
            BTypedesc returnType) {
        BArray filteredDataset = initializeNestedBArray(returnType, 2);
        for (int i = 0; i < dataset.size(); i++) {
            BMap<BString, Object> data = (BMap<BString, Object>) dataset.get(i);
            if (!data.containsKey(fieldName)) {
                return ErrorUtils.createFieldNotFoundError(fieldName);
            }
            if (data.get(fieldName) == null) {
                continue;
            }
            float fieldValue = Float.parseFloat(data.get(fieldName).toString());
            if (evaluateCondition(fieldValue, value, operation.toString())) {
                ((BArray) filteredDataset.get(0)).append(data);
            } else {
                ((BArray) filteredDataset.get(1)).append(data);
            }
        }
        return filteredDataset;
    }
}
