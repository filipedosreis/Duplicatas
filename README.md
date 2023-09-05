# CRDC - Desafio - Full Stack Java developer!

Projeto desenvolvido utilizando Java 17 e angular 16.
Foi utilizado banco de dados H2 para a persistência.

# Como Rodar

- Descompacte o arquivo .zip
- Na pasta raiz inicie o projeto Java com o commando
    - Compile o projeto: **.\gradlew build**
    - Suba o projeto: **.\gradlew bootRun**
- Para subir o projeto de FE rode o comando
    - O comando deve ser executado na pasta app **npm install**
    - E para subir o servidor  **npm start**

Apos isto o projeto deve estar disponível no endereço http://localhost:4000


## Banco de dados e API

Com a utilização do banco H2 ele fica disponível no endereço para consulta, http://localhost:8080/h2-console/ .
- User: **admin**
- Password: **admin**
- JDBC URL: **jdbc:h2:mem:duplicatasdb**
  A API pode ser acessada através do swagger disponível no endereço http://localhost:8080/swagger-ui/index.html

## Contato
Filipe Reis - filipedosreis@gmail.com