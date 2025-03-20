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

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTypedesc;
import io.ballerina.stdlib.etl.utils.ErrorUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import static io.ballerina.stdlib.etl.utils.CommonUtils.contains;
import static io.ballerina.stdlib.etl.utils.CommonUtils.convertJSONToBArray;
import static io.ballerina.stdlib.etl.utils.CommonUtils.initializeBArray;
import static io.ballerina.stdlib.etl.utils.CommonUtils.initializeBMap;

/**
 * This class hold Java external functions for ETL - data security APIs.
 *
 * * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class EtlSecurity {

    public static Object encryptData(BArray dataset, BArray fieldNames, BString key, BTypedesc returnType) {
        BArray encryptedDataset = initializeBArray(returnType);
        byte[] encryptKey = Base64.getDecoder().decode(key.getValue());
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey, "AES"));
        } catch (Exception e) {
            return ErrorUtils.createError(e.getMessage());
        }
        for (int i = 0; i < dataset.size(); i++) {
            BMap<BString, Object> data = (BMap<BString, Object>) dataset.get(i);
            BMap<BString, Object> encryptedData = initializeBMap(returnType);
            for (int j = 0; j < fieldNames.size(); j++) {
                BString fieldName = (BString) fieldNames.get(j);
                if (!data.containsKey(fieldName)) {
                    return ErrorUtils.createFieldNotFoundError(fieldName);
                }
            }
            BString[] keys = data.getKeys();
            for (BString keyField : keys) {
                if (contains(fieldNames, keyField)) {
                    String value = data.get(keyField).toString();
                    byte[] encryptedBytes = null;
                    try {
                        encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
                    } catch (IllegalBlockSizeException e) {
                        return ErrorUtils.createError(e.getMessage());
                    } catch (BadPaddingException e) {
                        return ErrorUtils.createError(e.getMessage());
                    }
                    String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);
                    encryptedData.put(keyField, StringUtils.fromString(encryptedBase64));
                } else {
                    encryptedData.put(keyField, data.get(keyField));
                }
            }
            encryptedDataset.append(encryptedData);
        }
        return encryptedDataset;
    }

    public static Object decryptData(BArray dataset, BArray fieldNames, BString key, BTypedesc returnType) {
        BArray decryptedDataset = initializeBArray(returnType);
        byte[] decryptKey = Base64.getDecoder().decode(key.getValue());

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey, "AES"));
        } catch (Exception e) {
            return ErrorUtils.createError(e.getMessage());
        }
        for (int i = 0; i < dataset.size(); i++) {
            BMap<BString, Object> data = (BMap<BString, Object>) dataset.get(i);
            BMap<BString, Object> decryptedData = initializeBMap(returnType);
            for (int j = 0; j < fieldNames.size(); j++) {
                BString fieldName = (BString) fieldNames.get(j);
                if (!data.containsKey(fieldName)) {
                    return ErrorUtils.createFieldNotFoundError(fieldName);
                }
            }
            BString[] keys = data.getKeys();
            for (BString keyField : keys) {
                if (contains(fieldNames, keyField)) {
                    String value = data.get(keyField).toString();
                    byte[] decryptedBytes = null;
                    try {
                        decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(value));
                    } catch (IllegalBlockSizeException e) {
                        return ErrorUtils.createError(e.getMessage());
                    } catch (BadPaddingException e) {
                        return ErrorUtils.createError(e.getMessage());
                    }
                    String decryptedValue = new String(decryptedBytes, StandardCharsets.UTF_8);
                    decryptedData.put(keyField, StringUtils.fromString(decryptedValue));
                } else {
                    decryptedData.put(keyField, data.get(keyField));
                }
            }
            decryptedDataset.append(decryptedData);
        }
        return decryptedDataset;
    }

    public static Object maskSensitiveData(Environment env, BArray dataset, BString maskCharacter, BString modelName,
            BTypedesc returnType) {
            Object[] args = new Object[] { dataset, maskCharacter, modelName, returnType };
            Object result = env.getRuntime().callFunction(env.getCurrentModule(), "maskSensitiveDataFunc", null,
                    args);
            return convertJSONToBArray(result, returnType);
    }

}
