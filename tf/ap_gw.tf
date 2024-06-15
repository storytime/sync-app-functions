resource "aws_api_gateway_rest_api" "sync_app_api" {
  name = var.api_gw_name
  depends_on = [
    aws_lambda_function.export_api_function
  ]
}

resource "aws_api_gateway_resource" "root" {
  parent_id   = aws_api_gateway_rest_api.sync_app_api.root_resource_id
  rest_api_id = aws_api_gateway_rest_api.sync_app_api.id
  path_part   = data.aws_ssm_parameter.export_api_root.value

  depends_on = [
    aws_api_gateway_rest_api.sync_app_api
  ]
}

resource "aws_api_gateway_resource" "user_path" {
  parent_id   = aws_api_gateway_resource.root.id
  rest_api_id = aws_api_gateway_rest_api.sync_app_api.id
  path_part   = data.aws_ssm_parameter.export_api_user.value

  depends_on = [
    aws_api_gateway_resource.root
  ]
}

resource "aws_api_gateway_resource" "type_path" {
  parent_id   = aws_api_gateway_resource.user_path.id
  rest_api_id = aws_api_gateway_rest_api.sync_app_api.id
  path_part   = data.aws_ssm_parameter.export_api_type.value

  depends_on = [
    aws_api_gateway_resource.user_path
  ]
}

resource "aws_api_gateway_method" "export_method" {
  authorization = data.aws_ssm_parameter.export_api_auth.value
  http_method   = data.aws_ssm_parameter.export_api_method.value
  resource_id   = aws_api_gateway_resource.type_path.id
  rest_api_id   = aws_api_gateway_rest_api.sync_app_api.id

  depends_on = [
    aws_api_gateway_resource.type_path
  ]
}

resource "aws_api_gateway_integration" "api_gw_integration" {
  rest_api_id             = aws_api_gateway_rest_api.sync_app_api.id
  resource_id             = aws_api_gateway_resource.type_path.id
  http_method             = aws_api_gateway_method.export_method.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.export_api_function.invoke_arn

  depends_on = [
    aws_api_gateway_method.export_method
  ]
}

resource "aws_api_gateway_method_response" "response_200" {
  rest_api_id = aws_api_gateway_rest_api.sync_app_api.id
  resource_id = aws_api_gateway_resource.type_path.id
  http_method = aws_api_gateway_method.export_method.http_method
  status_code = var.export_api_code

  response_models = {
    "application/json" = var.export_api_model
  }

  depends_on = [
    aws_api_gateway_integration.api_gw_integration
  ]
}

resource "aws_api_gateway_integration_response" "api_gw_integration_resp" {
  rest_api_id = aws_api_gateway_rest_api.sync_app_api.id
  resource_id = aws_api_gateway_resource.type_path.id
  http_method = aws_api_gateway_method.export_method.http_method
  status_code = aws_api_gateway_method_response.response_200.status_code

  depends_on = [
    aws_api_gateway_method_response.response_200
  ]
}

resource "aws_api_gateway_deployment" "apg_gw_deployment" {
  rest_api_id = aws_api_gateway_rest_api.sync_app_api.id

  triggers = {
    redeployment = sha1(jsonencode([
      aws_api_gateway_resource.root.id,
      aws_api_gateway_method.export_method.id,
      aws_api_gateway_integration.api_gw_integration.id,
    ]))
  }

  depends_on = [
    aws_api_gateway_integration_response.api_gw_integration_resp
  ]
}

resource "aws_api_gateway_stage" "sync_app_gw_stage" {
  deployment_id = aws_api_gateway_deployment.apg_gw_deployment.id
  rest_api_id   = aws_api_gateway_rest_api.sync_app_api.id
  stage_name    = data.aws_ssm_parameter.export_api_stage.value

  depends_on = [
    aws_api_gateway_deployment.apg_gw_deployment
  ]
}

resource "aws_lambda_permission" "sync_api_export_gw_perm" {
  source_arn    = "${aws_api_gateway_rest_api.sync_app_api.execution_arn}/*/*/*"
  function_name = aws_lambda_function.export_api_function.function_name
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  principal     = "apigateway.amazonaws.com"

  depends_on = [
    aws_api_gateway_stage.sync_app_gw_stage
  ]
}


resource "aws_api_gateway_usage_plan" "export_api_plan" {
  name = var.export_api_plan_name

  api_stages {
    api_id = aws_api_gateway_rest_api.sync_app_api.id
    stage  = aws_api_gateway_stage.sync_app_gw_stage.stage_name
  }

  throttle_settings {
    burst_limit = 1000
    rate_limit  = 10000
  }
}