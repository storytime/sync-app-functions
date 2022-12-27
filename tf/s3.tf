#resource "aws_s3_bucket" "bucket_for_builds" {
#  bucket = var.s3_bucket_for_builds
#
#  tags = {
#    app = var.tag_app_name
#  }
#}
#
#resource "aws_s3_bucket_public_access_block" "bucket_for_builds_access" {
#  bucket = aws_s3_bucket.bucket_for_builds.id
#
#  block_public_acls       = true
#  block_public_policy     = true
#  ignore_public_acls      = true
#  restrict_public_buckets = true
#}
#
#resource "aws_kms_key" "enc_key_for_bucket_for_builds" {
#  description             = var.kms_s3_desc
#  deletion_window_in_days = var.kms_s3_timeout
#
#  tags = {
#    app = var.tag_app_name
#  }
#}
#
#resource "aws_s3_bucket_server_side_encryption_configuration" "encryption-bucket-for-builds" {
#  bucket = aws_s3_bucket.bucket_for_builds.bucket
#
#  rule {
#    apply_server_side_encryption_by_default {
#      kms_master_key_id = aws_kms_key.enc_key_for_bucket_for_builds.arn
#      sse_algorithm     = var.kms_s3_algo
#    }
#  }
#}
#
#resource "aws_s3_bucket_lifecycle_configuration" "s3_rotation" {
#  bucket = aws_s3_bucket.bucket_for_builds.id
#
#  rule {
#    id = var.s3_bucket_for_builds_rule_desc
#    expiration {
#      days = var.s3_bucket_for_builds_expiration
#    }
#    status = var.s3_bucket_for_builds_policy_status
#  }
#}