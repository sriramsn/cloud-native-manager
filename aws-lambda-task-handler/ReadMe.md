AWS Lambda Task Handler (Python)
- Handler: lambda_function.lambda_handler
- Env:
    - BUCKET_NAME = <your-bucket>
    - S3_PREFIX = task-summaries/

Test locally (quick):
python3 -c "import json, lambda_function as lf; print(lf.lambda_handler({'taskId':1,'status':'COMPLETED'}, None))"
