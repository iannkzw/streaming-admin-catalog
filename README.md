# Streaming Admin Catalog

Microserviço de administração de catálogo para uma plataforma de streaming, desenvolvido com **Clean Architecture** e **Domain-Driven Design (DDD)** em Java/Spring Boot.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Domínio](#domínio)
- [API REST](#api-rest)
- [Configuração e Execução](#configuração-e-execução)
- [Testes](#testes)
- [Banco de Dados](#banco-de-dados)

---

## Visão Geral

Serviço responsável pela gestão de categorias do catálogo de uma plataforma de streaming. Expõe uma API REST para criação, listagem, busca, atualização e exclusão lógica de categorias.

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 11+ | Linguagem principal |
| Spring Boot | 2.7.7 | Framework web e DI |
| Spring Data JPA | 2.7.x | Persistência |
| Undertow | - | Servidor embedded (substitui Tomcat) |
| MySQL | 5.7+ | Banco de dados de produção |
| H2 | - | Banco em memória para testes |
| Flyway | 9.11.0 | Versionamento de migrations |
| Vavr | 0.10.4 | Programação funcional (Either monad) |
| SpringDoc OpenAPI | 1.6.8 | Documentação Swagger |
| JUnit 5 | 5.9.1 | Testes unitários e de integração |
| Mockito | 4.5.1 | Mocking em testes |
| Gradle | 8+ | Build e gerenciamento de módulos |
| Docker Compose | - | Infraestrutura local |

---

## Arquitetura

O projeto segue **Clean Architecture** com separação em três módulos independentes, garantindo que as dependências apontem sempre para dentro (em direção ao domínio).

```
┌──────────────────────────────────────────────┐
│              INFRASTRUCTURE                  │
│  Controllers · JPA Entities · Gateways impl  │
│  Spring Boot · Flyway · MySQL                │
├──────────────────────────────────────────────┤
│               APPLICATION                    │
│       Use Cases · Commands · Outputs         │
├──────────────────────────────────────────────┤
│                 DOMAIN                       │
│   Entities · Aggregates · Gateway interfaces │
│      Validators · Value Objects              │
└──────────────────────────────────────────────┘
```

**Fluxo de dependências:**

```
infrastructure → application → domain
infrastructure → domain
```

**Padrões utilizados:**
- **Use Case Pattern** — cada operação de negócio em sua própria classe
- **Gateway Pattern** — abstração de persistência definida no domínio, implementada na infraestrutura
- **Railway-Oriented Programming** — uso de `Either<Notification, Output>` para tratamento funcional de erros
- **Notification Pattern** — acumulação de erros de validação sem uso de exceções para fluxo de controle

---

## Estrutura do Projeto

```
streaming-admin-catalog/
├── domain/
│   └── src/main/java/com/admin/catalog/domain/
│       ├── category/
│       │   ├── Category.java              # Aggregate root
│       │   ├── CategoryId.java            # Value object
│       │   ├── CategoryGateway.java       # Gateway interface
│       │   ├── CategoryValidator.java     # Regras de validação
│       │   └── CategorySearchQuery.java
│       ├── pagination/
│       │   └── Pagination.java
│       └── validation/
│           └── Notification.java          # Error accumulator
│
├── application/
│   └── src/main/java/com/admin/catalog/application/
│       └── category/
│           ├── create/    # CreateCategoryUseCase
│           ├── retrieve/  # GetCategoryByIdUseCase · ListCategoriesUseCase
│           ├── update/    # UpdateCategoryUseCase
│           └── delete/    # DeleteCategoryUseCase
│
├── infrastructure/
│   └── src/main/java/com/admin/catalog/infrastructure/
│       ├── api/
│       │   ├── Controllers/CategoryController.java
│       │   └── CategoryAPI.java           # Interface com anotações OpenAPI
│       ├── category/
│       │   ├── CategoryMySQLGateway.java  # Implementação do gateway
│       │   ├── CategoryJpaEntity.java
│       │   └── CategoryRepository.java
│       ├── configuration/                 # Beans Spring
│       └── Main.java
│
├── docker-compose.yml
├── build.gradle
└── settings.gradle
```

---

## Domínio

### Entidade: `Category`

| Campo | Tipo | Regras |
|---|---|---|
| `id` | UUID (String) | Gerado automaticamente |
| `name` | String | Obrigatório, 3–255 caracteres |
| `description` | String | Opcional, máx. 4000 caracteres |
| `isActive` | Boolean | Default: `true` |
| `createdAt` | Instant | Gerado automaticamente |
| `updatedAt` | Instant | Atualizado a cada modificação |
| `deletedAt` | Instant | `null` = ativo; preenchido = excluído logicamente |

### Casos de Uso

| Caso de Uso | Entrada | Saída |
|---|---|---|
| `CreateCategoryUseCase` | `CreateCategoryCommand` | `Either<Notification, CreateCategoryOutput>` |
| `GetCategoryByIdUseCase` | `String (id)` | `CategoryOutput` |
| `ListCategoriesUseCase` | `SearchQuery` | `Pagination<CategoryListOutput>` |
| `UpdateCategoryUseCase` | `UpdateCategoryCommand` | `Either<Notification, UpdateCategoryOutput>` |
| `DeleteCategoryUseCase` | `String (id)` | `void` |

---

## API REST

**Base URL:** `http://localhost:8080`

**Documentação interativa (Swagger):** `http://localhost:8080/swagger-ui.html`

### Endpoints

#### Criar categoria
```
POST /categories
Content-Type: application/json

{
  "name": "Filmes",
  "description": "Filmes de todos os gêneros",
  "is_active": true
}
```
Resposta: `201 Created` com header `Location: /categories/{id}`

---

#### Listar categorias
```
GET /categories?search=&page=0&perPage=10&sort=name&dir=asc
```
Resposta: `200 OK`
```json
{
  "current_page": 0,
  "per_page": 10,
  "total": 1,
  "items": [
    {
      "id": "uuid",
      "name": "Filmes",
      "description": "Filmes de todos os gêneros",
      "is_active": true,
      "created_at": "2024-01-01T00:00:00Z",
      "deleted_at": null
    }
  ]
}
```

---

#### Buscar categoria por ID
```
GET /categories/{id}
```
Resposta: `200 OK` | `404 Not Found`

---

#### Atualizar categoria
```
PUT /categories/{id}
Content-Type: application/json

{
  "name": "Séries",
  "description": "Séries e minisséries",
  "is_active": true
}
```
Resposta: `200 OK` | `404 Not Found` | `422 Unprocessable Entity`

---

#### Deletar categoria
```
DELETE /categories/{id}
```
Resposta: `204 No Content` (exclusão lógica — `deletedAt` é preenchido)

---

### Formato de erros

```json
{
  "message": "Erro de validação",
  "errors": [
    { "message": "'name' must not be null" }
  ]
}
```

| Código | Significado |
|---|---|
| `422` | Erro de validação do domínio |
| `404` | Recurso não encontrado |
| `500` | Erro interno do servidor |

---

## Configuração e Execução

### Pré-requisitos

- Java 11+
- Docker e Docker Compose
- Gradle 8+ (ou use o wrapper `./gradlew`)

### 1. Subir o banco de dados

```bash
docker-compose up -d
```

Isso sobe um container MySQL 5.7 na porta `3306` com:
- **Database:** `adm_videos`
- **Usuário:** `root`
- **Senha:** `123456`

### 2. Executar a aplicação

```bash
./gradlew :infrastructure:bootRun
```

A aplicação inicia em `http://localhost:8080` com o profile `development`.

### 3. Build do projeto

```bash
./gradlew build
```

O JAR gerado fica em `build/libs/application.jar`.

### Profiles disponíveis

| Profile | Banco | Uso |
|---|---|---|
| `development` | MySQL (localhost:3306) | Desenvolvimento local |
| `test` | H2 (in-memory) | Testes automatizados |

---

## Testes

### Executar todos os testes

```bash
./gradlew test
```

### Executar testes de um módulo específico

```bash
./gradlew :domain:test
./gradlew :application:test
./gradlew :infrastructure:test
```

### Estrutura dos testes

- **Domain:** testes unitários das entidades e validadores
- **Application:** testes unitários dos use cases com gateway mockado (`Mockito`)
- **Infrastructure:** testes de integração com H2 in-memory (profile `test`)

Os use cases utilizam `Either` para resultados, permitindo assertions expressivas:

```java
// Sucesso
final var output = useCase.execute(command).get();

// Erro de validação
final var notification = useCase.execute(command).getLeft();
assertEquals(1, notification.getErrors().size());
```

---

## Banco de Dados

### Schema (gerenciado pelo Flyway)

```sql
CREATE TABLE categories (
    id          CHAR(36)        NOT NULL PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    description VARCHAR(4000),
    active      BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at  DATETIME(6)     NOT NULL,
    updated_at  DATETIME(6)     NOT NULL,
    deleted_at  DATETIME(6)     NULL
);
```

As migrations ficam em:
```
infrastructure/src/main/resources/db/migration/
├── V1__initial.sql
└── U1__Initial.sql   # undo migration
```

### Connection Pool (HikariCP)

| Parâmetro | Valor |
|---|---|
| `maximum-pool-size` | 20 |
| `minimum-idle` | 10 |
| `connection-timeout` | 250ms |
| `max-lifetime` | 600.000ms |
