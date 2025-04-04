# totvs
src/
├── main/
│   ├── java/com/teste/produtoapi/
│   │   ├── config/           # Configurações (ex.: logs, exceptions)
│   │   ├── controller/       # Endpoints REST
│   │   ├── entity/          # Entidades JPA
│   │   ├── repository/      # Repositórios JPA
│   │   ├── service/         # Lógica de negócio
│   │   ├── dto/             # Objetos de transferência
│   │   ├── exception/       # Exceções personalizadas
│   │   └── ProdutoApiApplication.java # Classe principal
│   └── resources/
│       └── application.yml   # Configurações do Spring
└── test/                     # Testes unitários