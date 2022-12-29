#output "api_gw_link" {
#  sensitive = false
#  value = "${aws_api_gateway_stage.example.invoke_url}/${aws_api_gateway_resource.example_root.path_part}/${data.aws_ssm_parameter.export_api_test_user.value}/${data.aws_ssm_parameter.export_api_test_type.value}"
#}
