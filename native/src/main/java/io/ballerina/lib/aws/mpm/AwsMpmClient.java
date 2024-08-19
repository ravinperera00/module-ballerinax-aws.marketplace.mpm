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

import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BString;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.marketplacemetering.MarketplaceMeteringClient;

import java.util.Objects;

public final class AwsMpmClient {
    private static final String NATIVE_CLIENT = "nativeClient";

    private AwsMpmClient() {
    }

    /**
     * Creates an AWS MPM native client with the provided configurations.
     *
     * @param bAwsMpmClient The Ballerina AWS MPM client object.
     * @param configurations AWS MPE client connection configurations.
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
     * Closes the AWS MPM client native resources.
     *
     * @param bAwsMpeClient The Ballerina AWS MPE client object.
     * @return A Ballerina `mpm:Error` if failed to close the underlying resources.
     */
    public static Object close(BObject bAwsMpeClient) {
        MarketplaceMeteringClient nativeClient = (MarketplaceMeteringClient) bAwsMpeClient
                .getNativeData(NATIVE_CLIENT);
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
