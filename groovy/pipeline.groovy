def callPost(String urlString, String queryString) {
    def url = new URL(urlString)
    def connection = url.openConnection()
    connection.setRequestMethod("POST")
    connection.doOutput = true
    //connection.setRequestProperty("Accept-Charset", "UTF-8")
    connection.setRequestProperty("Content-Type", "application/json")

    def writer = new OutputStreamWriter(connection.outputStream)
    writer.write(queryString.toString())
    writer.flush()
    writer.close()
    connection.connect()

    new groovy.json.JsonSlurper().parseText(connection.content.text)
}
node {
    /*
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
    */

    dir("") {
        /*
        stage("Build"){
            sh "gradle build"
        }

        stage('Deploy') {
            def branch = ['master']
            def name = "sping-microservice1"
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
                sh 'cf target -o ga72hib-org -s masterarbeit'
                sh 'cf push sping-microservice1 -f '+manifest+' --hostname '+name+' -p '+path
            }
        }
        */
        stage("Push Documentation"){
            println callPost("http://192.168.99.100:9123/document", "{\"id\": \"cjkbvajs1234\", \"name\": \"ServiceNow\", \"owner\": \"Martin\", \"description\": \"bla\", \"short_name\": \"serviceAZ12345\", \"type\": \"service\"}") //Include protocol
        }//stage
        
    }

}
