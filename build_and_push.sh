set -e
./gradlew clean bootJar
docker buildx build --platform linux/arm64,linux/amd64 -t xiahaohai123/goodwillghservice:latest --push .
