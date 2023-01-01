output "api_gw_link" {
  sensitive = false
  value     = aws_api_gateway_stage.sync_app_gw_stage.invoke_url
}


output "api_gw_path" {
  sensitive = false
  value     = nonsensitive(aws_api_gateway_resource.root.path_part)
}

output "api_gw_link_user" {
  sensitive = false
  value     = nonsensitive(data.aws_ssm_parameter.export_api_test_user.value)
}

output "api_gw_link_type" {
  sensitive = false
  value     = nonsensitive(data.aws_ssm_parameter.export_api_test_type.value)
}
