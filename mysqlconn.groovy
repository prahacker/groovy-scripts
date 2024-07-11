pipeline {
    agent any

    environment {
        DB_HOST = '<ip>'
        DB_PORT = '3306'
        DB_NAME = '<database name>'
        DB_USER = '<usrname>'
        DB_PASS = '<passwd>'
    }

    stages {
        stage('Extract Variable from MySQL') {
            steps {
                script {
                    // Execute MySQL query and capture the output
                    def queryResult = sh(script: """
                        mysql -h ${DB_HOST} -P ${DB_PORT} -u${DB_USER} -p${DB_PASS} -e "USE <DB_NAME>; SELECT LangName FROM jenkins LIMIT 1;" -s -N
                    """, returnStdout: true).trim()

                    // Set the result as a Jenkins environment variable
                    env.MYSQL_RESULT = queryResult
                }
            }
        }
        
        stage('Use Variable') {
            steps {
                echo "The value extracted from MySQL is: ${env.MYSQL_RESULT}"
                // Use the MYSQL_RESULT variable in your pipeline script as needed
            }
        }
    }
}
