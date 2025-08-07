#!/bin/bash

# Survey Handler Lambda Deployment Script

set -e

# Configuration
ENVIRONMENT=${1:-dev}
S3_BUCKET="survey-handler-lambda-20250729-193600"  # Update with your S3 bucket name
FUNCTION_NAME="survey-handler-${ENVIRONMENT}"
STACK_NAME="survey-handler-${ENVIRONMENT}"

echo "Deploying Survey Handler Lambda for environment: $ENVIRONMENT"

# Build the native executable
echo "Building native executable..."
./mvnw clean package -Pnative -Dquarkus.native.container-build=true

# Create deployment package
echo "Creating deployment package..."
cd target
cp *-runner bootstrap
zip -r function.zip bootstrap
cd ..

# Upload to S3
echo "Uploading to S3..."
aws s3 cp target/function.zip s3://${S3_BUCKET}/survey-handler/function.zip

# Force delete existing stack to clear any cached state
# echo "Deleting existing stack if present..."
# aws cloudformation delete-stack --stack-name ${STACK_NAME} 2>/dev/null || true
# aws cloudformation wait stack-delete-complete --stack-name ${STACK_NAME} 2>/dev/null || true

# Deploy CloudFormation stack
echo "Deploying CloudFormation stack..."
aws cloudformation deploy \
  --template-file cloudformation-template.yaml \
  --stack-name ${STACK_NAME} \
  --parameter-overrides \
    Environment=${ENVIRONMENT} \
    S3Bucket=${S3_BUCKET} \
    S3Key=survey-handler/function.zip \
  --capabilities CAPABILITY_IAM

echo "Deployment completed successfully!"

echo "Survey Handler Lambda deployed successfully!"
echo "Function ARN: $(aws cloudformation describe-stacks --stack-name ${STACK_NAME} --query 'Stacks[0].Outputs[?OutputKey==`SurveyHandlerFunctionArn`].OutputValue' --output text)"
echo "Use the shared API Gateway to access endpoints."