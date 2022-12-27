terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.48.0"
    }
  }
}

provider "aws" {
  profile = var.aws_profile
  region  = var.aws_region
}

terraform {
  backend "s3" {

    bucket = "sync-app-tf-state"
    key    = "functions-state/terraform.tfstate"
    region = "eu-west-1"

    #    dynamodb_table = "sync_app_terraform_locks"
    #    encrypt        = true
  }
}


############### exporter ###############
resource "aws_lambda_function" "export_be_function" {
  function_name = var.function_export_be_name
  role          = aws_iam_role.lambda_role.arn

  s3_bucket = aws_s3_bucket.bucket_for_builds.bucket
  s3_key    = "exporter/build/function.zip"

  runtime       = "provided.al2"
  timeout       = 45
  memory_size   = 128
  handler       = "not.used.in.provided.runtime"
  architectures = ["x86_64"]

  environment {
    variables = {
      foo = "bar"
      // foo = "bar" - user
      //  foo = "bar" - url
    }
  }

  tags = {
    app = var.tag_app_name
  }

  depends_on = [
    aws_cloudwatch_log_group.export_be_logs,
  ]
}

resource "aws_cloudwatch_log_group" "export_be_logs" {
  name              = "/aws/lambda/${var.function_export_be_name}"
  retention_in_days = 180
  // kms_key_id = "620f16d5-3919-44d2-96c3-fad8270182b3"
}

resource "aws_lambda_function_event_invoke_config" "export_be_invoke_param" {
  function_name                = aws_lambda_function.export_be_function.function_name
  maximum_event_age_in_seconds = 60
  maximum_retry_attempts       = 0
}


############### exporter - api ###############
resource "aws_lambda_function" "export_api_function" {
  function_name = var.function_export_api_name
  role          = aws_iam_role.lambda_role.arn

  s3_bucket = aws_s3_bucket.bucket_for_builds.bucket
  s3_key    = "function-api.zip"

  runtime       = "provided.al2"
  timeout       = 3
  memory_size   = 128
  handler       = "not.used.in.provided.runtime"
  architectures = ["x86_64"]

  environment {
    variables = {
      foo = "bar"
      // foo = "bar" - user
      //  foo = "bar" - url
    }
  }

  tags = {
    app = var.tag_app_name
  }

  depends_on = [
    aws_cloudwatch_log_group.export_api_logs,
  ]
}

resource "aws_cloudwatch_log_group" "export_api_logs" {
  name              = "/aws/lambda/${var.function_export_api_name}"
  retention_in_days = 180
  // kms_key_id = "620f16d5-3919-44d2-96c3-fad8270182b3"
}

resource "aws_lambda_function_event_invoke_config" "export_api_invoke_param" {
  function_name                = aws_lambda_function.export_api_function.function_name
  maximum_event_age_in_seconds = 60
  maximum_retry_attempts       = 0
}

#=====================================

resource "aws_api_gateway_rest_api" "example" {
  body = jsonencode({
    openapi = "3.0.1"
    info    = {
      title   = "example"
      version = "1.0"
    }
    paths = {
      "/userId/type" = {
        get = {
          x-amazon-apigateway-integration = {
            httpMethod           = "GET"
            payloadFormatVersion = "2.0"
            type                 = "AWS_PROXY"
            uri                  = aws_lambda_function.export_api_function.invoke_arn
          }
        }
      }
    }
  })

  name = "example"

  endpoint_configuration {
    types = ["REGIONAL"]
  }
}

resource "aws_api_gateway_deployment" "example" {
  rest_api_id = aws_api_gateway_rest_api.example.id

  triggers = {
    redeployment = sha1(jsonencode(aws_api_gateway_rest_api.example.body))
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_api_gateway_stage" "example" {
  deployment_id = aws_api_gateway_deployment.example.id
  rest_api_id   = aws_api_gateway_rest_api.example.id
  stage_name    = "example"
}

resource "aws_api_gateway_usage_plan" "example" {
  name         = "my-usage-plan"
  description  = "my description"
  product_code = "MYCODE"

  api_stages {
    api_id = aws_api_gateway_rest_api.example.id
    stage  = aws_api_gateway_stage.example.stage_name
  }


  throttle_settings {
    burst_limit = 500
    rate_limit  = 1000
  }
}