variable "aws_region" {
  default = "eu-west-1"
}

variable "aws_profile" {
  default = "default"
}

variable "username" {
  default = "sync-app-user"
}

variable "api_gw_name" {
  default = "sync-app-api"
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
    "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess",
  ]
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

variable "s3_bucket_for_builds_rule_desc" {
  default = "rotation-after-3-days"
}

variable "s3_bucket_for_builds_expiration" {
  default = 3
}

variable "s3_bucket_for_builds_policy_status" {
  default = "Enabled"
}

variable "function_role_name" {
  default = "sync_app_lambda_role"
}

variable "lambda_runtime" {
  default = "provided.al2"
}


variable "function_export_be_name" {
  default = "export-be"
}

variable "function_128_ram" {
  default = 128
}

variable "function_export_be_timeout" {
  default = 45
}

variable "function_export_api_timeout" {
  default = 3
}

variable "function_no_handler" {
  default = "not.used.in.provided.runtime"
}

variable "function_export_api_name" {
  default = "export-api"
}

variable "function_export_starter_name" {
  default = "export-starter"
}

variable "function_export_starter" {
  default = "starter"
}

variable "cw_lambda_prefix" {
  default = "/aws/lambda/"
}

variable "cw_lambda_logs_retentions" {
  default = 180
}

variable "function_event_age" {
  default = 60
}

variable "function_retry" {
  default = 0
}

variable "export_api_code" {
  default = "200"
}

variable "export_api_plan_name" {
  default = "export-api-plan"
}

variable "export_api_model" {
  default = "Empty"
}

variable "export_starter_schedule_name" {
  default = "export-be-every-1h"
}

variable "export_starter_schedule_internal" {
  default = "rate(1 hour)"
}
