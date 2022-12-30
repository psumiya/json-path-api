#!/bin/sh

# Remove a previously created custom runtime
file="json-path-api-1.0-SNAPSHOT.zip"
if [ -f "$file" ] ; then
    rm "$file"
fi

# Build the custom Java runtime from the Dockerfile
docker build -f Dockerfile --progress=plain -t lambda-custom-runtime-minimal-jre-19 .

# Extract the runtime.zip from the Docker environment and store it locally
docker run --rm --entrypoint cat lambda-custom-runtime-minimal-jre-19 json-path-api-1.0-SNAPSHOT.zip > json-path-api-1.0-SNAPSHOT.zip
