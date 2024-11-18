# cc

## How to Run?
```bash
docker compose up
```

Docker will first build, which means fetching 3 gigs of data, building the Java packages and then going live. So, with a slow internet connection, this can take upto 10 minutes. If you see some services crashing and starting up again, that is completely normal since there are services depending on each other inside docker-compose.yaml. 


The reason for long build times inside the docker container is that I wanted to docker compose command to be completely automatized. If this was in a company setting, you would have a CI/CD step where the build happens first and the built image is published to a registry (such as a nexus), but instead, since this is going to be run locally, current setup makes the most sense.


This project has been compiled, run and tested on an M3 Macbook Air. Even though docker ought to be platform-independent, it is not guaranteed. If you run into any troubles, please contact me so that we can solve the problem together.

## General Architecture
There are 2 microservices:

User service -> Creates, updates, deletes and reads User information.

Credit service -> Responsible for credit payment and creation logic. Also fetches Installments from a database, checks if there are overdue payments, saves the information to redis and schedules a new task to update the amount.


Two services demonstrate different tech stacks. User service is written reactively with Quarkus, whereas Credit service uses Spring Boot's blocking architecture and IoC methodology. I think Java is headed towards the Quarkus way, with virtual threads coming in with Project Loom, so it is a framework I believe everyone should look out for.


The project uses a shared PostgreSQL database. I did not want to go Database-per-Microservice pattern because that would overload thing a bit too much. Redis is used for counting how many days are overdue for installments. You can also store that data with the database itself but fetching information for end-of-day operations from a memcache is a best practice. 

This project covers every single optional feature defined within the specifications. Everything _but_ the Scheduler system is blackbox tested. You can find OpenAPI gateways and PostgreSQL administration tools below the document. There are also some unit and integration tests here and there, although the coverage is severely lacking, and only added to demonstrate how testing works with Modern Java.

## Testing
There are OpenAPI documentations for the microservices.

credit-service: http://localhost:8080/q/swagger-ui/index.html

user-service: http://localhost:8081/q/swagger-ui/


Also, a pgadmin resides under http://localhost:8888, with credentials:
```bash
username: user@example.com
password: string
```

To add a new server to the pgadmin, use the following information:
```bash
host: host.docker.internal
username: postgres
password: postgrespw
```

I've added some Unit tests to the Credit service and some Integration tests to the User service, although the coverage is lacking. The reasoning behind that is I wanted to show that I can handle how to manage writing tests, although covering some 80 percent of the whole codebase did feel redundant at this stage.

## Further Remarks

I wanted the Scheduling to be done with a Redis pub/sub interface, where the producer publishes a key of installmentId and a value of newAmount. However, time constraints did not allow me to do so.


Entire project is written some 8 hours. Most time consuming tasks was dockerizing Java applications without using a build tool plugin. The reason behind not using one is I wanted to orchestrate everything with a single docker compose command, instead of chaining bunch of maven and docker calls and then using docker compose. As stated previously, build times are atrocious.