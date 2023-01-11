terraform {

  cloud {
    organization = "sync-app"

    workspaces {
      name = "sync-app-functions"
    }
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.48.0"
    }
  }
}