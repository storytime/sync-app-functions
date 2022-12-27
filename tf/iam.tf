#resource "aws_iam_user" "sync_app_user" {
#  name = var.username
#  tags = {
#    app = var.tag_app_name
#  }
#}
#
#resource "aws_iam_group" "sync_app_group" {
#  name = var.group_name
#}
#
#resource "aws_iam_group_membership" "user_gr_assigment" {
#  name  = var.group_assigment
#  users = [
#    aws_iam_user.sync_app_user.name
#  ]
#  group = aws_iam_group.sync_app_group.name
#}
#
#resource "aws_iam_group_policy_attachment" "group_policy_assigment" {
#  for_each   = toset(var.permissions)
#  group      = aws_iam_group.sync_app_group.name
#  policy_arn = each.value
#}

resource "aws_iam_role" "lambda_role" {
  name               = "lambda_role"
  assume_role_policy =  jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_role_policy_attachment" {
  for_each   = toset(var.permissions)
  role       = aws_iam_role.lambda_role.name
  policy_arn = each.value
}
