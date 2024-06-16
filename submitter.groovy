pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                // Add your build steps here
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                // Add your deployment steps here
            }
        }
        stage('Approval') {
            steps {
                script{
                    def approvedUsers = ['#put names of approved  user here']
                    def user = input(
                        message: 'Approve deployment?',
                        parameters: [],
                        submitterParameter: 'submitter'
                    )
                    if (approvedUsers.contains(user)) {
                        build job: 'test-dbms', wait:true
                    }

                    if (!approvedUsers.contains(user)) {
                        error "Unauthorized user: ${user}. Deployment approval denied."
                    }
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
