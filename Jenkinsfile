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

        stage('Install Fastlane'){
            steps {
                sh 'bundle install'
            }
        }
        
        stage('Run Lints'){
            steps {
                sh 'bundle exec fastlane lint'
            }
        }

        stage('Run Tests'){
            steps {
                sh 'bundle exec fastlane test'
            }
        }
        
        stage('Build APK'){
            steps {
                sh 'bundle exec fastlane build_apk'
            }
        }

        stage('Build AAB'){
            steps {
                sh 'bundle exec fastlane build_aab'
            }
        }

        stage('Upload to Firebase App Distribution'){
            steps {
                sh 'bundle exec fastlane upload_to_firebase'
            }
        }
    }

    post {
        failure {
            emailext body: 'Something went wrong with the build.',
                recipientProviders: [culprits(), developers()],
                subject: 'Android Build Failed',
                to: 'muhammad.rizqi@divistant.com'
        }
    }
}
