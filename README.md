# NEAT User's requests processing service

## Project description

This project is designed to predict the data of the presented regression model. The neuroevolutionary algorithm ["Neuroevolution of augmenting topologies"(PDF)](http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf) is used for forecasting.
The project is presented in a microservice architecture and includes the following components: 

1. [User's requests processing service](https://github.com/SkaaRJik/neatvue) <- You are here
2. [Data preprocessing service](https://github.com/SkaaRJik/neat-data-preprocessing)
3. [Prediction service](https://github.com/SkaaRJik/neat-executor)
4. [Graphical user interface](https://github.com/SkaaRJik/neatvue)
5. PostgresSQL 11
6. SMB protocol (Linux: Samba)
7. RabbitMQ 

## Service description
This service is designed to process user requests using the HTTP protocol. Includes an authorization and authentication system. The service sends data to other services through RabbitMQ and receives work results from them. It is the only component with database access. 

## Minimal system requirements

<ol>
<li>CPU: 2core 2 Ghz </li>
<li>RAM: 4Gb</li>
<li>Free disk space: 10GB</li>
</ol>

## Requirements

<ol>
<li>Java 11</li>
<li>Maven</li>
<li>RabbitMQ</li>
<li>Samba</li>
<li>PostgresSQL 11</li>
</ol>

## Running

<ol>
<li>Run RabbitMQ if it is not active</li>
<li>Run Samba if it is not active</li>
<li>Run PostgreSQL 11 if it is not active</li>
<li>Configure connections: src/main/resources/application.properties</li>
<li>Run build app</li>

`mvn spring-boot:build-image`

<li>After build - run server:</li>

`java -jar ./target/neatvue-0.0.1-SNAPSHOT.jar`

</ol>

## Samba configuration

See [samba instruction](https://github.com/SkaaRJik/neat-user-requests/blob/separate-frontend/src/main/resources/sh-scripts/samba-instructions.md)
