# Testes automatizados da API ServeRest

![Testes de API](https://github.com/redhenrique7-gif/serverest-api-tests/actions/workflows/testes.yml/badge.svg)

Projeto de estudo de automação de testes de API REST, criado para praticar testes funcionais, BDD e integração contínua.

A API testada é a [ServeRest](https://serverest.dev), que simula uma loja virtual e é feita para estudo de testes.

## Tecnologias

- **Java 17** e **Maven**
- **JUnit 5**: estrutura dos testes
- **REST Assured**: requisições HTTP e validações das respostas
- **Cucumber**: cenários BDD escritos em português (Gherkin)
- **GitHub Actions**: integração contínua. Os testes rodam a cada push.

## Cenários cobertos

| Funcionalidade | Cenário | Tipo |
|---|---|---|
| Usuários | Listar usuários | Positivo |
| Usuários | Cadastrar usuário válido | Positivo |
| Usuários | Bloquear email duplicado | Negativo |
| Usuários | Bloquear nome em branco | Negativo |
| Usuários | Buscar usuário por id | Positivo |
| Usuários | Buscar id inexistente | Negativo |
| Usuários | Excluir usuário e confirmar exclusão | Positivo |
| Login | Login válido retorna token | Positivo / BDD |
| Login | Senha incorreta retorna 401 | Negativo / BDD |
| Login | Email em formato inválido | Negativo |

## Boas práticas aplicadas

- **Massa de dados independente:** cada teste gera um email único e não depende de outro teste.
- **Limpeza após os testes:** usuários criados são excluídos no final (`@AfterEach` / `@After`).
- **Reaproveitamento:** chamadas da API centralizadas em `UsuarioClient`, usadas tanto pelo JUnit quanto pelo Cucumber.
- **Log apenas em falhas:** requisição e resposta aparecem no console só quando um teste falha.
- **Ambiente configurável:** a URL da API é trocada por parâmetro (`-DbaseUrl`).

## Estrutura

```
src/test/java/br/com/joaohqa/serverest
├── config/ApiConfig.java        # configuração da URL e logs
├── utils/DadosUsuario.java      # geração de massa de dados
├── utils/UsuarioClient.java     # chamadas reutilizáveis da API
├── usuarios/UsuariosTest.java   # testes de /usuarios (JUnit)
├── login/LoginTest.java         # testes de /login (JUnit)
└── bdd/                         # runner e steps do Cucumber
src/test/resources/features/login.feature   # cenários BDD
.github/workflows/testes.yml                # pipeline de CI
```

## Como executar

Pré-requisitos: Java 17+ e Maven.

```bash
# Contra a API pública
mvn test

# Contra a ServeRest rodando na sua máquina (precisa de Node.js)
npx serverest@latest
mvn test -DbaseUrl=http://localhost:3000
```

O relatório do Cucumber é gerado em `target/cucumber-report.html`.

## Integração contínua

A cada push, o GitHub Actions sobe a ServeRest localmente, executa todos os testes e publica os relatórios como artefato da execução.

## Autor

João Henrique Souza da Silva. [LinkedIn](https://www.linkedin.com/in/joaohqa/)
