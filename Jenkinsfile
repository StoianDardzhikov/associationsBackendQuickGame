pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'asso-bg'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/StoianDardzhikov/associationsBackendQuickGame.git'
            }
        }

        stage('Build') {
            steps {
                sh '''
    # 1. Force the environment strictly to Java 17
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
    export PATH=$JAVA_HOME/bin:$PATH
    
    # 2. Verify the fix
    java -version
    javac -version
    
    # 3. Give execution permissions to the Maven wrapper
    chmod +x mvnw
    
    # 4. Use the wrapper (./mvnw) instead of system Maven (mvn)
    ./mvnw -B -f pom.xml clean install -DskipTests
'''
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${env.DOCKER_IMAGE}:${env.BUILD_ID} ."
                }
            }
        }

        stage('Deploy Docker Container') {
            steps {
                script {
                    // Stop and remove any existing container named spring-boot-app
                    sh 'docker stop asso-bg || true'
                    sh 'docker rm asso-bg || true'

                    // Run the new Docker container, mapping port 8080 of the container to port 8081 on the host
                    docker.image("${env.DOCKER_IMAGE}:${env.BUILD_ID}").run('-p 8082:8080 --name asso-bg')
                }
            }
        }
    }

    post {
        always {
            cleanWs()  // Clean up the workspace after the build
        }
    }
}
