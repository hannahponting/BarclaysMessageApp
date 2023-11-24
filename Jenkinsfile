pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
         stage('Test') {
             steps {
                 sh 'mvn test'
             }
             post {
                 always {
                      junit 'target/surefire-reports/*.xml'
                 }
             }
         }
                 stage('SonarQube Analysis') {
                             steps {
                                 script {
                                     withSonarQubeEnv('sonarqube') {
                 //                        sh "${tool('sonar-scanner')}/bin/sonar-scanner -Dsonar.projectKey=myProjectKey -Dsonar.projectName=myProjectName"
                                         sh 'mvn clean package sonar:sonar'
                                     }
                                 }
                             }
                         }

               stage ('OWASP Dependency-Check Vulnerabilities') {
                                   steps {
                                       dependencyCheck additionalArguments: '''
                                           --out "./"
                                           --scan "./"
                                           --nvdApiKey "e88f4b9e-bf01-410a-8351-e1089ceaafcd"
                                           --noupdate
                                           --format "ALL"
                                           --prettyPrint''', odcInstallation: 'OWASP-DC'
                                       dependencyCheckPublisher pattern: 'dependency-check-report.xml'
                                   }
                       }

                 stage("Quality gate") {
                             steps {
                                  waitForQualityGate abortPipeline: true
                               }
                           }


         stage('Deliver') {
             steps {
             echo 'Deployment Successful!!'
                // sh './jenkins/scripts/deliver.sh'
             }
        }
    }
}