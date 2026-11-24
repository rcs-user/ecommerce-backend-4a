# Projeto Integrador - Backend de E-commerce

Backend REST em Spring Boot para gerenciamento de um e-commerce. O projeto cobre categorias, produtos, clientes, pedidos e itens de pedido, com regras de estoque e autenticação por HTTP Basic.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Data JPA / Hibernate
- Spring Security HTTP Basic
- Bean Validation
- MySQL
- Maven

## Como executar

1. Configure o MySQL em `ecommerce/src/main/resources/application.properties`.
2. Execute:

```bash
cd ecommerce
./mvnw spring-boot:run
```

3. A aplicação ficará disponível em `http://localhost:8080`. A página inicial oferece um formulário de login e uma lista de produtos. O formulário autentica com HTTP Basic; os endpoints da API continuam protegidos.

## Usuários de teste

| Usuário | Senha | Perfil |
|---|---|---|
| `admin` | `admin123` | ADMIN |
| `vendedor` | `vendedor123` | VENDEDOR |

O perfil ADMIN pode excluir recursos. Os dois perfis podem consultar e alterar dados; endpoints não autenticados recebem `401`.

## Organização

- `src/main/java/.../entidades`: entidades JPA
- `src/main/java/.../repositorios`: acesso com Spring Data
- `src/main/java/.../servicos`: regras de negócio
- `src/main/java/.../recursos`: controllers REST
- `src/main/java/.../exceptions`: exceções e tratamento centralizado
- `src/main/java/.../config`: segurança
- `docs/documentacao-api.md`: documentação dos endpoints
- `docs/modelo-dados.md`: modelo entidade-relacionamento
- `postman/projeto-integrador.postman_collection.json`: coleção de testes

## Integrantes

| Nome completo | Matrícula |
|---|---|
| Rogério Cater Souza Júnior | 274099 |
| Joaquim Matheus da Silva Costa | 272510 |
| João Vitor Lemos de Campos | 272826 |
| Paulo Neto Azevedo | 273190 |
| Leandro Gabriel Tostes | 274654 |
| Enzo Luiz Keller Granemann | 273533 |
