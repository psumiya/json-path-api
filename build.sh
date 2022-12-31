#!/bin/sh

# Remove a previously created custom runtime
file="JsonPathEvaluator.zip"
if [ -f "$file" ] ; then
    rm "$file"
fi

# Build the custom Java runtime from the Dockerfile
docker build -f Dockerfile --progress=plain -t lambda-custom-runtime-minimal-jre-19 .

# Extract the JsonPathEvaluator.zip from the Docker environment and store it locally
docker run --rm --entrypoint cat lambda-custom-runtime-minimal-jre-19 JsonPathEvaluator.zip > JsonPathEvaluator.zip
