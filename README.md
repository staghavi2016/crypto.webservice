# Not So Useful Webservice

How to start the webservice application
---
1. Install JDK (tested on openjdk version "18.0.1" 2022-04-19) and maven (Apache Maven 3.8.5)
2. Set $JAVA_HHOME
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
6. To check that your application is running enter url `http://localhost:8080`
7. Run UnitTests
```
   mvn test
```
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
10. 


---

To see your applications health enter url `http://localhost:8081/healthcheck`

Assumptions & Limitations:
---
1. JCE provider is leveraged for symmetric crypto (AES256) used for this implementation. 
2. Only 1 key is provisioned to be used for encryption and decryption based on requirements. In order to
provision it once, leveraged singleton design pattern for CryptoSVCImpl API.
3. AES/CBC/PKCS5Padding is used for both encryption and decryption; however, any other modes could be configured through config.yml
4. In order to calculate moving average and standard deviation on streaming data, 
Welford's online algorithm is implemented. (https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Welford's_online_algorithm)
5. 

