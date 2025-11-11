## Proteção da `main`
- [ ] **Pull Request obrigatório** (sem commits diretos na `main`).
- [ ] **1+ aprovação de revisor** (obrigatório por autor do PR).
- [ ] **Status checks exigidos**: `lint`, `build`, `test` todo **verdes** para permitir merge.
- [ ] **Require branches to be up to date** (PR atualizado com a `main`).
- [ ] **Sem force push** na `main`.
- [ ] **Sem bypass** das proteções.
- [ ] (Opcional) **Require review from Code Owners**.

## --------------------------------------------------------------------------------------------------------------------------------------------##

## Scripts de execução

Este projeto utiliza o **Maven Wrapper** (`mvnw` ou `mvnw.cmd`), que permite executar a aplicação sem precisar ter o Maven instalado globalmente.  
Abaixo estão os scripts padronizados de execução que devem ser utilizados por todos os desenvolvedores:

### Subir a aplicação em modo desenvolvimento
```bash
./mvnw spring-boot:run
```

## --------------------------------------------------------------------------------------------------------------------------------------------##

## Healthcheck
Após iniciar a aplicação, o endpoint de verificação de saúde estará disponível em:

[http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

Saída esperada:
```json
{"status":"UP"}
```