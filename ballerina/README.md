## Overview

This package provides a collection of functions designed for data processing and manipulation, enabling seamless ETL workflows and supporting a variety of use cases.

The functions in this package are categorized into the following ETL process stages:
- Unstructured Data Extraction
- Data Cleaning
- Data Enrichment
- Data Security
- Data Filtering
- Data Categorization

## Features

### Unstructured Data Extraction
- `extractFromUnstructuredData`: Extracts relevant details from a string and maps them to a ballerina record.

### Data Cleaning
- `standardizeData`: Standardizes string values in a dataset based on approximate matches.
- `groupApproximateDuplicates`: Identifies approximate duplicates in a dataset and groups them, returning unique records separately.
- `removeEmptyValues`: Removes records that contain nil, empty parentheses, or empty string values in any field.
- `removeDuplicates`: Removes exact duplicate records from a dataset based on their content.
- `removeField`: Removes a specified field from each record in the dataset.
- `replaceText`: Replaces text in a specific field of a dataset using regular expressions.
- `sortData`: Sorts a dataset based on a specific field in ascending or descending order.
- `handleWhiteSpaces`: Cleans up whitespace in all fields of a dataset by replacing multiple spaces with a single space and trimming the values.

### Data Enrichment
- `joinData`: Merges two datasets based on a given key, updating records from the first dataset with matching records from the second.
- `mergeData`: Merges multiple datasets into a single dataset by flattening a nested array of records.

### Data Security
- `encryptData`: Encrypts a dataset using AES-ECB encryption with a given symmetric key.
- `decryptData`: Decrypts a dataset using AES-ECB decryption with a given symmetric key.
- `maskSensitiveData`: Masks specified fields of a dataset by replacing each character in the sensitive fields with a masking character.

### Data Filtering
- `filterDataByRatio`: Splits a dataset into two parts based on a given ratio.
- `filterDataByRegex`: Filters a dataset into two subsets based on a regex pattern match.
- `filterDataByRelativeExp`: Filters a dataset based on a relative numeric comparison expression.

### Data Categorization
- `categorizeNumeric`: Categorizes a dataset based on a numeric field and specified ranges.
- `categorizeRegexData`: Categorizes a dataset based on a string field using a set of regular expressions.
- `categorizeSemantic`: Categorizes a dataset based on a string field using semantic classification via OpenAI's GPT model.

## Usage

### Configurations

The following functions require an OpenAI API key to operate:
- `extractFromUnstructuredData`
- `groupApproximateDuplicates`
- `standardizeData`
- `maskSensitiveData`
- `categorizeSemantic`

If your Ballerina application uses any of these functions, follow the steps below before calling them:

1. Create an [OpenAI account](https://platform.openai.com) and obtain an [API key](https://platform.openai.com/account/api-keys).

2. Values need to be provided for the `modelConfig` configurable value. Add the relevant configuration in the `Config.toml` file as follows.

```toml
[ballerina.etl.modelConfig]
connectionConfig.auth.token = "<OPEN_AI_KEY>"
model = "<GPT_MODEL>"
```

- Replace `<OPEN_AI_KEY>` with the key you obtained, and `<GPT_MODEL>` with one of the supported GPT models listed below:
    - `"gpt-4-turbo"`
    - `"gpt-4o"`
- `"gpt-4o-mini"`

4. **(Optional)** If you want to increase the client timeout (the default is 60 seconds), set the `connectionConfig.auth.timeout` field as shown below:

```toml
connectionConfig.auth.timeout = <TIMEOUT_IN_SECONDS>
```

- Replace `<TIMEOUT_IN_SECONDS>` with your desired timeout duration. 

### Dependent Type Support 

All functions in this package support dependent types. Here is an example of how to use them:

```ballerina 
import ballerina/etl;
import ballerina/io;

type Customer record {|
    string name;
    string city;
|};

Customer[] dataset = [
    { name: "Alice", city: "New York" },
    { name: "Bob", city: "Los Angeles" },
    { name: "Alice", city: "New York" }
];

public function main() returns error? {
    Customer[] uniqueData = check etl:removeDuplicates(dataset);
    io:println(`Customer Data Without Duplicates : ${uniqueData}`);
}
```
