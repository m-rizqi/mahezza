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

        stage('Lint') {
            steps {
                sh 'flutter analyze'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'flutter test'
            }
        }

        stage('Integration Test') {
            steps {
                sh 'flutter drive --target=test_driver/app.dart'
            }
        }

        stage('Build APK') {
            steps {
                sh 'flutter build apk'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/build/app/outputs/flutter-apk/app-release.apk', allowEmptyArchive: true
                }
            }
        }

        stage('Build AAB') {
            steps {
                sh "$PATH build appbundle --release --build-number=1 --build-name=1.0.0"
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/build/app/outputs/bundle/release/app-release.aab', allowEmptyArchive: true
                }
            }
        }

        stage('Sign AAB') {
            steps {
                sh "jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore $KEYSTORE_PATH -storepass $KEYSTORE_PASSWORD $WORKSPACE/build/app/outputs/bundle/release/app-release.aab $KEY_ALIAS"
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
