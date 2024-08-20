// Copyright (c) 2024 WSO2 LLC. (http://www.wso2.com).
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

import ballerina/constraint;
import ballerina/time;

# Represents the Client configurations for AWS Marketplace Metering service.
public type ConnectionConfig record {|
    # The AWS region with which the connector should communicate
    Region region;
    # The authentication configurations for the AWS Marketplace Metering service
    AuthConfig auth;
|};

# An Amazon Web Services region that hosts a set of Amazon services.
public enum Region {
    AF_SOUTH_1 = "af-south-1",
    AP_EAST_1 = "ap-east-1",
    AP_NORTHEAST_1 = "ap-northeast-1",
    AP_NORTHEAST_2 = "ap-northeast-2",
    AP_NORTHEAST_3 = "ap-northeast-3",
    AP_SOUTH_1 = "ap-south-1",
    AP_SOUTH_2 = "ap-south-2",
    AP_SOUTHEAST_1 = "ap-southeast-1",
    AP_SOUTHEAST_2 = "ap-southeast-2",
    AP_SOUTHEAST_3 = "ap-southeast-3",
    AP_SOUTHEAST_4 = "ap-southeast-4",
    AWS_CN_GLOBAL = "aws-cn-global",
    AWS_GLOBAL = "aws-global",
    AWS_ISO_GLOBAL = "aws-iso-global",
    AWS_ISO_B_GLOBAL = "aws-iso-b-global",
    AWS_US_GOV_GLOBAL = "aws-us-gov-global",
    CA_WEST_1 = "ca-west-1",
    CA_CENTRAL_1 = "ca-central-1",
    CN_NORTH_1 = "cn-north-1",
    CN_NORTHWEST_1 = "cn-northwest-1",
    EU_CENTRAL_1 = "eu-central-1",
    EU_CENTRAL_2 = "eu-central-2",
    EU_ISOE_WEST_1 = "eu-isoe-west-1",
    EU_NORTH_1 = "eu-north-1",
    EU_SOUTH_1 = "eu-south-1",
    EU_SOUTH_2 = "eu-south-2",
    EU_WEST_1 = "eu-west-1",
    EU_WEST_2 = "eu-west-2",
    EU_WEST_3 = "eu-west-3",
    IL_CENTRAL_1 = "il-central-1",
    ME_CENTRAL_1 = "me-central-1",
    ME_SOUTH_1 = "me-south-1",
    SA_EAST_1 = "sa-east-1",
    US_EAST_1 = "us-east-1",
    US_EAST_2 = "us-east-2",
    US_GOV_EAST_1 = "us-gov-east-1",
    US_GOV_WEST_1 = "us-gov-west-1",
    US_ISOB_EAST_1 = "us-isob-east-1",
    US_ISO_EAST_1 = "us-iso-east-1",
    US_ISO_WEST_1 = "us-iso-west-1",
    US_WEST_1 = "us-west-1",
    US_WEST_2 = "us-west-2"
}

# Represents the Authentication configurations for AWS Marketplace Metering service.
public type AuthConfig record {|
    # The AWS access key, used to identify the user interacting with AWS
    string accessKeyId;
    # The AWS secret access key, used to authenticate the user interacting with AWS
    string secretAccessKey;
    # The AWS session token, retrieved from an AWS token service, used for authenticating 
    # a user with temporary permission to a resource
    string sessionToken?;
|};

# Represents the result retrieved from `ResolveCustomer` operation.
public type ResolveCustomerResponse record {|
    # The AWS account ID associated with the Customer identifier for the individual customer
    string customerAWSAccountId;
    # The unique identifier used to identify an individual customer
    string customerIdentifier;
    # The unique identifier for the Marketplace product
    string productCode;
|};

# Represents the parameters used for `BatchMeterUsage` operation.
public type BatchMeterUsageRequest record {|
    # The unique identifier for the Marketplace product
    @constraint:String {
        pattern: re `^[-a-zA-Z0-9/=:_.@]{1,255}$`
    }
    string productCode;
    # The set of usage records. Each usage record provides information about an instance of product usage. 
    @constraint:Array {
        maxLength: 25
    }
    UsageRecord[] usageRecords = [];
|};

# Represents the details of the quantity of usage for a given product.
public type UsageRecord record {|
    # The unique identifier used to identify an individual customer
    @constraint:String {
        pattern: re `[\s\S]{1,255}$`
    }
    string customerIdentifier;
    # The dimension for which the usage is being reported
    @constraint:String {
        pattern: re `[\s\S]{1,255}$`
    }
    string dimension;
    # The timestamp when the usage occurred (in UTC)
    time:Utc timestamp;
    # The quantity of usage consumed
    @constraint:Int {
        minValue: 0,
        maxValue: 2147483647
    }
    int quantity?;
    # The list of usage allocations
    @constraint:Array {
        minLength: 1,
        maxLength: 2500
    }
    UsageAllocation[] usageAllocations?;
|};

# Represents a usage allocation for AWS Marketplace metering.
public type UsageAllocation record {|
    # The total quantity allocated to this bucket of usage
    @constraint:Int {
        minValue: 0,
        maxValue: 2147483647
    }
    int allocatedUsageQuantity;
    # The set of tags that define the bucket of usage
    @constraint:Array {
        minLength: 1,
        maxLength: 5
    }
    Tag[] tags?;
|};

# Represents the metadata assigned to a usage allocation.
public type Tag record {|
    # The label that acts as the category for the specific tag values
    @constraint:String {
        pattern: re `^[a-zA-Z0-9+ -=._:\\/@]{1,100}$`
    }
    string 'key;
    # The descriptor within a tag category (key)
    @constraint:String {
        pattern: re `^[a-zA-Z0-9+ -=._:\\/@]{1,256}$`
    }
    string value;
|};

# Represents the result retrieved from `BatchMeterUsage` operation.
public type BatchMeterUsageResponse record {|
    # The list of all the `UsageRecord` instances successfully processed
    UsageRecordResult[] results;
    # The list of all the `UsageRecord` instances which were not processed
    UsageRecord[] unprocessedRecords;
|};

# Represents the details regarding the status of a given `UsageRecord` processed by `BatchMeterUsage` operation. 
public type UsageRecordResult record {|
    # The unique identifier for this metering event
    string meteringRecordId?;
    # The status of the individual `UsageRecord` processed by the `BatchMeterUsage` operation
    UsageRecordStatus status?;
    # The `UsageRecord` which was part of the `BatchMeterUsage` request
    UsageRecord usageRecord?;
|};

# Represents the possible status of a `UsageRecord` 
public enum UsageRecordStatus {
    # The `UsageRecord` was accepted by the `BatchMeterUsage` operation
    SUCCESS = "Success",
    # The provided customer identifier in the `BatchMeterUsage` request, is not able to use your product
    CUSTOMER_NOT_SUBSCRIBED = "CustomerNotSubscribed",
    # The provided `UsageRecord` matches a previously metered `UsageRecord` in terms of customer, dimension, and time
    DUPLICATE_RECORD = "DuplicateRecord"
}

