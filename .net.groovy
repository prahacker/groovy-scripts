pipeline {
    agent {
        label 'Windows-slave'  // Ensure this matches the label of your Windows slave node
    }

    environment {
        // Set necessary environment variables for MSBuild
        VSINSTALLDIR = 'C:\\Program Files (x86)\\Microsoft Visual Studio\\2022\\BuildTools'
        MSBUILDDIR = "${VSINSTALLDIR}\\MSBuild\\Current\\Bin"
        PATH = "${MSBUILDDIR};C:\\Program Files\\dotnet;C:\\Tools\\NuGet;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the source code...'
                git url: 'https://github.com/dotnet/samples.git', branch: 'main'
            }
        }
        stage('NuGet Restore') {
            steps {
                echo 'Restoring NuGet packages...'
                bat 'nuget restore async/async-and-await/cs/AsyncFirstExampleCS.sln'
            }
        }
        stage('Build') {
            steps {
                echo 'Building the .NET project...'
                withEnv(["PATH+MSBUILD=C:\\Program Files (x86)\\Microsoft Visual Studio\\2022\\BuildTools\\MSBuild\\Current\\Bin"]) {
                    bat '"C:\\Program Files (x86)\\Microsoft Visual Studio\\2022\\BuildTools\\MSBuild\\Current\\Bin\\MSBuild.exe" async/async-and-await/cs/AsyncFirstExampleCS.sln /p:Configuration=Release'
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Running unit tests...'
                withEnv(["PATH+MSBUILD=C:\\Program Files (x86)\\Microsoft Visual Studio\\2022\\BuildTools\\MSBuild\\Current\\Bin"]) {
                    bat 'dotnet test async/async-and-await/cs/AsyncFirstExampleCS.sln --configuration Release'
                    

                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded!'
            archiveArtifacts artifacts: '**/*.dll', allowEmptyArchive: true
        }
        failure {
            echo 'Pipeline failed!'
            // Optionally archive failed build logs or artifacts
        }
    }
}



