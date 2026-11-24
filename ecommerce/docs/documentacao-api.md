# Documentação da API ToAqui

Base URL: `http://localhost:8080`

## Autenticação

Todos os endpoints protegidos usam HTTP Basic.

- `rcs4` / `1234`: ADMIN
- `rcs1` / `1234`: RESPONSAVEL
- `rcs2` / `1234`: MONITOR
- `rcs3` / `1234`: ALUNO

Somente ADMIN pode excluir registros.

## Responsáveis

`GET /responsaveis`, `GET /responsaveis/{id}`, `POST /responsaveis`,
`PUT /responsaveis/{id}` e `DELETE /responsaveis/{id}`.

## Alunos

`GET /alunos`, `GET /alunos/{id}`, `GET /alunos/consultas/responsavel/{responsavelId}`,
`POST /alunos`, `PUT /alunos/{id}` e `DELETE /alunos/{id}`.

## Motoristas

`GET /motoristas`, `GET /motoristas/{id}`, `POST /motoristas`,
`PUT /motoristas/{id}` e `DELETE /motoristas/{id}`.

## Veículos

`GET /veiculos`, `GET /veiculos/{id}`, `POST /veiculos`,
`PUT /veiculos/{id}` e `DELETE /veiculos/{id}`.

## Viagens

`GET /viagens`, `GET /viagens/{id}`, `POST /viagens`, `PUT /viagens/{id}` e
`DELETE /viagens/{id}`.

Consultas personalizadas:

- `GET /viagens/consultas/status?status=EM_ANDAMENTO`
- `GET /viagens/consultas/data?data=2026-11-24`
- `GET /viagens/consultas/motorista/{motoristaId}`

Operações específicas:

- `PATCH /viagens/{id}/status?status=EM_ANDAMENTO`
- `PATCH /viagens/{id}/presenca/{alunoId}?status=A_BORDO`

## Regras principais

- Um aluno deve possuir um responsável válido.
- A capacidade do veículo deve ser maior que zero.
- Uma viagem precisa de motorista, veículo e pelo menos um aluno.
- Alunos não podem ser repetidos em uma viagem.
- O número de alunos não pode exceder a capacidade.
- Motorista ou veículo não pode ser usado em outra viagem em andamento na mesma data.
- Responsáveis com alunos, motoristas/veículos com viagens e alunos em viagens não podem ser excluídos.

## Códigos de resposta

- `400`: dados inválidos
- `401`: credenciais ausentes ou inválidas
- `403`: usuário sem perfil suficiente
- `404`: recurso inexistente
- `409`: conflito com regra de negócio
