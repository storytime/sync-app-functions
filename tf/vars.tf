variable "aws_region" {
  default = "eu-west-1"
}

variable "aws_profile" {
  default = "default"
}

variable "username" {
  default = "sync-app-user"
}

variable "group_name" {
  default = "sync-app-group"
}

variable "tag_app_name" {
  default = "sync-app"
}

variable "group_assigment" {
  default = "group-membership"
}

variable "permissions" {
  default = [
    "arn:aws:iam::aws:policy/AmazonEventBridgeFullAccess",
    "arn:aws:iam::aws:policy/AmazonS3FullAccess",
    "arn:aws:iam::aws:policy/AmazonSQSFullAccess",
    "arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess",
    "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess"
  ]
}

variable "dynamo_db_export_name" {
  default = "sync-app-export-data"
}

variable "dynamo_db_billing" {
  default = "PROVISIONED"
}

variable "dynamo_db_rw_capacity" {
  default = 2
}

variable "dynamo_db_export_pk" {
  default = "userId"
}

variable "dynamo_db_export_type" {
  default = "S"
}

variable "s3_bucket_for_builds" {
  default = "sync-app-lambda-native-builds-1"
}

variable "kms_s3_desc" {
  default = "KMS for build bucket"
}

variable "kms_s3_timeout" {
  default = 10
}

variable "kms_s3_algo" {
  default = "aws:kms"
}

variable "s3_bucket_for_builds_rule_desc" {
  default = "rotation-after-3-days"
}

variable "s3_bucket_for_builds_expiration" {
  default = 3
}

variable "s3_bucket_for_builds_policy_status" {
  default = "Enabled"
}

variable "function_export_be_name" {
  default = "export-be"
}

variable "function_export_api_name" {
  default = "export-api"
}
