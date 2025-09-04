pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'stoyan-bg-frontend'  // Name of the Docker image
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'master', url: 'https://github.com/StoianDardzhikov/associationsBackendQuickGame.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image with a unique tag using the BUILD_ID
                    docker.build("${env.DOCKER_IMAGE}:${env.BUILD_ID}")
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
                    docker.image("${env.DOCKER_IMAGE}:${env.BUILD_ID}").run('-p 8082:80 --name asso-bg')
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