# test run
resource "aws_lambda_invocation" "test_run" {
  function_name = aws_lambda_function.export_starter_function.function_name

  depends_on = [
    aws_lambda_function.export_starter_function,
    aws_lambda_function.export_be_function,
    aws_lambda_function.export_api_function,
    aws_lambda_function.backup_starter_function,
    aws_lambda_function.backup_be_function
  ]
  input = jsonencode({})
}