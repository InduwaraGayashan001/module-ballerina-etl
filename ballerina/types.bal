// Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
//
// WSO2 LLC. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

# Represents the available comparison operations for the `filterDataByRelativeExp` function.
#
# + GREATER_THAN - Checks if the left operand is greater than the right operand.
# + LESS_THAN - Checks if the left operand is less than the right operand.
# + EQUAL - Checks if the left and right operands are equal.
# + NOT_EQUAL - Checks if the left and right operands are not equal.
# + GREATER_THAN_OR_EQUAL - Checks if the left operand is greater than or equal to the right operand.
# + LESS_THAN_OR_EQUAL - Checks if the left operand is less than or equal to the right operand.
public enum Operation {
    GREATER_THAN = ">",
    LESS_THAN = "<",
    EQUAL = "==",
    NOT_EQUAL = "!=",
    GREATER_THAN_OR_EQUAL = ">=",
    LESS_THAN_OR_EQUAL = "<="
}

# Represents the direction for the `sortData` function
#
# + ASCENDING - Sorts the data in ascending order.
# + DESCENDING - Sorts the data in descending order.
public enum SortDirection {
    ASCENDING = "ascending",
    DESCENDING = "descending"
}

# Represents the category ranges in the `filterDataByCategory` function.
# - `float` - Represents the minimum value.
# - `float[]` - Represents the intermediate breakpoints.
# - `float` - Represents the maximum value.
public type CategoryRanges [float, float[], float];
