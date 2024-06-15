resource "aws_iam_role" "function_role_name" {
  name = var.function_role_name
  assume_role_policy = jsonencode({
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

resource "aws_iam_role_policy_attachment" "function_role_name_policy_attachment" {
  for_each = toset(var.permissions)
  role       = aws_iam_role.function_role_name.name
  policy_arn = each.value
}
