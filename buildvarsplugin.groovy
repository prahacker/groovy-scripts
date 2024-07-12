pipeline {
    agent any

    stages {
        stage('Initialize') {
            steps {
                script {
                    wrap([$class: 'BuildUser']) {
                        def buildUserName = env.BUILD_USER ?: 'N/A'
                        def buildUserId = env.BUILD_USER_ID ?: 'N/A'
                        
                        // Print the user information to confirm it's captured correctly
                        echo "Initialization: Build initiated by ${buildUserName} (${buildUserId})"

                        // Set environment variables using the script step
                        env.BUILD_USER_NAME = buildUserName
                        env.BUILD_USER_ID = buildUserId
                    }
                }
            }
        }

        stage('Use Build User Info - Stage 1') {
            steps {
                script {
                    echo "Stage 1: Build initiated by ${env.BUILD_USER_NAME} (${env.BUILD_USER_ID})"
                }
            }
        }

        stage('Use Build User Info - Stage 2') {
            steps {
                script {
                    echo "Stage 2: Build initiated by ${env.BUILD_USER_NAME} (${env.BUILD_USER_ID})"
                }
            }
        }
    }
}
