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