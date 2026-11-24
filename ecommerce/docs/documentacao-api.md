# Documentação da API

Base URL: `http://localhost:8080`

A página inicial (`/`) oferece um formulário de login para consultar produtos. Ela envia as credenciais de teste via HTTP Basic, sem persistir a senha. Os endpoints da API exigem HTTP Basic. Usuários:

- `admin` / `admin123` - perfil ADMIN
- `vendedor` / `vendedor123` - perfil VENDEDOR

## Categorias

| Método | Caminho | Descrição |
|---|---|---|
| GET | `/categorias` | Lista categorias |
| GET | `/categorias/{id}` | Busca uma categoria |
| POST | `/categorias` | Cria uma categoria |
| PUT | `/categorias/{id}` | Atualiza uma categoria |
| DELETE | `/categorias/{id}` | Remove uma categoria sem produtos associados |

## Produtos

| Método | Caminho | Descrição |
|---|---|---|
| GET | `/produtos` | Lista produtos |
| GET | `/produtos/{id}` | Busca um produto |
| POST | `/produtos` | Cria um produto |
| PUT | `/produtos/{id}` | Atualiza um produto |
| DELETE | `/produtos/{id}` | Remove um produto |
| GET | `/produtos/consultas/categoria/{categoriaId}` | Produtos por categoria |
| GET | `/produtos/consultas/preco?limite=200` | Produtos até o preço informado |
| GET | `/produtos/consultas/estoque-baixo?limite=10` | Produtos com estoque abaixo do limite |

## Clientes

| Método | Caminho | Descrição |
|---|---|---|
| GET | `/clientes` | Lista clientes |
| GET | `/clientes/{id}` | Busca um cliente |
| POST | `/clientes` | Cria um cliente |
| PUT | `/clientes/{id}` | Atualiza um cliente |
| DELETE | `/clientes/{id}` | Remove um cliente |

## Pedidos

| Método | Caminho | Descrição |
|---|---|---|
| GET | `/pedidos` | Lista pedidos |
| GET | `/pedidos/{id}` | Busca um pedido |
| POST | `/pedidos` | Cria um pedido e baixa o estoque |
| PATCH | `/pedidos/{id}/status?status=CANCELADO` | Atualiza o status e devolve itens ao cancelar |
| DELETE | `/pedidos/{id}` | Remove o pedido e devolve o estoque |
| GET | `/pedidos/consultas/cliente/{clienteId}` | Pedidos por cliente |
| GET | `/pedidos/consultas/status?status=NOVO` | Pedidos por status |

## Itens de pedido

| Método | Caminho | Descrição |
|---|---|---|
| GET | `/itens-pedido` | Lista itens |
| GET | `/itens-pedido/{id}` | Busca um item |
| GET | `/itens-pedido/pedido/{pedidoId}` | Lista itens de um pedido |
| POST | `/itens-pedido/pedido/{pedidoId}` | Adiciona item e atualiza estoque |
| PUT | `/itens-pedido/{id}` | Atualiza quantidade |
| DELETE | `/itens-pedido/{id}` | Remove item e devolve estoque |

## Regras e respostas

- `400`: validação ou dados inválidos
- `404`: entidade não encontrada
- `409`: conflito de regra de negócio
- `401`: credenciais ausentes ou inválidas
- `403`: usuário autenticado sem perfil suficiente

Exemplo de pedido:

```json
{
  "cliente": {"id": 1},
  "itens": [
    {"produto": {"id": 1}, "quantidade": 2}
  ]
}
```
