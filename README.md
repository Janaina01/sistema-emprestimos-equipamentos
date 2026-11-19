# Sistema de Empréstimos de Equipamentos 💻

API REST desenvolvida em **Java e Spring Boot** para gerenciar equipamentos corporativos, colaboradores e empréstimos, com controle de disponibilidade, devoluções e atrasos.

Esse é um projeto desenvolvido no programa Java + IA 
#ElasTech da SoulCode em parceria com a PagBank.

## Tecnologias utilizadas

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC, Spring Data JPA e Hibernate
- H2 Database (persistência local em arquivo)
- Bean Validation (`jakarta.validation`)
- Swagger / OpenAPI (`springdoc-openapi`)
- Maven e JUnit 5

## Funcionalidades e regras de negócio

| Regra | Descrição |
| --- | --- |
| R1 | Não permite cadastrar equipamentos com patrimônio duplicado. |
| R2 | Todo equipamento novo é cadastrado como disponível. |
| R3 | Não permite cadastrar colaboradores com e-mail duplicado. |
| R4 | Não permite emprestar equipamento indisponível. |
| R5 | Ao emprestar, marca o equipamento como indisponível, registra a data atual e prevê devolução em 14 dias. |
| R6 | Não permite devolver duas vezes o mesmo empréstimo. |
| R7 | Ao devolver, registra a data atual e torna o equipamento disponível novamente. |
| R8 | Considera atrasado o empréstimo não devolvido cuja data prevista já passou. |
| R9 | Não permite excluir equipamento com histórico de empréstimos. |
| R10 | Não permite novo empréstimo para colaborador com empréstimo atrasado. |

### Validação de dados

- Equipamento: patrimônio e nome obrigatórios; tipo obrigatório (`NOTEBOOK`, `MONITOR` ou `HEADSET`).
- Colaborador: nome e e-mail obrigatórios; formato de e-mail validado.
- Conflitos de regras de negócio, como patrimônio duplicado ou equipamento indisponível, retornam **HTTP 409**.

## Endpoints

### Equipamentos

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/equipamentos` | Lista equipamentos; aceita filtros opcionais `tipo` e `disponivel`. |
| GET | `/equipamentos/{id}` | Busca equipamento por ID. |
| POST | `/equipamentos` | Cadastra equipamento. |
| PUT | `/equipamentos/{id}` | Atualiza nome e tipo do equipamento. |
| DELETE | `/equipamentos/{id}` | Exclui equipamento sem histórico de empréstimos. |

Exemplo de cadastro (`POST /equipamentos`):

```json
{
  "patrimonio": "PAT-001",
  "nome": "Notebook Dell",
  "tipo": "NOTEBOOK"
}
```

### Colaboradores

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/colaboradores` | Lista colaboradores. |
| GET | `/colaboradores/{id}` | Busca colaborador por ID. |
| POST | `/colaboradores` | Cadastra colaborador. |
| GET | `/colaboradores/{id}/emprestimos` | Consulta o histórico de empréstimos do colaborador. |

Exemplo de cadastro (`POST /colaboradores`):

```json
{
  "nome": "Maria Oliveira",
  "email": "maria.oliveira@example.com",
  "cargo": "Analista"
}
```

### Empréstimos

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/emprestimos` | Lista empréstimos; `?atrasados=true` retorna apenas os atrasados. |
| GET | `/emprestimos/{id}` | Busca empréstimo por ID. |
| POST | `/emprestimos?equipamentoId=1&colaboradorId=1` | Registra empréstimo. |
| PUT | `/emprestimos/{id}/devolucao` | Registra devolução. |

**Observação:** o cadastro de empréstimo recebe os IDs como parâmetros da URL, e não em um JSON no corpo da requisição.

## Como executar localmente

**Pré-requisitos:** Java 21 e Git. O projeto inclui Maven Wrapper, portanto não é necessário instalar o Maven separadamente.

1. Clone o repositório e entre na pasta do projeto:

   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd emprestimos
   ```

2. Inicie a aplicação:

   **Windows:**
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **Linux/macOS:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. Acesse a documentação interativa: <http://localhost:8080/swagger-ui/index.html>.

A API utiliza, por padrão, a porta **8080** e o banco H2 em arquivo local (`./dados/emprestimos`). O esquema é atualizado automaticamente pelo Hibernate.

### Console do H2

- URL: <http://localhost:8080/h2-console>
- JDBC URL: `jdbc:h2:file:./dados/emprestimos`
- Usuário: `sa`
- Senha: em branco

O console H2 é destinado ao desenvolvimento local.

## Testes

O projeto possui **12 testes JUnit 5**: 3 testes de `Equipamento`, 8 de `Emprestimo` e 1 teste de inicialização do contexto Spring.

Para executá-los:

**Windows:**
```powershell
.\mvnw.cmd clean test
```

**Linux/macOS:**
```bash
./mvnw clean test
```

Os testes verificam, entre outros comportamentos, a disponibilidade dos equipamentos, a devolução, o prazo de 14 dias e a identificação de atrasos.

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/com/projeto/emprestimos/
│   │   ├── config/         # Configuração OpenAPI
│   │   ├── controller/     # Endpoints REST
│   │   ├── model/          # Entidades e enum
│   │   ├── repository/     # Acesso aos dados
│   │   ├── service/        # Regras de negócio
│   │   └── EmprestimosApplication.java
│   └── resources/
│       └── application.properties
└── test/java/com/projeto/emprestimos/
    ├── model/             # Testes unitários
    └── EmprestimosApplicationTests.java
```

## Documentação da API

Com a aplicação em execução, acesse o **Swagger UI** em:

<http://localhost:8080/swagger-ui/index.html>

A especificação OpenAPI também fica disponível em `/v3/api-docs`.

## Deploy e demonstração

- **API:** <https://sistema-emprestimos-equipamentos.onrender.com>
- **Listagem de equipamentos:** <https://sistema-emprestimos-equipamentos.onrender.com/equipamentos>
- **Documentação Swagger:** <https://sistema-emprestimos-equipamentos.onrender.com/swagger-ui/index.html>

>Observação: a aplicação utiliza o plano gratuito do Render, podendo levar alguns segundos para responder após períodos de inatividade. O banco H2 é utilizado para fins acadêmicos e não possui persistência garantida após reinicializações do serviço.

## Próximas melhorias possíveis

- Interface web integrada à API para demonstração do fluxo de empréstimos.
- Persistência adequada para hospedagem permanente.
- Ampliação dos testes automatizados para as camadas Controller e Service.
