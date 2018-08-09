node {

    deleteDir()

    stage('Sources') {
        checkout([
                $class           : 'GitSCM',
                branches         : [[name: "refs/heads/master"]],
                extensions       : [[$class: 'CleanBeforeCheckout', localBranch: "master"]],
                userRemoteConfigs: [[
                                            credentialsId: '2119a63f-6ae3-4d37-b498-5e2e1422c378',
                                            url          : "https://github.com/Nicocovi/MS-Repo"
                                    ]]
                ])
    }
 

    dir("") {
        
        stage("Build"){
            sh "gradlew build"
        }

        stage('Deploy') {
            def branch = ['master']
            def manifest = 'manifest.yml'
               if (manifest == null) {
                throw new RuntimeException('Could not map branch ' + env.Branch + ' to a manifest file')
               }
               withCredentials([[
                                     $class          : 'UsernamePasswordMultiBinding',
                                     credentialsId   : 'ID',
                                     usernameVariable: 'CF_USERNAME',
                                     passwordVariable: 'CF_PASSWORD'
                             ]]) {
                sh 'cf login -a run.pivotal.io -u $CF_USERNAME -p $CF_PASSWORD --skip-ssl-validation'
                sh 'cf push ms1'
            }
        }
    }

}
