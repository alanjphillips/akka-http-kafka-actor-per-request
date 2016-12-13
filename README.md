Actor-Per-Http-Request: Service Http requests via Kafka command and response messages
=====================================================================================

Completed basic Http request -> output kafka -> input kafka -> http response sequence. Proof of concept implementation, not best practices.

Install docker, docker-machine and docker-compose

1) Connect to 'default' machine, see docker docs on how to create machine in virtualbox

Actor-Per-Http-Request> docker-machine start default

Actor-Per-Http-Request> eval "$(docker-machine env default)"

2) CD into project and use SBT to build and publish to local Docker repo:

Actor-Per-Http-Request> sbt clean docker:publishLocal

3) Run docker compose to launch

Actor-Per-Http-Request> docker-compose up

4) Use rest client such as Postman to send http request:

Uri: http://192.168.99.100:8081/service/work

Content-Type: application/json

body:

{
    "name": "test123"
}

5) To stop, use ctrl+c to kill process in terminal. To ensure all containers are stopped run:
Actor-Per-Http-Request> docker-compose down
