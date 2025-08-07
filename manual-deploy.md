# Manual Deployment Guide

This guide provides step-by-step instructions for manually deploying the Survey Handler Lambda.

## Prerequisites

1. AWS CLI configured with appropriate permissions
2. Java 17+ installed
3. Maven 3.8+ installed
4. Docker installed (for native builds)

## Step 1: Setup S3 Bucket

```bash
./setup-s3.sh
```

## Step 2: Build the Application

### Option A: Native Build (Recommended for production)
```bash
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
```

### Option B: JVM Build (Faster for development)
```bash
./mvnw clean package
```

## Step 3: Create Deployment Package

For native build:
```bash
cd target
cp *-runner bootstrap
zip -r function.zip bootstrap
cd ..
```

For JVM build:
```bash
cd target
zip -r function.zip *-runner.jar lib
cd ..
```

## Step 4: Upload to S3

```bash
aws s3 cp target/function.zip s3://lopez-lambda-deployments/survey-handler/function.zip
```

## Step 5: Deploy CloudFormation Stack

```bash
aws cloudformation deploy \
  --template-file cloudformation-template.yaml \
  --stack-name survey-handler-dev \
  --parameter-overrides \
    Environment=dev \
    S3Bucket=lopez-lambda-deployments \
    S3Key=survey-handler/function.zip \
  --capabilities CAPABILITY_IAM
```

## Step 6: Test the Deployment

Get the API URL:
```bash
aws cloudformation describe-stacks \
  --stack-name survey-handler-dev \
  --query 'Stacks[0].Outputs[?OutputKey==`ApiUrl`].OutputValue' \
  --output text
```

Test the health endpoint:
```bash
curl https://YOUR_API_URL/health
```

Test the survey endpoints:
```bash
# List surveys
curl https://YOUR_API_URL/survey

# List agreements
curl https://YOUR_API_URL/survey/agreement
```

## Troubleshooting

### Common Issues

1. **Permission Denied**: Ensure your AWS credentials have the necessary permissions for Lambda, DynamoDB, and CloudFormation.

2. **Native Build Fails**: Make sure Docker is running and you have sufficient memory allocated.

3. **DynamoDB Access Issues**: Check that the Lambda execution role has the correct DynamoDB permissions.

### Logs

View Lambda logs:
```bash
aws logs describe-log-groups --log-group-name-prefix /aws/lambda/survey-handler
aws logs tail /aws/lambda/survey-handler-dev --follow
```

### Stack Updates

To update an existing stack:
```bash
aws cloudformation deploy \
  --template-file cloudformation-template.yaml \
  --stack-name survey-handler-dev \
  --parameter-overrides \
    Environment=dev \
    S3Bucket=lopez-lambda-deployments \
    S3Key=survey-handler/function.zip \
  --capabilities CAPABILITY_IAM
```

### Cleanup

To delete the stack:
```bash
aws cloudformation delete-stack --stack-name survey-handler-dev
```