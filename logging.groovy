pipeline {
    agent any
    tools {
        maven 'MAVEN3'
        jdk 'OracleJDK11'
    }
    environment {
        DB_HOST = '#ip'
        DB_PORT = '3306'
        DB_USER = '#username'
        DB_PASS = '#password'
    }
    stages {
        stage('Fetch') {
            steps {
                git branch: '#github branch', url: '#github url'
            }
        }
        stage('Build') {
            steps {
                script {
                    def startTime = new Date().format("yyyy-MM-dd HH:mm:ss")
                    env.BUILD_START_TIME = startTime  
                }
                script {
                    try {
                        // Run Maven build
                        sh 'mvn clean install -DskipTests'
                        // If Maven build is successful
                        currentBuild.result = 'SUCCESS'
                    } catch (Exception e) {
                        // If Maven build fails
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                def endTime = new Date().format("yyyy-MM-dd HH:mm:ss")
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                sh """
                    mysql -h ${DB_HOST} -P ${DB_PORT} -u ${DB_USER} -p${DB_PASS} -e \\
                    "USE jen; INSERT INTO log (BuildStart, BuildEnd, BuildStatus ) VALUES('${env.BUILD_START_TIME}','${endTime}', '${buildStatus}');"
                """
            }
        }
    }
}
