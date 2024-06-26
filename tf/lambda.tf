############### exporter-be ###############
resource "aws_lambda_function" "export_be_function" {
  function_name = var.function_export_be_build
  role          = aws_iam_role.function_role_name.arn

  s3_bucket = aws_s3_bucket.bucket_for_builds.bucket
  s3_key    = "${var.function_export_be_build}.zip"

  runtime          = var.lambda_runtime
  timeout          = var.function_export_be_timeout
  memory_size      = var.function_256_ram
  handler          = var.function_no_handler
  source_code_hash = data.aws_s3_object.export_be.content_length

  environment {
    variables = {
      TABLE_USER              = data.aws_ssm_parameter.user_db.value
      TABLE_EXPORT            = data.aws_ssm_parameter.export_be_export_table.value
      TABLE_CURRENCY          = data.aws_ssm_parameter.table_currency.value
      URL                     = data.aws_ssm_parameter.export_be_url.value
      PB_CURRENCY_ARCHIVE_URL = data.aws_ssm_parameter.pb_currency_archive_url.value
      EXPORT_START            = data.aws_ssm_parameter.export_be_export_start.value
    }
  }

  tags = {
    app = var.tag_app_name
  }

  depends_on = [
    aws_cloudwatch_log_group.export_be_logs,
  ]
}

resource "aws_lambda_function_event_invoke_config" "export_be_invoke_param" {
  function_name                = aws_lambda_function.export_be_function.function_name
  maximum_event_age_in_seconds = var.function_event_age
  maximum_retry_attempts       = var.function_retry
}

resource "aws_lambda_event_source_mapping" "event_source_mapping" {
  event_source_arn = aws_sqs_queue.export-be-queue.arn
  enabled          = true
  function_name    = aws_lambda_function.export_be_function.arn
  batch_size       = 1

  depends_on = [
    aws_sqs_queue.export-be-queue,
    aws_lambda_function.export_be_function
  ]
}

############### exporter - api ###############
resource "aws_lambda_function" "export_api_function" {
  function_name = var.function_export_api_build
  role          = aws_iam_role.function_role_name.arn

  s3_bucket        = aws_s3_bucket.bucket_for_builds.bucket
  s3_key           = "${var.function_export_api_build}.zip"
  source_code_hash = data.aws_s3_object.export_api.content_length

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

resource "aws_lambda_function_event_invoke_config" "export_api_invoke_param" {
  function_name                = aws_lambda_function.export_api_function.function_name
  maximum_event_age_in_seconds = var.function_event_age
  maximum_retry_attempts       = var.function_retry
}


############### exporter - starter ###############
resource "aws_lambda_function" "export_starter_function" {
  function_name = var.function_export_starter_name
  role          = aws_iam_role.function_role_name.arn

  s3_bucket        = aws_s3_bucket.bucket_for_builds.bucket
  s3_key           = "${var.function_starter_build}.zip"
  source_code_hash = data.aws_s3_object.starter.content_length

  runtime     = var.lambda_runtime
  timeout     = var.function_export_api_timeout
  memory_size = var.function_128_ram
  handler     = var.function_no_handler

  environment {
    variables = {
      TABLE_USER = data.aws_ssm_parameter.user_db.value
      QUEUE_NAME = data.aws_ssm_parameter.export_be_queue.value
    }
  }

  tags = {
    app = var.tag_app_name
  }

  depends_on = [
    aws_cloudwatch_log_group.export_starter_logs,
  ]
}

resource "aws_lambda_function_event_invoke_config" "export_starter_invoke_param" {
  function_name                = aws_lambda_function.export_starter_function.function_name
  maximum_event_age_in_seconds = var.function_event_age
  maximum_retry_attempts       = var.function_retry
}


resource "aws_cloudwatch_event_rule" "export_starter_schedule" {
  name                = var.export_starter_schedule_name
  schedule_expression = var.export_starter_schedule_internal
}

resource "aws_cloudwatch_event_target" "export_starter_target" {
  arn  = aws_lambda_function.export_starter_function.arn
  rule = aws_cloudwatch_event_rule.export_starter_schedule.name

  depends_on = [
    aws_cloudwatch_event_rule.export_starter_schedule,
    aws_lambda_function.export_starter_function
  ]
}

resource "aws_lambda_permission" "export_starter_target_allow_events_bridge_to_run_lambda" {
  function_name = aws_lambda_function.export_starter_function.function_name
  statement_id  = "AllowExecutionFromCloudWatch"
  action        = "lambda:InvokeFunction"
  principal     = "events.amazonaws.com"
}


############### backup - starter ###############
resource "aws_lambda_function" "backup_starter_function" {
  function_name = var.function_backup_starter_name
  role          = aws_iam_role.function_role_name.arn

  s3_bucket        = aws_s3_bucket.bucket_for_builds.bucket
  s3_key           = "${var.function_starter_build}.zip"
  source_code_hash = data.aws_s3_object.starter.content_length

  runtime     = var.lambda_runtime
  timeout     = var.function_export_api_timeout
  memory_size = var.function_128_ram
  handler     = var.function_no_handler

  environment {
    variables = {
      TABLE_USER = data.aws_ssm_parameter.user_db.value
      QUEUE_NAME = data.aws_ssm_parameter.backup_starter_queue.value
    }
  }

  tags = {
    app = var.tag_app_name
  }

  depends_on = [
    aws_cloudwatch_log_group.backup_starter_logs,
  ]
}


resource "aws_lambda_function_event_invoke_config" "backup_starter_invoke_param" {
  function_name                = aws_lambda_function.backup_starter_function.function_name
  maximum_event_age_in_seconds = var.function_event_age
  maximum_retry_attempts       = var.function_retry
}


resource "aws_cloudwatch_event_rule" "backup_schedule" {
  name                = var.backup_starter_schedule_name
  schedule_expression = var.backup_starter_schedule_internal
}

resource "aws_cloudwatch_event_target" "backup_starter_target" {
  arn  = aws_lambda_function.backup_starter_function.arn
  rule = aws_cloudwatch_event_rule.backup_schedule.name

  depends_on = [
    aws_cloudwatch_event_rule.backup_schedule,
    aws_lambda_function.backup_starter_function
  ]
}

resource "aws_lambda_permission" "backup_starter_target_allow_events_bridge_to_run_lambda" {
  function_name = aws_lambda_function.backup_starter_function.function_name
  statement_id  = "AllowExecutionFromCloudWatch"
  action        = "lambda:InvokeFunction"
  principal     = "events.amazonaws.com"
}


############### backup-be ###############
resource "aws_lambda_function" "backup_be_function" {
  function_name = var.function_backup_be_build
  role          = aws_iam_role.function_role_name.arn

  s3_bucket = aws_s3_bucket.bucket_for_builds.bucket
  s3_key    = "${var.function_backup_be_build}.zip"

  runtime          = var.lambda_runtime
  timeout          = var.function_export_be_timeout
  memory_size      = var.function_256_ram
  handler          = var.function_no_handler
  source_code_hash = data.aws_s3_object.backup_be.content_length

  environment {
    variables = {
      TABLE_USER = data.aws_ssm_parameter.user_db.value
      URL        = data.aws_ssm_parameter.export_be_url.value
      BUCKET     = data.aws_ssm_parameter.backup_s3_bucket.value
      CLASS      = var.backup_be_storage_class
    }
  }

  tags = {
    app = var.tag_app_name
  }

  depends_on = [
    aws_cloudwatch_log_group.backup_be_logs,
  ]
}


resource "aws_lambda_function_event_invoke_config" "backup_be_invoke_param" {
  function_name                = aws_lambda_function.backup_be_function.function_name
  maximum_event_age_in_seconds = var.function_event_age
  maximum_retry_attempts       = var.function_retry
}

resource "aws_lambda_event_source_mapping" "backup_be_event_source_mapping" {
  event_source_arn = aws_sqs_queue.backup-queue.arn
  enabled          = true
  function_name    = aws_lambda_function.backup_be_function.arn
  batch_size       = 1

  depends_on = [
    aws_sqs_queue.backup-queue,
    aws_lambda_function.backup_be_function
  ]
}