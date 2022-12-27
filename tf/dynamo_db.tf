resource "aws_dynamodb_table" "dynamo_db_export_table" {
  name           = var.dynamo_db_export_name
  billing_mode   = var.dynamo_db_billing
  read_capacity  = var.dynamo_db_rw_capacity
  write_capacity = var.dynamo_db_rw_capacity
  hash_key       = var.dynamo_db_export_pk

  attribute {
    name = var.dynamo_db_export_pk
    type = var.dynamo_db_export_type
  }

  tags = {
    app = var.tag_app_name
  }
}
