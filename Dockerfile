# Use a 64-bit Debian Buster base image
FROM debian:buster

# Set environment variables for Java
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Install necessary packages
RUN apt-get update && \
    apt-get install -y openjdk-11-jre maven libasound2-dev libv4l-dev

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Install the ZegoExpressEngine JAR in the local Maven repository
RUN mvn install:install-file -Dfile=ZegoExpressEngine.jar -DgroupId=im.zego.express -DartifactId=ZegoExpressEngine -Dversion=1 -Dpackaging=jar

# Build the application
RUN mvn clean install

# Set the entry point
ENTRYPOINT ["java", "-jar", "target/livestream-moderation-1.0.0.jar"]
