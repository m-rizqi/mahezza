pipeline {
    agent any

    environment {
        KEYSTORE_PATH = '/keystore.jks'
        KEYSTORE_PASSWORD = 'password'
        KEY_ALIAS = 'alias'
        KEY_PASSWORD = 'password'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Set local.properties'){
            steps {
                sh 'if [ ! -f "local.properties" ]; then touch local.properties; fi'
                sh 'echo "sdk.dir=$ANDROID_HOME" > local.properties'
            }
        }
        
        stage('Run Lints'){
            steps {
                sh 'fastlane lint'
            }
        }

        stage('Run Tests'){
            steps {
                sh 'fastlane test'
            }
        }
        
        stage('Build APK'){
            steps {
                sh 'fastlane build_apk'
            }
        }

        stage('Build AAB'){
            steps {
                sh 'fastlane build_aab'
            }
        }

        stage('Upload to Firebase App Distribution'){
            steps {
                sh 'fastlane upload_to_firebase'
            }
        }
    }

    post {
        failure {
            emailext body: 'Something went wrong with the build.',
                recipientProviders: [culprits(), developers()],
                subject: 'Flutter Build Failed',
                to: 'muhammad.rizqi@divistant.com'
        }
    }
}
