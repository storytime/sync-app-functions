resource "aws_cloudwatch_log_group" "export_be_logs" {
  name              = "${var.cw_lambda_prefix}${var.function_export_be_build}"
  retention_in_days = var.cw_lambda_logs_retentions
}

resource "aws_cloudwatch_log_group" "export_starter_logs" {
  name              = "${var.cw_lambda_prefix}${var.function_export_starter_name}"
  retention_in_days = var.cw_lambda_logs_retentions
}

resource "aws_cloudwatch_log_group" "backup_starter_logs" {
  name              = "${var.cw_lambda_prefix}${var.function_backup_starter_name}"
  retention_in_days = var.cw_lambda_logs_retentions
}

resource "aws_cloudwatch_log_group" "backup_be_logs" {
  name              = "${var.cw_lambda_prefix}${var.function_backup_be_build}"
  retention_in_days = var.cw_lambda_logs_retentions
}

resource "aws_cloudwatch_log_group" "export_api_logs" {
  name              = "${var.cw_lambda_prefix}${var.function_export_api_build}"
  retention_in_days = var.cw_lambda_logs_retentions
}