# Exporter Project

### Instructions

1. Testing locally with **JVM** image (Optional)
    - build
        - `./gradlew build`
    - invoke Lambda function locally using SAM (AWS Serverless)
        - make sure you have AWS CLI installed and configured
            - `aws --version` - should return something like `aws-cli/2.1.3 Python/3.7.7 Windows/10 exe/AMD64`
        - make sure you have SAM CLI installed and configured
            - `sam --version` should return something like `SAM CLI, version 1.65.0`
        - `sam local invoke --template build/sam.jvm.yaml --event configs/payload.json`
        - in case of Read Timeout Error you need to modify `./build/sam.jvm.yaml`:
            - increase `Resources.SimpleQuarkusLambda.Properties.MemorySize` from 256MB to 512MB
            - increase `Resources.SimpleQuarkusLambda.Properties.Timeout` from 15 to 30sec
2. Build **Native** image
    - `./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true`
    - **or** (in case of Windows)
    - `./gradlew build  '-Dquarkus.package.type=native' '-Dquarkus.native.container-build=true'`
3. Invoke Native Lambda Function locally using SAM
    - (prerequisites: AWS CLI and SAM ClI must be installed and configured)
    - `sam local invoke --template build/sam.native.yaml --event configs/payload.json`
    - response should be like that

4. Deploy Lambda into the AWS cloud using SAM

    - `sam deploy --template-file build/sam.native.yaml` or `sam deploy --template-file build/sam.native.yaml --guided` 
        - Stack Name: `quarkus-lambda-stack` (Whatever you want)
        - AWS Region: `...` (your default region)
        - Confirm changes before deploy [y/N]: (Enter)
        - Allow SAM CLI IAM role creation [Y/n]: (Enter)
        - Disable rollback [y/N]: (Enter)
        - Save arguments to configuration file [Y/n]: (Enter)
        - SAM configuration file [samconfig.toml]: (Enter) - SAM will create configuration file for future
          deployments/updates
        - SAM configuration environment [default]: (Enter)
    - After that SAM will upload files into S3 bucket and create Stack in CloudFormation
    - CloudFormation will create Lambda function and Role for Lambda Function
    - In the end success message should look like that
        - `Successfully created/updated stack - quarkus-lambda-stack in eu-north-1`
2. Invoke Lambda in AWS console
    - [https://aws.amazon.com/](https://aws.amazon.com/)
    - Sign In to the Console
    - choose your region
    - go to Lambda console (you can search for lambda)
    - find your function (by typing `quarkus` in search field for example)
    - I have `quarkus-lambda-stack-SimpleQuarkusLambdaNative-v5MjzBhFg34q` (your function name will slightly differ)
    - Test &rarr; Create new event &rarr;
    - Event name: `empty` &rarr;
    - Event JSON: `{}` &rarr; Save
    - Press `Test`
    - The response should be like that

```
START RequestId: c38d2ef3-1698-42da-8f08-3146b30df2fc Version: $LATEST
2022-12-18 13:35:53,524 INFO  [net.shy.upw.lam.UserLambda] (Lambda Thread (NORMAL)) Start Lambda
END RequestId: c38d2ef3-1698-42da-8f08-3146b30df2fc
REPORT RequestId: c38d2ef3-1698-42da-8f08-3146b30df2fc	Duration: 252.20 ms	Billed Duration: 253 ms	Memory Size: 128 MB	Max Memory Used: 73 MB	
```

6. View logs using SAM (without AWS console)
    - `sam logs --stack-name quarkus-lambda-stack`
    - will see logs of Lambda
7. Invoke Lambda function using AWS CLI (without AWS console)
    - `aws lambda invoke --function-name quarkus-lambda-stack-SimpleQuarkusLambdaNative-v5MjzBhFg34q response.json`
    - response will be saved in `response.json` (content: `...`)
    - output of `aws lambda invoke ...` should be something like that

```json
{
    "StatusCode": 200,
    "ExecutedVersion": "$LATEST"
}
```

8. Update lambda function in AWS
    - modify project (whatever you want)
    - rebuild native executable (Step 2 of current instructions)
        - `./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true`
    - Redeploy (from inside the directory `./build`)
        - `sam deploy --template-file sam.native.yaml`
9. Delete whole stack with Lambda function
    - (from inside the directory `./build`) run
    - `sam delete`
10. There is another option to manipulate Lambda function (instead of SAM)
    - shell script `./build/manage.sh`
    - some instructions can be found in [AMAZON LAMBDA](https://quarkus.io/guides/amazon-lambda)
    - but I did not test it (I prefer using SAM)