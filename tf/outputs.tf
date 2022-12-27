output "tf_output_aws_apigw_domain_name__domain_name" {
  value = aws_api_gateway_deployment.example.invoke_url
}