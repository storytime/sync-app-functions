data "aws_ssm_parameter" "user_db" {
  name = "/export-be/table_user_name"
}

data "aws_ssm_parameter" "export_be_export_table" {
  name = "/export-be/table_export_name"
}

data "aws_ssm_parameter" "export_be_url" {
  name = "/export-be/url"
}

data "aws_ssm_parameter" "export_api_export_table" {
  name = "/export-api/table_export_name"
}

data "aws_ssm_parameter" "export_api_test_user" {
  name = "/export-api/test_user"
}

data "aws_ssm_parameter" "export_api_test_type" {
  name = "/export-api/test_type"
}

data "aws_ssm_parameter" "export_api_root" {
  name = "/sync-app-api/root"
}

data "aws_ssm_parameter" "export_api_user" {
  name = "/sync-app-api/user"
}

data "aws_ssm_parameter" "export_api_type" {
  name = "/sync-app-api/type"
}

data "aws_ssm_parameter" "export_api_method" {
  name = "/sync-app-api/method"
}

data "aws_ssm_parameter" "export_api_auth" {
  name = "/sync-app-api/auth"
}

data "aws_ssm_parameter" "export_api_stage" {
  name = "/sync-app-api/stage"
}

data "aws_ssm_parameter" "export_be_queue" {
 name = "/export-be/queue"
}

data "aws_ssm_parameter" "backup_starter_queue" {
 name = "/backup/queue_name"
}

data "aws_ssm_parameter" "backup_s3_bucket" {
  name = "/backup/s3_bucket"
}