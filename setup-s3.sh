#!/bin/bash

# Setup S3 bucket for Lambda deployments
ENVIRONMENT=${1:-dev}
BUCKET_NAME="survey-handler-lambda-${ENVIRONMENT}"
REGION="us-east-1"

echo "Setting up S3 bucket: $BUCKET_NAME"

# Check if bucket exists
if aws s3 ls "s3://$BUCKET_NAME" 2>&1 | grep -q 'NoSuchBucket'; then
    echo "Creating S3 bucket..."
    aws s3 mb s3://$BUCKET_NAME --region $REGION
    
    # Enable versioning
    aws s3api put-bucket-versioning \
        --bucket $BUCKET_NAME \
        --versioning-configuration Status=Enabled
    
    echo "S3 bucket created and versioning enabled"
else
    echo "S3 bucket already exists"
fi

# Create survey-handler folder
aws s3api put-object --bucket $BUCKET_NAME --key survey-handler/

echo "S3 setup completed"