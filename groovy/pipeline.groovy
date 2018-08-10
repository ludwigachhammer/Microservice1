import groovyx.net.http.*
    
node {

    deleteDir()

    stage('Sources') {
        checkout([
                $class           : 'GitSCM',
                branches         : [[name: "refs/heads/master"]],
                extensions       : [[$class: 'CleanBeforeCheckout', localBranch: "master"]],
                userRemoteConfigs: [[
                                            credentialsId: 'cbf178fa-56ee-4394-b782-36eb8932ac64',
                                            url          : "https://github.com/Nicocovi/MS-Repo"
                                    ]]
                ])
    }
 

    dir("") {
        
        stage("Build"){
            sh "gradle build"
        }

        stage('Deploy') {
            def branch = ['master']
            def name = "SpringMS1"
            def path = "build/libs/gs-spring-boot-0.1.0.jar"
            def manifest = "manifest.yml"
            
               if (manifest == null) {
                throw new RuntimeException('Could not map branch ' + master + ' to a manifest file')
               }
               withCredentials([[
                                     $class          : 'UsernamePasswordMultiBinding',
                                     credentialsId   : '98c5d653-dbdc-4b52-81ba-50c2ac04e4f1',
                                     usernameVariable: 'CF_USERNAME',
                                     passwordVariable: 'CF_PASSWORD'
                             ]]) {
                sh 'cf login -a https://api.run.pivotal.io -u $CF_USERNAME -p $CF_PASSWORD --skip-ssl-validation'
                sh 'cf target -o nico0205-org -s MA'
                sh 'cf push sping-ms1 -f '+manifest+' --hostname '+name+' -p '+path
            }
        }
        /*
        stage("Push Documentation"){
            
            http.request(POST) {
                uri.path = '192.168.99.100:9123'
                body = [name: name, domain: 'XXX', applicationresposible: 'XXX']
                requestContentType = ContentType.JSON

                response.success = { resp ->
                    println "Success! ${resp.status}"
                }
                response.failure = { resp ->
                    println "Request failed with status ${resp.status}"
                }
            }
        }
        */
    }

}
