terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.19.0"
    }
  }
}

terraform {
  backend "s3" {
    bucket = "sync-app-tf-state"
    key    = "functions-state/terraform.tfstate"
    region = "eu-west-1"
  }
}

provider "aws" {
  profile = var.aws_profile
  region  = var.aws_region
}