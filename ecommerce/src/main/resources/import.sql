insert ignore into categoria (nome, descricao) values ('Informática', 'Produtos de Informática');
insert ignore into categoria (nome, descricao) values ('Eletrônicos', 'Aparelhos Eletrônicos');
insert ignore into categoria (nome, descricao) values ('Livros', 'Livros Técnicos');
insert ignore into categoria (nome, descricao) values ('Casa e Decoração', 'Produtos para casa e decoração');
insert ignore into categoria (nome, descricao) values ('Acessórios', 'Acessórios e itens de uso pessoal');

insert ignore into produto (nome, descricao, preco, estoque, categoria_id) values ('Código Limpo', 'Livro do Autor Robert C. Martin', 100.00, 20, 3);
insert ignore into produto (nome, descricao, preco, estoque, categoria_id) values ('Notebook Dell Inspiron', 'Notebook com processador Intel Core i5 e 16GB de memória RAM', 3499.90, 15, 1);
insert ignore into produto (nome, descricao, preco, estoque, categoria_id) values ('Fone de Ouvido Bluetooth', 'Fone de ouvido sem fio com conexão Bluetooth', 129.90, 40, 2);
insert ignore into produto (nome, descricao, preco, estoque, categoria_id) values ('Luminária de Mesa LED', 'Luminária de mesa com iluminação LED ajustável', 89.90, 35, 4);
insert ignore into produto (nome, descricao, preco, estoque, categoria_id) values ('Mochila para Notebook', 'Mochila para notebook de até 15,6 polegadas', 149.90, 25, 5);

insert ignore into cliente (nome, email, documento, endereco) values ('Maria da Silva', 'maria@example.com', '12345678901', 'Rua das Flores, 100');
insert ignore into cliente (nome, email, documento, endereco) values ('João Santos', 'joao@example.com', '98765432100', 'Av. Brasil, 200');