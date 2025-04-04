# totvs
Instalação
1. Clonar o Repositório
bash

git clone https://github.com/seu-usuario/produto-api.git
cd produto-api

2. Configurar o Back-end
Navegue até o diretório do back-end:
bash

cd backend

Instale as dependências:

mvn clean install

3. Configurar o Front-end
Navegue até o diretório do front-end:
bash

cd frontend

Instale as dependências:
bash

npm install
4. Configurar o Docker (Recomendado)
Certifique-se de que o Docker e o Docker Compose estão instalados.
O arquivo docker-compose.yml já está configurado para subir a API e o PostgreSQL.
Executando a Aplicação
No diretório do front-end, inicie o Angular:
bash

cd frontend
ng serve
Acesse:
API: http://localhost:8080
Front-end: http://localhost:4200
Sem Docker
Inicie o PostgreSQL localmente e crie um banco chamado produto_db.
No back-end, execute:
bash

mvn spring-boot:run
No front-end, execute:

cd frontend
ng serve

Acesse as mesmas URLs acima.
Endpoints da API
GET /produtos: Lista produtos com filtros (nome, categoria) e paginação.
GET /produtos/{id}: Busca produto por ID.
POST /produtos: Cadastra um novo produto.
PUT /produtos/{id}: Edita um produto existente.
DELETE /produtos/{id}: Exclui um produto.
GET /categorias/estoque-total: Retorna a quantidade total de estoque por categoria.
Exemplo de Resposta (GET /categorias/estoque-total):
json
[
  { "categoria": "Eletrônicos", "quantidadeTotalEstoque": 150 },
  { "categoria": "Roupas", "quantidadeTotalEstoque": 230 },
  { "categoria": "Livros", "quantidadeTotalEstoque": 90 }
]
Testes
Back-end: Execute os testes unitários com:

cd backend
mvn test

Front-end: Execute os testes com:
bash

cd frontend
ng test

Regras de Negócio

Não é permitido cadastrar produtos com o mesmo nome na mesma categoria.
Não é possível excluir produtos com quantidade em estoque maior que zero.
Estrutura do Projeto

produto-api/
├── backend/                # Código do back-end (Java/Spring Boot)
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/               # Código do front-end (Angular)
│   ├── src/
│   └── angular.json
├── docker-compose.yml      # Configuração do Docker Compose
└── README.md
