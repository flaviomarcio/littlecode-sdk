#!/bin/bash
set -e

export AWS_HOST_NAME=localhost
export AWS_HOST_URL=http://${AWS_HOST_NAME}:4566
export AWS_ACCESS_KEY_ID=localstack
export AWS_DEFAULT_REGION=us-east-1
export AWS_SECRET_ACCESS_KEY=localstack

echo "Criando bucket S3..."
awslocal s3 mb s3://develop

#aws --endpoint-url=http://localhost:4566 s3 ls

echo ""
echo "aws --endpoint-url=${AWS_HOST_URL} --region=${AWS_DEFAULT_REGION} s3 ls"
aws --endpoint-url=${AWS_HOST_URL} --region=${AWS_DEFAULT_REGION} s3 ls

echo ""
echo "aws --endpoint-url=${AWS_HOST_URL} --region=${AWS_DEFAULT_REGION} s3 ls s3://develop"
aws --endpoint-url=${AWS_HOST_URL} --region=${AWS_DEFAULT_REGION} s3 ls s3://develop