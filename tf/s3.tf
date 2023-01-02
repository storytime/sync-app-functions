resource "aws_s3_bucket" "bucket_for_builds" {
  bucket = var.s3_bucket_for_builds

  tags = {
    app = var.tag_app_name
  }

  lifecycle {
    prevent_destroy = true
  }
}

resource "aws_s3_bucket_public_access_block" "bucket_for_builds_access" {
  bucket = aws_s3_bucket.bucket_for_builds.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_bucket_lifecycle_configuration" "s3_for_builds_rotation" {
  bucket = aws_s3_bucket.bucket_for_builds.id

  rule {
    id = var.s3_bucket_for_builds_rule_desc
    expiration {
      days = var.s3_bucket_for_builds_expiration
    }
    status = var.s3_bucket_policy_status
  }
}

#S3 for backups
resource "aws_s3_bucket" "bucket_for_backups" {
  bucket = data.aws_ssm_parameter.backup_s3_bucket.value

  tags = {
    app = var.tag_app_name
  }

  lifecycle {
    prevent_destroy = true
  }
}

resource "aws_s3_bucket_public_access_block" "bucket_for_backups_access" {
  bucket = aws_s3_bucket.bucket_for_backups.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_bucket_lifecycle_configuration" "s3_for_backups_rotation" {
  bucket = aws_s3_bucket.bucket_for_backups.id

  rule {
    id = var.s3_bucket_for_backups_rule_desc
    expiration {
      days = var.s3_bucket_for_backups_rule_expiration
    }
    status = var.s3_bucket_policy_status
  }
}

data "aws_s3_object" "export_be" {
  bucket = aws_s3_bucket.bucket_for_builds.bucket
  key    = "${var.function_export_be_build}.zip"
}

data "aws_s3_object" "export_api" {
  bucket = aws_s3_bucket.bucket_for_builds.bucket
  key    = "${var.function_export_api_build}.zip"
}

data "aws_s3_object" "starter" {
  bucket = aws_s3_bucket.bucket_for_builds.bucket
  key    = "${var.function_starter_build}.zip"
}

data "aws_s3_object" "backup_be" {
  bucket = aws_s3_bucket.bucket_for_builds.bucket
  key    = "${var.function_backup_be_build}.zip"
}