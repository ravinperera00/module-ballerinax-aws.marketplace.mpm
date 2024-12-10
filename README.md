# Ballerina AWS Marketplace Metering connector

[![Build](https://github.com/ballerina-platform/module-ballerinax-aws.marketplace.mpm/actions/workflows/ci.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-aws.marketplace.mpm/actions/workflows/ci.yml)
[![Trivy](https://github.com/ballerina-platform/module-ballerinax-aws.marketplace.mpm/actions/workflows/trivy-scan.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-aws.marketplace.mpm/actions/workflows/trivy-scan.yml)
[![GraalVM Check](https://github.com/ballerina-platform/module-ballerinax-aws.marketplace.mpm/actions/workflows/build-with-bal-test-graalvm.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-aws.marketplace.mpm/actions/workflows/build-with-bal-test-graalvm.yml)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/ballerina-platform/module-ballerinax-aws.marketplace.mpm.svg)](https://github.com/ballerina-platform/module-ballerinax-aws.marketplace.mpm/commits/main)
[![GitHub Issues](https://img.shields.io/github/issues/ballerina-platform/ballerina-library/module/aws.marketplace.mpm.svg?label=Open%20Issues)](https://github.com/ballerina-platform/ballerina-library/labels/module%2Faws.marketplace.mpm)

[AWS Marketplace Metering Service](https://docs.aws.amazon.com/marketplacemetering/latest/APIReference/Welcome.html) is
a usage and billing service that allows AWS Marketplace sellers to report the usage of their products for
billing purposes. This service supports both software-as-a-service (SaaS) products and metering products sold through
AWS Marketplace.

The `ballerinax/aws.marketplace.mpm` package provides APIs to interact with the AWS Marketplace Metering Service,
enabling developers to submit usage records, batch meter usage data, and manage metering-related tasks programmatically.

## Setup guide
Before using this connector in your Ballerina application, complete the following:
1. Create an [AWS account](https://portal.aws.amazon.com/billing/signup?nc2=h_ct&src=default&redirect_url=https%3A%2F%2Faws.amazon.com%2Fregistration-confirmation#/start)
2. [Obtain tokens](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html)

## Quickstart

To use the `aws.marketplace.mpm` connector in your Ballerina project, modify the `.bal` file as follows:

### Step 1: Import the module

Import the `ballerinax/aws.marketplace.mpm` module into your Ballerina project.

```ballerina
import ballerinax/aws.marketplace.mpm;
```

### Step 2: Instantiate a new connector

Create a new `mpm:Client` by providing the access key ID, secret access key, and the region.

```ballerina
configurable string accessKeyId = ?;
configurable string secretAccessKey = ?;

mpm:Client mpm = check new(region = mpm:US_EAST_1, auth = {
    accessKeyId,
    secretAccessKey
});
```

### Step 3: Invoke the connector operation

Now, utilize the available connector operations.

```ballerina
mpm:ResolveCustomerResponse response = check mpm->resolveCustomer("<registration-token>");
```

### Step 4: Run the Ballerina application

Use the following command to compile and run the Ballerina program.

```bash
bal run
```

## Build from the source

### Setting up the prerequisites

1. Download and install Java SE Development Kit (JDK) version 21. You can download it from either of the following sources:

    * [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
    * [OpenJDK](https://adoptium.net/)

   > **Note:** After installation, remember to set the `JAVA_HOME` environment variable to the directory where JDK was installed.

2. Download and install [Ballerina Swan Lake](https://ballerina.io/).

3. Download and install [Docker](https://www.docker.com/get-started).

   > **Note**: Ensure that the Docker daemon is running before executing any tests.

4. Export Github Personal access token with read package permissions as follows,

    ```bash
    export packageUser=<Username>
    export packagePAT=<Personal access token>
    ```

### Build options

Execute the commands below to build from the source.

1. To build the package:

   ```bash
   ./gradlew clean build
   ```

2. To run the tests:

   ```bash
   ./gradlew clean test
   ```

3. To build the without the tests:

   ```bash
   ./gradlew clean build -x test
   ```

4. To run tests against different environments:

   ```bash
   ./gradlew clean test -Pgroups=<Comma separated groups/test cases>
   ```

5. To debug the package with a remote debugger:

   ```bash
   ./gradlew clean build -Pdebug=<port>
   ```

6. To debug with the Ballerina language:

   ```bash
   ./gradlew clean build -PbalJavaDebug=<port>
   ```

7. Publish the generated artifacts to the local Ballerina Central repository:

    ```bash
    ./gradlew clean build -PpublishToLocalCentral=true
    ```

8. Publish the generated artifacts to the Ballerina Central repository:

   ```bash
   ./gradlew clean build -PpublishToCentral=true
   ```

## Contribute to Ballerina

As an open-source project, Ballerina welcomes contributions from the community.

For more information, go to the [contribution guidelines](https://github.com/ballerina-platform/ballerina-lang/blob/master/CONTRIBUTING.md).

## Code of conduct

All the contributors are encouraged to read the [Ballerina Code of Conduct](https://ballerina.io/code-of-conduct).

## Useful links

* For more information go to the [`aws.marketplace.mpm` package](https://central.ballerina.io/ballerinax/aws.marketplace.mpm/latest).
* For example demonstrations of the usage, go to [Ballerina By Examples](https://ballerina.io/learn/by-example/).
* Chat live with us via our [Discord server](https://discord.gg/ballerinalang).
* Post all technical questions on Stack Overflow with the [#ballerina](https://stackoverflow.com/questions/tagged/ballerina) tag.