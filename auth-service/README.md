# Auth Service

Microsserviço de autenticação e autorização para o sistema BarberApp.

## Descrição

Este serviço é responsável por:
- Autenticação de usuários (login)
- Registro de novos usuários
- Geração e validação de tokens JWT
- Confirmação de email
- Recuperação de senha (forgot/reset password)
- Gerenciamento de roles e permissões

## Tecnologias

- Java 17
- Spring Boot 3.5.6
- Spring Security
- Spring Data JPA
- MySQL
- JWT (Auth0)
- Flyway
- Swagger/OpenAPI
- Spring Cloud OpenFeign

## Porta

O serviço roda na porta **8082**

## Configuração

### Banco de Dados

O serviço utiliza MySQL. Configure as credenciais em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:4000/barberApp_auth
spring.datasource.username=root
spring.datasource.password=root
```

### Chaves RSA

O serviço utiliza chaves RSA para assinar e validar tokens JWT. As chaves estão localizadas em:
- `src/main/resources/public_key.pem`
- `src/main/resources/private_key.pem`

## Endpoints Públicos

- `POST /auth/register` - Registrar novo usuário
- `POST /auth/login` - Fazer login
- `GET /auth/confirm-account?token=` - Confirmar conta via email
- `GET /auth/request-confirmation?id=` - Solicitar novo email de confirmação
- `POST /auth/forgot-password` - Solicitar recuperação de senha
- `POST /auth/reset-password?token=` - Resetar senha
- `GET /auth/reset-password?token=` - Validar token de reset

## Endpoints Protegidos

Todos os outros endpoints requerem autenticação via Bearer Token.

## Swagger

Acesse a documentação interativa em: `http://localhost:8082/swagger-ui.html`

## Executar

```bash
cd C:\dev_projects\auth-service
mvn spring-boot:run
```

## Arquitetura

```
auth-service/
├── models/          # User (com UserDetails)
├── dtos/            # AuthenticationDTO, RegisterDTO, ResponseDTO, etc
├── repositories/    # UserRepository
├── services/        # AuthService, AuthenticationService
├── controllers/     # AuthController
├── infra/
│   ├── security/    # JwtUtils, SecurityConfig, SecurityFilter, KeyLoader
│   ├── config/      # OpenApiConfig
│   └── exception_handler/  # GlobalExceptionHandler
└── user_cases/
    ├── interfaces/  # UserEmailValidator, UserDataValidator
    └── validations/ # EmailAlreadyExists, EmailConfirmation
```

## Integração com User Service

Este serviço pode ser chamado pelo User Service via OpenFeign para validar tokens JWT.

URL configurada: `http://localhost:8082`
