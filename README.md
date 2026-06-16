# Exercicio Microservices

Projeto de estudo com dois microservicos Spring Boot que se comunicam de forma assincrona via RabbitMQ:

- `user`: recebe cadastro de usuario via API REST e publica evento de envio de email.
- `email`: consome o evento, envia o email e persiste o status do envio.

## Arquitetura

```mermaid
flowchart LR
		A[Cliente HTTP] -->|POST /users| B[user-service :8081]
		B -->|Salva usuario no PostgreSQL ms-user| C[(DB ms-user)]
		B -->|Publica mensagem em default.email| D[(RabbitMQ)]
		D -->|Consume mensagem| E[email-service :8082]
		E -->|Envia email SMTP| F[Servidor SMTP]
		E -->|Salva historico e status| G[(DB ms-email)]
```

## Stack

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- RabbitMQ (AMQP)
- PostgreSQL
- Spring Mail
- Maven Wrapper (`./mvnw`)

## Estrutura

```text
.
|-- user/
|   |-- src/main/java/com/brenokas/ms/user
|   |-- src/main/resources/application.properties
|   `-- pom.xml
|-- email/
|   |-- src/main/java/com/brenokas/ms/email
|   |-- src/main/resources/application.properties
|   `-- pom.xml
`-- README.md
```

## Pre-requisitos

- JDK 21
- Docker (recomendado para RabbitMQ e PostgreSQL) ou instalacoes locais equivalentes
- Maven (opcional, pois o projeto usa Maven Wrapper)
- Conta SMTP (exemplo atual: Gmail)

## Variaveis de ambiente

Defina as variaveis antes de subir os servicos:

| Variavel | Onde e usada | Exemplo |
|---|---|---|
| `DB_USER` | `user` e `email` | `postgres` |
| `DB_PASSWORD` | `user` e `email` | `postgres` |
| `RABBITMQ_ADDRESS` | `user` e `email` | `amqp://guest:guest@localhost:5672/` |
| `EMAIL` | `email` (spring.mail.username) | `seu-email@gmail.com` |
| `EMAIL_PASSWORD` | `email` (senha/app password) | `xxxx xxxx xxxx xxxx` |

Observacao: os dois servicos usam nomes diferentes para a URL do RabbitMQ (`RABBITMQ_ADDRESS` e `RABBIT_MQ_ADDRESS`). Defina ambas para evitar erro de inicializacao.

## Infra local rapida com Docker

### 1) Subir RabbitMQ

```bash
docker run -d --name rabbitmq \
	-p 5672:5672 -p 15672:15672 \
	rabbitmq:3-management
```

Painel RabbitMQ: `http://localhost:15672` (user/pass padrao: `guest` / `guest`)

### 2) Subir PostgreSQL

```bash
docker run -d --name postgres-ms \
	-e POSTGRES_USER=postgres \
	-e POSTGRES_PASSWORD=postgres \
	-p 5432:5432 \
	postgres:16
```

### 3) Criar bancos

```sql
CREATE DATABASE "ms-user";
CREATE DATABASE "ms-email";
```

## Como executar

### 1) Iniciar o servico de email

```bash
cd email
./mvnw spring-boot:run
```

### 2) Iniciar o servico de user

```bash
cd user
./mvnw spring-boot:run
```

## API

### Criar usuario

- Metodo: `POST`
- Endpoint: `http://localhost:8081/users`
- Body:

```json
{
	"name": "Breno",
	"email": "breno@email.com"
}
```

- Resposta esperada (`201 Created`):

```json
{
	"id": "uuid-gerado",
	"name": "Breno",
	"email": "breno@email.com"
}
```

## Fluxo de negocio

1. API `user` recebe o cadastro.
2. Usuario e persistido no banco `ms-user`.
3. Servico `user` publica mensagem na fila `default.email`.
4. Servico `email` consome a mensagem.
5. Servico `email` tenta enviar via SMTP.
6. Status (`SENT` ou `ERROR`) e salvo no banco `ms-email`.

## Testes

Executar em cada servico:

```bash
./mvnw test
```

## Problemas comuns

- Falha de SMTP no Gmail:
	- Use App Password (nao a senha normal da conta).
	- Verifique `EMAIL` e `EMAIL_PASSWORD`.
- Erro de conexao com RabbitMQ:
	- Verifique se a porta `5672` esta ativa.
	- Garanta que as duas variaveis (`RABBITMQ_ADDRESS` e `RABBIT_MQ_ADDRESS`) foram exportadas.
- Erro de banco:
	- Confirme criacao dos bancos `ms-user` e `ms-email`.
	- Confirme `DB_USER` e `DB_PASSWORD`.
