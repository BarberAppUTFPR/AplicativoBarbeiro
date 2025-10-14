## Contribuindo

Obrigada por contribuir! Este guia padroniza como rodar o projeto, abrir issues/PRs e manter a qualidade do código.

## Pré-requisitos

- Java 17+
- Maven Wrapper (mvnw / mvnw.cmd)
- Git
- Acesso ao repositório

## Como rodar o projeto
 ```bash
 # na pasta do projeto
./mvnw spring-boot:run

Healthcheck: acesse http://localhost:8080/actuator/health → esperado {"status":"UP"}.
```

## Scripts úteis

```bash
# rodar em desenvolvimento
./mvnw spring-boot:run

# executar testes
./mvnw test

# build completo (compila + testa)
./mvnw clean verify

# gerar .jar
./mvnw clean package

# executar o .jar gerado
java -jar target/barberApp-0.0.1-SNAPSHOT.jar
```

## Fluxo de trabalho (Git)

- main: protegida (sem commits diretos).
    - Crie branches a partir de main:
    - feat/<resumo-curto> – nova funcionalidade
    - fix/<resumo-curto> – correção
    - docs/<resumo-curto> – documentação
    - chore/<resumo-curto> – tarefas de build/infra
- Abra PR para main.


## Pull Requests

Antes de abrir o PR:

- Garanta que a branch está atualizada com main.
- Rode localmente: ./mvnw clean verify.

Checklist do PR:
 - Objetivo e contexto descritos (use o template de PR)
 - Evidências (logs/prints; curl do healthcheck ou endpoints)
 - CI verde (build/test)
 - Impactos avaliados (segurança, dados, performance)
 - Documentação atualizada (README/CHANGELOG/Docs)
 - Pelo menos 1 aprovação de revisor (ou Code Owner, se aplicável)


## Estilo de código

- Java 17 + Spring Boot
- Nomes claros, classes coesas, métodos curtos
- Evite “God classes”; prefira extrair serviços e componentes
- Formatação padrão da IDE (IntelliJ/Google style)
(Se o projeto adotar Checkstyle/SpotBugs/PMD, seguir as regras do repo.)
