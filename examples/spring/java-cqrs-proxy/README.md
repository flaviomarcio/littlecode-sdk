# Documentação do componente

Esse projeto é utilizado para recebimento de request e consumo de filas

## Requirements

For building, test and execution the application you need:

- [OpenJDK 17](https://openjdk.org/projects/jdk/17/)
- [Maven 3](https://maven.apache.org)
- [Intellij](https://www.jetbrains.com/pt-br/idea/)
- [Spring Tools](https://spring.io/tools)

### Instalação

- [OpenJDK 17](https://openjdk.org/projects/jdk/17/)

```bash
#!/bin/bash

sudo apt update
sudo apt install -y openjdk-17-jre openjdk-17-jdk
```

- [Maven 3](https://maven.apache.org)

```bash 
#!/bin/bash

sudo apt update
sudo apt install -y maven
```

- [Intellij](https://www.jetbrains.com/pt-br/idea/) Community

```bash 
#!/bin/bash

sudo snap refresh
sudo snap install intellij-idea-community --classic
```

- [Intellij](https://www.jetbrains.com/pt-br/idea/) Ultimate

```bash 
#!/bin/bash

sudo snap refresh
sudo snap install intellij-idea-ultimate --classic
```

## Definições de arquitetura

Para acessar a rquitetura de referência
acesse: [arquitetura de referência](https://github.com/flaviomarcio/architecture-docs/blob/master/README.md)

## Recomendações

Para exemplos e **POCs** use como referências:

- [Baeldung](baeldung.com/)

## POM/YML

Arquivo de configuração do [Maven 3](https://maven.apache.org) e
aplicações [OpenJDK 17](https://openjdk.org/projects/jdk/17/)

- **pom.xml** é o arquivo de configuração dos pacotes do **MANVEN**
- **application.yml** é o arquivo de configuração da aplicação **JAVA**
- Manter nome da aplicação como [app] e ou [application].
- Manter a classe principal da aplicação [Application] e para testes [ApplicationTests]
- Utilizar templates como origem do [application.yml]
- Sempre o uso nativo dos componentes do spring.
- Negociar a entrada de novas **libs**, **packages**, **dependências**

## **Controllers**

Controller é o **Adapter** http para a aplição API

- Manter nome como **[Controller]**.
- Utilizar apenas um **[Controller]** por aplicação.
- Evitar sem utilizar multiplos controlles, logo existe uma grande chançe desta necessidade resultando em outro
  microserviço, ex:
    - Aqui vamos um app com dois controllers /customer e /sales.
        - Repositorio **App1**:
          ```
          paths app 1: 
              /customer/*
              /sales/*
          ```
    - Estes são contextos diferentes de funcionalidade e sendo assim são necessários dois microserviços diferentes,
      sendo assim:
        - Repositorio **App1**:
          ```
            Paths app 1: 
                /customer/*
          ```
        - Repositorio **App2**:
          ```
          Paths app 2: 
              /sales/*
          ```
- Havendo necessidade de vários controllers sempre iniciar conversa com **[TO]**, **[LT]** para verificar necessidade da
  criação de um novo componente.
- Para métodos **HTTP** evitar uso de @PathVariable considerar sempre como parâmetro.
- Métodos **GET** devem preferencialmente ser transações **ReadOnly** no banco de dados
- Métodos **DELETE** devem preferencialmente ser transações **Insert/Update**, as ações de [**update|delete**] deve ser
  evitada sempre no banco de dados.
- Devem servir apenas para receber requisições **[HTTP/HTTPS]** não podendo ter qualquer tipo de verificação e apenas
  dando acesso as classes anotadas como **[@Service]**.
- Os serviços devem ser com **[HTTP]** simples, logo o tratamento para **[HTTPS]** é feito no **[proxy]** ou *
  *[ApiGateway]**
- A camada de segurança deve ser implementada sempre em aplicações que são exportas, como API para **[Frontend]** ou *
  *[Backend]** para **[Terceiros]**.

## **Consumers**

Consume é o **Adapter** http para a aplição consumidora de filas

- Manter nome como **[Controller]**.
- Utilizar apenas um **[Controller]** por aplicação.
- Multiplos consumers
- Evitar ao maximo, logo existe uma grande chançe desta necessidade resultando em outro microserviço, ex:
    - Aqui vamos um app com dois controllers /customer e /sales.
        - Repositorio **App1**:
            ```
            Queue app 1: 
                queue-customer
                queue-sales
            ```
    - Estes são contextos diferentes de funcionalidade e sendo assim são necessários dois microserviços diferentes,
      sendo assim:
        - Repositorio **App1**:
            ```
            Queue app 1: 
                queue-customer
            ```
        - Repositorio **App2**:
            ```
               Queue app 2: 
                   queue-sales
            ```
- Havendo necessidade de vários **[Consumers]** sempre iniciar conversa com **[TO]**, **[LT]** para verificar
  necessidade da criação de um novo componente.
- Devem servir apenas para receber requisições **[Mensageiria]** não podendo ter qualquer tipo de verificação e apenas
  dando acesso as classes anotadas como **[@Service]**.
    - Os tipos de **[Mensageiria]** desde que disponiveis os serviços os tipos:
        - AMQP
        - SQS
        - SQN
        - Kafka
        - MQTT para **[iot]**

## **Services**

Services são classes preparadas para conter a regra de negócio do microserviço.

- Manter nome como **[Service]** deste que a aplicação não tenha várias estrategias diferentes,
    - ex:
        - **SalesService**
        - **BillingService**

## **JPA** e **MVC**

JPA é a definição de como trabalhar com maperamentos de objetos nos mais diversos tipos de bancos de dados. São classes
e anotações para definição das tabelas e repository e DAOs

- Manter nome orientado ao nome da tabela em questão, ex:
    - **SalesRepository.class**, sendo esta referente a tabela **sales**
        - **Models**
            - Representação fisica das tabelas no banco de dados
                - **Sales.class**, sendo este referente a tabela **sales**
        - **JpaRepositórios|DAOs**
            - Contém metodos para consulta dos dados das tabelas
                - **SalesDAO.class**, sendo este referente a tabela **sales**
        - **DTO**
            - Classes preparadas para retornar apenas os dados selecionados dos **models** ou consulta dos bancos de
              dados
            - **SalesDTO.class**, sendo este referente a tabela **sales**

## Ambiente Testing

Para configurar o o ambiente testing localmente você precisa ter acesso ao repositório de **pipelines**

- Repositório de [Pipelines](https://github.com/flaviomarcio/architecture-docs/blob/master/arquitetura-006-pipelines.md)
    - Neste poderão ser configudos:
    - Banco de dados
    - Mensageiria
    - Segurança
    - Outros

## Repositório

Command Line para clonagem via **GIT**

```bash
#!/bin/bash

git clone git@github.com:flaviomarcio/my-componente.git
```

## Test and Build

Command Line para clonagem via **GIT** e **build** do projeto

```bash
#!/bin/bash

cd projectDir #projeto clonado
mvn clean #remove compilados existentes
mvn test #executa tests unitários
mvn install #compila aplicação e faz a execução dos testes unitários
mvn install -DskipTests #compila aplicação sem a execução testes unitários
```

