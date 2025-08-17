import json, os, time, logging
from datetime import datetime, timezone
import boto3
from botocore.exceptions import ClientError

logger = logging.getLogger()
logger.setLevel(logging.INFO)

BUCKET_NAME = os.getenv("BUCKET_NAME", "mock-task-summary-bucket")
S3_PREFIX = os.getenv("S3_PREFIX", "task-summaries/")
s3 = boto3.client("s3")

def _now_iso(): return datetime.now(timezone.utc).isoformat()
def _key(task_id):  return f"{S3_PREFIX}{task_id}-{int(time.time())}.json"

def lambda_handler(event, context):
    logger.info("Received event: %s", json.dumps(event))
    task_id = event.get("taskId")
    if not task_id:
        return {"statusCode": 400, "body": json.dumps({"message": "taskId is required"})}

    summary = {
        "taskId": task_id,
        "title": event.get("title", ""),
        "status": event.get("status", "COMPLETED"),
        "completedAt": event.get("completedAt") or _now_iso(),
        "recordedAt": _now_iso(),
        "recordedBy": event.get("user", "unknown")
    }

    key = _key(task_id)
    try:
        s3.put_object(Bucket=BUCKET_NAME, Key=key, Body=json.dumps(summary), ContentType="application/json")
        logger.info("Wrote summary to s3://%s/%s", BUCKET_NAME, key)
        return {"statusCode": 200, "body": json.dumps({"message":"Summary stored","bucket":BUCKET_NAME,"key":key})}
    except ClientError as e:
        logger.exception("S3 write failed")
        return {"statusCode": 500, "body": json.dumps({"message":"S3 write failed","error":str(e)})}
