node(''){
    
	stage('checkout'){
		echo 'github checkout'
		bat '''
			set http_proxy=
			set https_proxy=
			set no_proxy=''
		'''
		bat label: '', script:"set"
		git branch: '', credentialsId: '', url:''
	}
	//change proxy only when bypassing firewall
	stage('Dependencies installation'){
		bat '''
			set http_proxy=
			set https_proxy=
			set no_proxy=
		'''
		bat label: '', script:"set"
		bat '''
		flutter pub get 
		'''
	}
	
	stage('Building flutter web'){
		bat '''
		flutter build web --web-renderer=canvaskit --dart-define=FLUTTER_WEB_CANVASKIT_URL=/canvaskit/
		'''
	}
	
	stage('Archive the deployment file'){
		bat label: 'ZipFile', script: '"%zipexe%" a -r "%WORKSPACE%\\archive.zip" "%WORKSPACE%\\build\\web\\*.*"'
	}
	//set nexus repo IP
	stage('Upload to nexus'){
	   def nexusRepo = ''
	   def nexusURL=''
	   nexusArtifactUploader artifacts: [[artifactId: nexusRepo, classifier: '', file: 'archive.zip', type: 'zip']], credentialsId: '', groupId: '', nexusUrl: nexusURL, nexusVersion: 'nexus3', protocol: 'http', repository: nexusRepo, version: 'archive'
		
		}
  //fill in the details of variables
    stage('Whitesource Inspect'){
			
			def Github_URL= ''
			def ref= ''
			def modName= ''
			build job:'', parameters: [string(name: 'Github_Url', value: "$Github_URL"), string(name: 'Branch', value: "$ref"), string(name: 'modName', value: "$modName")]
			
			}
		
	}
