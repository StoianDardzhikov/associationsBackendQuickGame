pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'stoyan-bg-frontend'  // Name of the Docker image
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'master', url: 'https://github.com/StoianDardzhikov/stoyan-bg-frontend.git'
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
                    sh 'docker stop stoyan-bg-frontend || true'
                    sh 'docker rm stoyan-bg-frontend || true'

                    // Run the new Docker container, mapping port 8080 of the container to port 8081 on the host
                    docker.image("${env.DOCKER_IMAGE}:${env.BUILD_ID}").run('-p 8081:80 --name stoyan-bg-frontend')
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