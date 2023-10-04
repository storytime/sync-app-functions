resource "aws_sqs_queue" "export-be-queue" {
  name                       = data.aws_ssm_parameter.export_be_queue.value
  delay_seconds              = 0
  max_message_size           = 1024
  message_retention_seconds  = 10800
  visibility_timeout_seconds = 90
}


resource "aws_sqs_queue" "backup-queue" {
  name                       = data.aws_ssm_parameter.backup_starter_queue.value
  delay_seconds              = 0
  max_message_size           = 1024
  message_retention_seconds  = 10800
  visibility_timeout_seconds = 90
}