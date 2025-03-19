import ballerina/jballerina.java;

# Merges two datasets based on a common primary key, updating records from the first dataset with matching records from the second.
# ```ballerina
# type Customer record {int id; string name; int age;};
# type Customer1 record {int id; string name;};
# type Customer2 record {int id; int age;};
# 
# Customer1[] dataset1 = [{id: 1, name: "Alice"}, {id: 2, name: "Bob"}];
# Customer2[] dataset2 = [{id: 1, age: 25}, {id: 2, age: 30}];
# Customer[] mergedData = check etl:joinData(dataset1, dataset2, "id");
# ```
#
# + dataset1 - First dataset containing base records.
# + dataset2 - Second dataset with additional data to be merged.
# + primaryKey - The field used to match records between the datasets.
# + returnType - The type of the return value (Ballerina record).
# + return - A merged dataset with updated records or an `etl:Error`.
public function joinData(record {}[] dataset1, record {}[] dataset2, string primaryKey, typedesc<record {}> returnType = <>) returns returnType[]|Error = @java:Method {
    'class: "io.ballerina.stdlib.etl.nativeimpl.EtlEnrichment"
} external;

# Merges multiple datasets into a single dataset by flattening a nested array of records.
# ```ballerina
# type Customer record {
#    int id;
#    string name;
# };
# Customer[][] dataSets = [
#     [{id: 1, name: "Alice"}, {id: 2, name: "Bob"}],
#     [{id: 3, name: "Charlie"}, {id: 4, name: "David"}]
# ];
# Customer[] mergedData = check etl:mergeData(dataSets);
# ```
#
# + datasets - An array of datasets, where each dataset is an array of records.
# + returnType - The type of the return value (Ballerina record).
# + return - A single merged dataset containing all records or an `etl:Error`.
public function mergeData(record {}[][] datasets, typedesc<record {}> returnType = <>) returns returnType[]|Error = @java:Method {
    'class: "io.ballerina.stdlib.etl.nativeimpl.EtlEnrichment"
} external;

