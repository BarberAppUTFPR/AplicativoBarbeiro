# User Service

Microsserviço de gerenciamento de usuários para o sistema BarberApp.

## Descrição

Este serviço é responsável por:
- Gerenciamento de perfis de usuário (CRUD)
- Atualização de dados de usuário
- Busca e listagem de usuários
- Consulta de informações de usuário

## Tecnologias

- Java 17
- Spring Boot 3.5.6
- Spring Security
- Spring Data JPA
- MySQL
- Flyway
- Swagger/OpenAPI
- Spring Cloud OpenFeign

## Porta

O serviço roda na porta **8081**

## Configuração

### Banco de Dados

O serviço utiliza MySQL. Configure as credenciais em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:4000/barberApp_users
spring.datasource.username=root
spring.datasource.password=root
```

## Endpoints

Todos os endpoints requerem autenticação via Bearer Token (exceto Swagger e Actuator).

- `GET /user` - Listar todos os usuários (paginado)
- `GET /user/{id}` - Buscar usuário por ID
- `PUT /user/{id}` - Atualizar dados do usuário
- `DELETE /user/{id}` - Deletar usuário

## Swagger

Acesse a documentação interativa em: `http://localhost:8081/swagger-ui.html`

## Executar

```bash
cd C:\dev_projects\user-service
mvn spring-boot:run
```

## Arquitetura

```
user-service/
├── models/          # User (sem UserDetails)
├── dtos/            # GetUsersDTO, UpdateDTO
├── repositories/    # UserRepository
├── services/        # UserService
├── controllers/     # UserController
└── infra/
    ├── security/    # JwtValidationFilter, SecurityConfig
    ├── config/      # OpenApiConfig
    └── exception_handler/  # GlobalExceptionHandler
```

## Integração com Auth Service

Este serviço valida tokens JWT recebidos nas requisições. A validação pode ser feita:
1. Usando chaves públicas compartilhadas
2. Chamando o Auth Service via OpenFeign

URL do Auth Service: `http://localhost:8082`

## Diferenças do Auth Service

- Não possui lógica de autenticação
- Não gera tokens JWT
- Modelo User simplificado (sem senha e UserDetails)
- Foco em operações CRUD de usuários
