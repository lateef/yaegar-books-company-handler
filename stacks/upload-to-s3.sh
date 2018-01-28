#!/bin/bash

aws s3 sync ../build/distributions s3://sharedbucketsintyaegarboo-s3bucketsharedresources-boencsukew48/code/lambda --exclude "*" --include "yaegar-books-company-handler-1.0.0-SNAPSHOT.zip"