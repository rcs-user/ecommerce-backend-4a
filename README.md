# Projeto Integrador - ToAqui

Backend REST em Spring Boot para gerenciamento do transporte escolar ToAqui.

## Objetivo

Disponibilizar uma API organizada para cadastrar responsáveis, alunos, motoristas e veículos, além de planejar viagens, controlar presença e validar regras operacionais do domínio.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Data JPA
- MySQL
- Spring Security HTTP Basic
- Bean Validation
- Maven

## Como executar

1. Configure MySQL em `ecommerce/src/main/resources/application.properties`.
2. Execute:

```bash
cd ecommerce
./mvnw spring-boot:run
```

3. A API ficará disponível em `http://localhost:8080`.

## Usuários de teste

| Usuário | Senha | Perfil |
|---|---|---|
| rcs4 | 1234 | ADMIN |
| rcs1 | 1234 | RESPONSAVEL |
| rcs2 | 1234 | MONITOR |
| rcs3 | 1234 | ALUNO |

Somente o perfil ADMIN pode excluir registros.

## Organização

- `entidades`: modelo JPA do domínio ToAqui
- `repositorios`: persistência com Spring Data JPA
- `servicos`: regras de negócio
- `recursos`: controllers REST
- `exceptions`: exceções e respostas centralizadas
- `docs`: documentação e modelo de dados
- `postman`: coleção exportada com testes

## Integrantes

| Nome completo | Matrícula |
|---|---|
| Rogério Cater Souza Júnior | 274099 |
| Joaquim Matheus da Silva Costa | 272510 |
| João Vitor Lemos de Campos | 272826 |
| Paulo Neto Azevedo | 273190 |
| Leandro Gabriel Tostes | 274654 |
| Enzo Luiz Keller Granemann | 273533 |
