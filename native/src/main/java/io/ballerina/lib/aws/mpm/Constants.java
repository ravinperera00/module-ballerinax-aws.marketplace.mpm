/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com)
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
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

package io.ballerina.lib.aws.mpm;

import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BString;

/**
 * Represents the constants related to Ballerina MPE connector.
 */
public interface Constants {
    // Constants related to MPM `ResolveCustomerResponse`
    String MPM_RESOLVE_CUSTOMER = "ResolveCustomerResponse";
    BString MPM_RESOLVE_CUSTOMER_AWS_ACNT_ID = StringUtils.fromString("customerAWSAccountId");
    BString MPM_RESOLVE_CUSTOMER_IDNFR = StringUtils.fromString("customerIdentifier");
    BString MPM_RESOLVE_CUSTOMER_PRODUCT_CODE = StringUtils.fromString("productCode");

    // Constants related to MPE Error
    String MPM_ERROR = "Error";
    String MPM_ERROR_DETAILS = "ErrorDetails";
    BString MPM_ERROR_DETAILS_HTTP_STATUS_CODE = StringUtils.fromString("httpStatusCode");
    BString MPM_ERROR_DETAILS_HTTP_STATUS_TXT = StringUtils.fromString("httpStatusText");
    BString MPM_ERROR_DETAILS_ERR_CODE = StringUtils.fromString("errorCode");
    BString MPM_ERROR_DETAILS_ERR_MSG = StringUtils.fromString("errorMessage");
}
