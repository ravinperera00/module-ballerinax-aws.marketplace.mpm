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

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BString;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.marketplacemetering.MarketplaceMeteringClient;
import software.amazon.awssdk.services.marketplacemetering.model.BatchMeterUsageRequest;
import software.amazon.awssdk.services.marketplacemetering.model.BatchMeterUsageResponse;
import software.amazon.awssdk.services.marketplacemetering.model.ResolveCustomerRequest;
import software.amazon.awssdk.services.marketplacemetering.model.ResolveCustomerResponse;

import java.util.Objects;

/**
 * Representation of {@link software.amazon.awssdk.services.marketplacemetering.MarketplaceMeteringClient} with
 * utility methods to invoke as inter-op functions.
 */
public final class NativeClientAdaptor {
    private static final String NATIVE_CLIENT = "nativeClient";

    private NativeClientAdaptor() {
    }

    /**
     * Creates an AWS MPM native client with the provided configurations.
     *
     * @param bAwsMpmClient The Ballerina AWS MPM client object.
     * @param configurations AWS MPM client connection configurations.
     * @return A Ballerina `mpm:Error` if failed to initialize the native client with the provided configurations.
     */
    public static Object init(BObject bAwsMpmClient, BMap<BString, Object> configurations) {
        try {
            ConnectionConfig connectionConfig = new ConnectionConfig(configurations);
            AwsCredentials credentials = getCredentials(connectionConfig);
            AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
            MarketplaceMeteringClient nativeClient = MarketplaceMeteringClient.builder()
                    .credentialsProvider(credentialsProvider)
                    .region(connectionConfig.region()).build();
            bAwsMpmClient.addNativeData(NATIVE_CLIENT, nativeClient);
        } catch (Exception e) {
            String errorMsg = String.format("Error occurred while initializing the marketplace metering client: %s",
                    e.getMessage());
            return CommonUtils.createError(errorMsg, e);
        }
        return null;
    }

    private static AwsCredentials getCredentials(ConnectionConfig connectionConfig) {
        if (Objects.nonNull(connectionConfig.sessionToken())) {
            return AwsSessionCredentials.create(connectionConfig.accessKeyId(), connectionConfig.secretAccessKey(),
                    connectionConfig.sessionToken());
        } else {
            return AwsBasicCredentials.create(connectionConfig.accessKeyId(), connectionConfig.secretAccessKey());
        }
    }

    /**
     * Retrieves customer details mapped to a registration token.
     *
     * @param env The Ballerina runtime environment.
     * @param bAwsMpmClient The Ballerina AWS MPM client object.
     * @param registrationToken The registration-token provided by the customer.
     * @return A Ballerina `mpm:Error` if there was an error while executing the operation or else the AWS MPM
     *         resolve-customer response.
     */
    public static Object resolveCustomer(Environment env, BObject bAwsMpmClient, BString registrationToken) {
        MarketplaceMeteringClient nativeClient = (MarketplaceMeteringClient) bAwsMpmClient.getNativeData(NATIVE_CLIENT);
        return env.yieldAndRun(() -> {
            try {
                ResolveCustomerRequest resolveCustomerReq = ResolveCustomerRequest.builder()
                        .registrationToken(registrationToken.getValue()).build();
                ResolveCustomerResponse nativeResponse = nativeClient.resolveCustomer(resolveCustomerReq);
                BMap<BString, Object> bResponse = CommonUtils.getBResolveCustomerResponse(nativeResponse);
                return bResponse;
            } catch (Exception e) {
                String errorMsg = String.format("Error occurred while executing resolve customer operation: %s",
                        e.getMessage());
                BError bError = CommonUtils.createError(errorMsg, e);
                return bError;
            }
        });
    }

    /**
     * Retrieves the post-metering records for a set of customers.
     *
     * @param env The Ballerina runtime environment.
     * @param bAwsMpmClient The Ballerina AWS MPM client object.
     * @param request The Ballerina AWS MPM `BatchMeterUsage` request.
     * @return A Ballerina `mpm:Error` if there was an error while processing the request or else the AWS MPM
     *         batch-meter-usage response.
     */
    public static Object batchMeterUsage(Environment env, BObject bAwsMpmClient, BMap<BString, Object> request) {
        MarketplaceMeteringClient nativeClient = (MarketplaceMeteringClient) bAwsMpmClient.getNativeData(NATIVE_CLIENT);
        BatchMeterUsageRequest nativeRequest = CommonUtils.getNativeBatchMeterUsageRequest(request);
        return env.yieldAndRun(() -> {
            try {
                BatchMeterUsageResponse nativeResponse = nativeClient.batchMeterUsage(nativeRequest);
                BMap<BString, Object> bResponse = CommonUtils.getBBatchMeterUsageResponse(nativeResponse);
                return bResponse;
            } catch (Exception e) {
                String errorMsg = String.format("Error occurred while executing batch-meter-usage operation: %s",
                        e.getMessage());
                BError bError = CommonUtils.createError(errorMsg, e);
                return bError;
            }
        });
    }

    /**
     * Closes the AWS MPM client native resources.
     *
     * @param bAwsMpmClient The Ballerina AWS MPM client object.
     * @return A Ballerina `mpm:Error` if failed to close the underlying resources.
     */
    public static Object close(BObject bAwsMpmClient) {
        MarketplaceMeteringClient nativeClient = (MarketplaceMeteringClient) bAwsMpmClient.getNativeData(NATIVE_CLIENT);
        try {
            nativeClient.close();
        } catch (Exception e) {
            String errorMsg = String.format("Error occurred while closing the marketplace metering client: %s",
                    e.getMessage());
            return CommonUtils.createError(errorMsg, e);
        }
        return null;
    }
}
