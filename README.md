# Lava Jato Backend

> Sistema de gestão para lava-jato com controle de clientes, veículos, ordens de serviço, funcionários, faturamento e relatórios financeiros.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![JasperReports](https://img.shields.io/badge/JasperReports-PDF-1F6FEB?style=for-the-badge)](https://community.jaspersoft.com/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white)

---

## 📌 Visão Geral

Este projeto é um backend em Java com Spring Boot, foi desenvolvido para atender o controle operacional e financeiro do ramo, incluindo:

- cadastro de clientes e veículos;
- controle de ordens de serviço;
- gestão de usuários e permissões;
- acompanhamento de faturamento;
- geração de relatórios em PDF;
- autenticação e autorização com JWT;
- integração com banco PostgreSQL e migrações com Flyway.


## ✨ Funcionalidades Principais

### 🔐 Autenticação e Segurança
- Login com geração de token JWT;
- validação de autenticação via filtros do Spring Security;
- controle de acesso por perfil/usuário;
- endpoints protegidos e regras de segurança centralizadas.

### 🚘 Gestão Operacional
- cadastro e acompanhamento de clientes;
- controle de veículos vinculados aos clientes;
- gestão de serviços e ordens de serviço;
- acompanhamento do status de atendimento;
- histórico de movimentações e faturamento.

### 📊 Relatórios e Dashboard
- exportação de relatórios em PDF com JasperReports;
- métricas de faturamento;
- visão geral do desempenho do negócio;
- dados financeiros por período e por entidade.

### 🧩 Administração do Sistema
- cadastro de usuários e permissões;
- estrutura pronta para gestão administrativa;
- módulos para dashboard, faturamento e relatórios.

## 🛠️ Tecnologias e Ferramentas Utilizadas

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Security
- Spring Data JPA / Hibernate
- Docker
- PostgreSQL
- Flyway
- JWT
- JasperReports
- Jaspersoft Studio
- Maven
- Lombok

## 🗂️ Estrutura do Projeto

```text
lava-jato-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── lavajato/
│   │   │           ├── config/
│   │   │           │   ├── JwtFilter.java
│   │   │           │   └── SecurityConfig.java
│   │   │           ├── controller/
│   │   │           │   ├── AdminController.java
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── ClienteController.java
│   │   │           │   ├── DashBoardController.java
│   │   │           │   ├── FaturamentoController.java
│   │   │           │   └── ...
│   │   │           ├── dto/
│   │   │           ├── model/
│   │   │           ├── repository/
│   │   │           ├── service/
│   │   │           ├── util/
│   │   │           └── LavajatoBackendApplication.java
│   │   ├── resources/
│   │   │   ├── application.yaml
│   │   │   ├── db/
│   │   │   │   └── migration/
│   │   │   └── relatorios/
│   │   └── test/
│   └── ...
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
├── HELP.md
└── target/
```

### Descrição dos principais pacotes

- `config`: configuração de segurança, filtros e beans da aplicação.
- `controller`: endpoints da API REST.
- `dto`: objetos de transferência de dados.
- `model`: entidades JPA do domínio.
- `repository`: interfaces de persistência.
- `service`: regras de negócio.
- `util`: classes auxiliares e helpers.
- `resources/db/migration`: scripts Flyway para criação e população do banco.
- `resources/relatorios`: templates JasperReports para geração de PDF.

## 🌐 Principais Endpoints da API

A API expõe endpoints organizados por módulo e costuma receber e responder em JSON. Abaixo estão os principais pontos de entrada da aplicação:

### 🔐 Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Realiza login do usuário e retorna token JWT. |
| `POST` | `/auth/cadastrar` | Cadastra um novo usuário no sistema. |

### 👤 Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/usuario/cadastrar` | Cria um novo usuário. |
| `GET` | `/usuario/listar` | Lista todos os usuários. |
| `GET` | `/usuario/listarAtivos` | Lista usuários ativos. |
| `GET` | `/usuario/listarInativos` | Lista usuários inativos. |
| `GET` | `/usuario/listar/{id}` | Busca usuário por ID. |
| `PUT` | `/usuario/atualizar` | Atualiza dados do usuário. |
| `PUT` | `/usuario/atualizarSenha` | Atualiza senha do usuário. |
| `PATCH` | `/usuario/desativar/{id}` | Desativa usuário. |
| `PATCH` | `/usuario/ativar/{id}` | Ativa usuário. |

### 🚘 Clientes e Veículos

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/cliente/cadastrar` | Cadastra cliente. |
| `PUT` | `/cliente/atualizar` | Atualiza cliente. |
| `GET` | `/cliente/listar` | Lista todos os clientes. |
| `GET` | `/cliente/listar/{id}` | Busca cliente por ID. |
| `PATCH` | `/cliente/desativar/{id}` | Desativa cliente. |
| `PATCH` | `/cliente/ativar/{id}` | Ativa cliente. |
| `POST` | `/veiculo/cadastrar` | Cadastra veículo. |
| `PUT` | `/veiculo/atualizar` | Atualiza veículo. |
| `GET` | `/veiculo/listar` | Lista todos os veículos. |
| `GET` | `/veiculo/listar/{id}` | Busca veículo por ID. |
| `GET` | `/veiculo/listar/cliente/{clienteId}` | Lista veículos por cliente. |
| `PATCH` | `/veiculo/desativar/{id}` | Desativa veículo. |
| `PATCH` | `/veiculo/ativar/{id}` | Ativa veículo. |

### 🧾 Serviços e Ordens de Serviço

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/servico/listar` | Lista serviços disponíveis. |
| `POST` | `/servico/criar` | Cria serviço. |
| `PUT` | `/servico/atualizar` | Atualiza serviço. |
| `PATCH` | `/servico/desativar/{id}` | Desativa serviço. |
| `PATCH` | `/servico/ativar/{id}` | Ativa serviço. |
| `POST` | `/ordemServico/gerar` | Gera uma ordem de serviço. |
| `PUT` | `/ordemServico/atualizar` | Atualiza ordem de serviço. |
| `POST` | `/ordemServico/listar` | Lista ordens de serviço com filtros. |
| `GET` | `/ordemServico/listar/{id}` | Busca ordem por ID. |
| `DELETE` | `/ordemServico/deletar/{id}` | Remove ordem de serviço. |

### 💰 Faturamento e Relatórios

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/faturamento/listar` | Lista faturamentos. |
| `GET` | `/faturamento/listar/{id}` | Busca faturamento por ID. |
| `POST` | `/relatorio/funcionario` | Gera relatório de funcionário em PDF. |
| `POST` | `/relatorio/faturamento` | Gera relatório de faturamento em PDF. |
| `POST` | `/relatorio/clientes` | Gera relatório de clientes em PDF. |
| `POST` | `/relatorio/funcionarios` | Gera relatório de funcionários em PDF. |

### 📊 Dashboard e Administração

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/dashboard/funcionario/{id}` | Retorna dados do dashboard do funcionário. |
| `POST` | `/dashboard/gerente` | Retorna dados do dashboard do gerente. |
| `POST` | `/admin/gerarOrdemServicoTeste` | Gera ordens de serviço teste para a população de dados. 
| `POST` | `/admin/gerarOrdemServicoDiaAtual` | Gera ordens de serviço teste para o dia atual. |

> Observação: alguns endpoints possuem restrições por perfil, como `ADM`, `GERENTE` e `FUNCIONARIO`, sendo exigidas regras de autorizção via Spring Security.

## ⚙️ Projeto Frontend

O sistema conta também com um frontend desenvolvido em Angular, responsável pelo consumo dos endpoints e a apresentação visual dos dados.
- repositório: https://github.com/ViniciusSB/lava-jato-frontend

## ⚙️ Configuração e Execução

### Pré-requisitos

- Java 21
- PostgreSQL em execução
- Maven ou uso do wrapper `mvnw`
- Variáveis de ambiente configuradas

### Variáveis de ambiente

No arquivo de configuração, a aplicação lê as seguintes variáveis:

```
export DB_DATASOURCE=sua_conecao_banco
```
ex de conexão local: `jdbc:postgresql://localhost:5432/lavajato`
```
export DB_USERNAME=usuario_postgres
export DB_PASSWORD=sua_senha
export JWT_SECRET=chave_jwt
```

### ▶️ Como Executar Localmente

Clone o repositório:

```
https://github.com/ViniciusSB/lava-jato-backend
```

Acesse a pasta do projeto:

```
cd lava-jato-backend
```

Instale as dependências:

```
./mvnw clean install
```

Inicie a aplicação:

```
./mvnw spring-boot:run
```

A aplicação normalmente ficará disponível em:

```
http://localhost:8080
```

## 👨‍💻 Autor
Vinícius Santos Bessa

LinkedIn: https://www.linkedin.com/in/viniciussabessa/