
irrigitionsystem

As a irrigation system which helps the automatic irrigation of agricultural lands without human intervention, system has to
be designed to fulfil the requirement of maintaining and configuring the plots of land by the irrigation time slots and the
amount of water required for each irrigation period.
The irrigation system should have integration interface with a sensor device to direct letting the sensor to irrigate based on the configured time slots/amount of water.

technologies

use spring boot webflux to create rest apis

maven to bulid the project : mvn clean install to bulid the project

Spring JPA, liquibase and postgres for DB 

spring.datasource.url=jdbc:postgresql://localhost:5432/irrigation
spring.datasource.username=postgres
spring.datasource.password=test


spring kafka to call sensor device check if available

Kafka Connection 

application.kafka.bootstrap.server=localhost:9092

application.kafka.sync.group.id=grp-sensor-device-tmp
#consumer
application.sensor-device.response.topic=sensor-device-response-internal

#producer
application.sensor-device.request.topic=sensor-device-request-internal

for test use kadeck to mock resoponse produced from sensor device 

TODO: In the future implemenet sensor device producer


Swagger_UI
Use http://localhost:8080/webjars/swagger-ui/index.html to list apis


