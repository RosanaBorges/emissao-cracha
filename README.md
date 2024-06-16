# Emissão de Crachás

Este é um projeto Spring Boot para uma aplicação básica de emissão de crachás de funcionários para impressão, utilizando PostgreSQL como banco de dados e com tratamento de exceções implementado. O front-end é construído utilizando Thymeleaf.

## Configuração do Ambiente

- **Java**: JDK 17
- **Banco de Dados**: PostgreSQL
- **Build Tool**: Maven

## Dependências do Projeto

Este projeto utiliza as seguintes dependências Maven:

- `spring-boot-starter-data-jpa`: Suporte ao Spring Data JPA para persistência de dados.
- `spring-boot-starter-thymeleaf`: Para renderização de templates HTML.
- `spring-boot-starter-validation`: Para validação de beans.
- `spring-boot-starter-web`: Para construção de aplicativos da web, incluindo RESTful APIs.
- `spring-boot-devtools`: Ferramentas de desenvolvimento para reinicialização automática e outros utilitários de desenvolvimento.
- `postgresql`: Driver JDBC para PostgreSQL.
- `lombok`: Biblioteca para geração automática de código Java boilerplate.
- `h2`: Banco de dados em memória para testes.
- `spring-boot-starter-test`: Dependências para testes unitários e de integração.
- `jakarta.validation`: API de validação para Java Bean Validation.
- `mockito-core`: Para criação de mocks em testes.

## Executando o Projeto

Para executar o projeto localmente, você pode usar a classe principal `EmissaoCrachaApplication.java`. A aplicação estará acessível em `http://localhost:8080`.

```sh
mvn clean install
mvn spring-boot:run
