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


# test run
resource "aws_lambda_invocation" "test_run" {
  function_name = aws_lambda_function.backup_starter_function.function_name

  depends_on = [
    aws_lambda_function.export_starter_function,
    aws_lambda_function.export_be_function,
    aws_lambda_function.export_api_function,
    aws_lambda_function.backup_starter_function,
    aws_lambda_function.backup_be_function
  ]
  input = jsonencode({})
}