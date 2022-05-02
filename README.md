# Not So Useful Webservice

How to start the webservice application
---
1. Install JDK (tested on openjdk version "18.0.1" 2022-04-19) and maven (Apache Maven 3.8.5)
2. Set $JAVA_HOME
```
   export JAVA_HOME=<Path to JDK folder>/openjdk/18.0.1  
```
3. Clone this repo:
```  
   git clone https://github.com/staghavi2016/crypto.webservice.git
   cd crypto.webservice
```
4. Run `mvn clean install` to build your application
5. Start application with `$JAVA_HOME/bin/java -jar $HOME/<PATH TO CLONED REPO>/target/crypto.webservice-1.0-SNAPSHOT.jar server config.yml`

Dockerized version 
---
6. Alternatively, build and run the dockerized version of the webservice following the steps below:
```
   ### Any changes made to the code requires rebuilding of the Docker (step 1 below) to test the changes
   1- Run docker build command on the cloned repo top directory where Dockerfile is with a tag you want to label the docker with
      docker build -t <INSERT-YOUR-TAG> .
      e.g., docker built -t crypto.webserviceV1.0 
   2- Two options to run the docker are:
   # Run locally in interactive mode
   docker run -p 8080:8081 -it crypto.webserviceV1.0
   # Run in detached mode (you will need to look to docker see the log/stdout stream)
   docker run -d -p 8080:8081 crypto.webserviceV1.0

   Hint:
   If you prefer to serve on port 6060 and 6061 on the host machine; however, do not want to take the time to change
   the default 8080 and 8081 on crypto.webservice, you could use -p 6060-6061:8080-8081
    
```

7. To check that your application is running enter url `http://localhost:8080`

8. In order to specifically run the unit tests, run:
```
   mvn test
```
you will see similar output on successful execution of current tests
```
[INFO] Results:
[INFO] 
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.907 s
[INFO] Finished at: 2022-05-01T08:43:11-07:00
[INFO] ------------------------------------------------------------------------
```  
8. To see defined metrics for all the resources check (e.g., push-and-recalculate-post-requests-timed,
   push-and-recalculate-post-requests-metered)
```
   http://localhost:8081/metrics?pretty=true
```
9. To access the rest of the available operational menu check:
```
   http://localhost:8081/
```
10. To see your healthcheck of the application enter url `http://localhost:8081/healthcheck`
---



Assumptions & Limitations:
---
1. JCE provider is leveraged for symmetric crypto (AES256) used for this implementation. 
2. Only 1 key is provisioned to be used for encryption and decryption based on requirements. In order to
provision it once, leveraged singleton design pattern for CryptoSVCImpl class instance; hence, one symmetric key will be generated only and used across.
3. AES/CBC/PKCS5Padding is used for both encryption and decryption; however, any other modes could be configured through config.yml
4. In order to calculate moving average and standard deviation on streaming data, 
Welford's online algorithm is implemented. (https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Welford's_online_algorithm)
5. Streaming statistics are accumulated and calculated towards one shared value for both moving average and moving standard deviation.
6. Streaming (moving) statistics are reset on application restart.
7. As per requirements, there is no client authentication nor server side TLS/SSL.
8. Base64 encoding is used for encoding and decoding encrypted data 
9. Accumulating moving statistics is designed to be ThreadSafe
10. Dropwizard framework and many of its features are leveraged to build this application. Here is the refrence: 
https://www.dropwizard.io/en/latest/getting-started.html 

Future Enhancements
---
    More logging and custom meaningful logs
    Custom error handling for various http error types
    More on input validation as Dropwizard validation annotatioons seem to have some deficiencies
    Comparing other crypto libraries (e.g, Bouncy Castle) and integrating a stronger one
    Containerization of the application natively through Maven
    Adding healthcheck probes for the application
    

