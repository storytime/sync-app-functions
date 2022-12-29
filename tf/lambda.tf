############### exporter-be ###############
resource "aws_lambda_function" "export_be_function" {
  function_name = var.function_export_be_name
  role          = aws_iam_role.function_role_name.arn

  s3_bucket = aws_s3_bucket.bucket_for_builds.bucket
  s3_key    = "${var.function_export_be_name}.zip"

  runtime          = var.lambda_runtime
  timeout          = var.function_export_be_timeout
  memory_size      = var.function_128_ram
  handler          = var.function_no_handler
  source_code_hash = data.aws_s3_object.export_be.body

  environment {
    variables = {
      TABLE_USER   = data.aws_ssm_parameter.export_be_user_db.value
      TABLE_EXPORT = data.aws_ssm_parameter.export_be_export_table.value
      URL          = data.aws_ssm_parameter.export_be_url.value
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
  name              = "${var.cw_lambda_prefix}${var.function_export_be_name}"
  retention_in_days = var.cw_lambda_logs_retentions
}

resource "aws_lambda_function_event_invoke_config" "export_be_invoke_param" {
  function_name                = aws_lambda_function.export_be_function.function_name
  maximum_event_age_in_seconds = var.function_event_age
  maximum_retry_attempts       = var.function_retry
}


############### exporter - api ###############
resource "aws_lambda_function" "export_api_function" {
  function_name = var.function_export_api_name
  role          = aws_iam_role.function_role_name.arn

  s3_bucket        = aws_s3_bucket.bucket_for_builds.bucket
  s3_key           = "${var.function_export_api_name}.zip"
  source_code_hash = data.aws_s3_object.export_api.body

  runtime     = var.lambda_runtime
  timeout     = var.function_export_api_timeout
  memory_size = var.function_128_ram
  handler     = var.function_no_handler

  environment {
    variables = {
      TABLE_EXPORT = data.aws_ssm_parameter.export_be_export_table.value
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
  name              = "${var.cw_lambda_prefix}${var.function_export_api_name}"
  retention_in_days = var.cw_lambda_logs_retentions
}

resource "aws_lambda_function_event_invoke_config" "export_api_invoke_param" {
  function_name                = aws_lambda_function.export_api_function.function_name
  maximum_event_age_in_seconds = var.function_event_age
  maximum_retry_attempts       = var.function_retry
}

