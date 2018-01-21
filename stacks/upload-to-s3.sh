#!/bin/bash

aws s3 sync ../build/distributions s3://intyaegarbookssharedresou-s3bucketsharedresources-17imna8ju1kby/code/lambda --exclude "*" --include "yaegar-books-company-handler-1.0.0-SNAPSHOT.zip"