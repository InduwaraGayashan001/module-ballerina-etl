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

import ballerina/test;

type ReviewSummary record {|
    string[] goodPoints;
    string[] badPoints;
    string[] improvements;
|};

@test:Config {}
public function testExtractFromUnstructuredData() returns error? {
    string reviews = "The smartphone has an impressive camera and smooth performance, making it great for photography and gaming. However, the battery drains quickly, and the charging speed could be improved. The UI is intuitive, but some features feel outdated and need a refresh.";
    string[] fields = ["goodPoints", "badPoints", "improvements"];
    ReviewSummary extractedDetails = check extractFromUnstructuredData(reviews, fields);
    test:assertNotEquals(extractedDetails["goodPoints"], null);
    test:assertNotEquals(extractedDetails["badPoints"], null);
    test:assertNotEquals(extractedDetails["improvements"], null);
}
