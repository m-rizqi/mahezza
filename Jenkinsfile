pipeline {
    agent any

    environment {
        KEYSTORE_PATH = '/keystore.jks'
        KEYSTORE_PASSWORD = 'password'
        KEY_ALIAS = 'alias'
        KEY_PASSWORD = 'password'
        ANDROID_HOME = "/home/muhammad_rizqi/Android/sdk/cmdline-tools/bin"
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

        stage('Setup Fastlane & Android SDK'){
            steps {
                sh 'bundle install'
                sh 'chmod +x gradlew'
                sh 'echo "y" | $ANDROID_HOME/sdkmanager --licenses --sdk_root=$ANDROID_HOME'
                sh '$ANDROID_HOME/sdkmanager "build-tools;33.0.1" "platforms;android-34" --sdk_root=$ANDROID_HOME'
            }
        }
        
        // stage('Run Lints'){
        //     steps {
        //         sh 'bundle exec fastlane lint'
        //     }
        // }

        // stage('Run Tests'){
        //     steps {
        //         sh 'bundle exec fastlane test'
        //     }
        // }
        
        stage('Build APK'){
            steps {
                sh 'bundle exec fastlane build_apk'
            }
        }

        // stage('Build AAB'){
        //     steps {
        //         sh 'bundle exec fastlane build_aab'
        //     }
        // }

        stage('Upload to Firebase App Distribution'){
            steps {
                sh 'cd $WORKSPACE'
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
