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

import io.ballerina.runtime.api.creators.ErrorCreator;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

import java.util.Objects;

/**
 * {@code CommonUtils} contains the common utility functions for the Ballerina AWS MPM connector.
 */
public final class CommonUtils {

    private CommonUtils() {
    }

    public static BError createError(String message, Throwable exception) {
        BError cause = ErrorCreator.createError(exception);
        BMap<BString, Object> errorDetails = ValueCreator.createRecordValue(
                ModuleUtils.getModule(), Constants.MPM_ERROR_DETAILS);
        if (exception instanceof AwsServiceException awsSvcExp && Objects.nonNull(awsSvcExp.awsErrorDetails())) {
            AwsErrorDetails awsErrorDetails = awsSvcExp.awsErrorDetails();
            if (Objects.nonNull(awsErrorDetails.sdkHttpResponse())) {
                errorDetails.put(
                        Constants.MPM_ERROR_DETAILS_HTTP_STATUS_CODE, awsErrorDetails.sdkHttpResponse().statusCode());
                awsErrorDetails.sdkHttpResponse().statusText().ifPresent(httpStatusTxt -> errorDetails.put(
                        Constants.MPM_ERROR_DETAILS_HTTP_STATUS_TXT, StringUtils.fromString(httpStatusTxt)));
            }
            errorDetails.put(
                    Constants.MPM_ERROR_DETAILS_ERR_CODE, StringUtils.fromString(awsErrorDetails.errorCode()));
            errorDetails.put(
                    Constants.MPM_ERROR_DETAILS_ERR_MSG, StringUtils.fromString(awsErrorDetails.errorMessage()));
        }
        return ErrorCreator.createError(
                ModuleUtils.getModule(), Constants.MPM_ERROR, StringUtils.fromString(message), cause, errorDetails);
    }
}
