# Start with an Amazon Linux 2 Container
FROM --platform=linux/amd64 amazonlinux:2

# Add the Amazon Corretto repository
RUN rpm --import https://yum.corretto.aws/corretto.key
RUN curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo

# Update the packages and install Amazon Corretto 11 and Zip
RUN yum -y update
RUN yum install -y java-11-amazon-corretto-devel zip

# Set Java 11 as the default
ENV JAVA_HOME /usr/lib/jvm/java-11-amazon-corretto/
RUN export JAVA_HOME

# Copy the current folder to the image and build the function with shadowJar gradle plugin
COPY . json-path-api
WORKDIR json-path-api
RUN ./gradlew clean shadowJar

RUN ls build

# Find JDK module dependencies dynamically from the uber jar
RUN jdeps -q \
    --ignore-missing-deps \
    --multi-release 11 \
    --print-module-deps \
    build/libs/json-path-api-1.0-SNAPSHOT-all.jar > jre-deps.info

# Create a slim Java 11 JRE which only contains the required modules to run the function
RUN jlink --verbose \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /jre11-slim \
    --add-modules $(cat jre-deps.info)

# Use Javas Application Class Data Sharing feature
# It creates the file /jre11-slim/lib/server/classes.jsa
RUN /jre11-slim/bin/java -Xshare:dump

# Package everything together into a custom runtime archive
WORKDIR /
COPY bootstrap bootstrap
RUN chmod 755 bootstrap
RUN cp json-path-api/build/libs/json-path-api-1.0-SNAPSHOT-all.jar json-path-api-1.0-SNAPSHOT-all.jar
RUN zip -r json-path-api-1.0-SNAPSHOT.zip \
    bootstrap \
    json-path-api-1.0-SNAPSHOT-all.jar \
    /jre11-slim
