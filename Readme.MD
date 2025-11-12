# API de Filmes - Serviço de Catálogo

Esta é a documentação para a API de Filmes, um microsserviço responsável por gerenciar o catálogo de filmes, gêneros, elenco, reviews e favoritos dos usuários.

Este serviço foi desenvolvido utilizando **Java** com o framework **Spring Boot** e a segurança é gerenciada via **JWT (JSON Web Tokens)**, operando de forma totalmente *stateless*.

## 🚀 Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot**
* **Spring Security** (com autenticação baseada em JWT)
* **Maven** (para gerenciamento de dependências)
* **JPA / Hibernate** (para persistência de dados)
* **PostgreSQL** (Banco de dados)

## ⚙️ Configuração do Projeto

1.  **Clone o repositório:**
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd <NOME_DO_PROJETO>
    ```

2.  **Configure o Banco de Dados:**
    Certifique-se de ter uma instância do **PostgreSQL** em execução e crie um banco de dados para a aplicação.

3.  **Configure as variáveis de ambiente:**
    Este projeto utiliza o arquivo `application.properties` para configuração. Abra o arquivo localizado em `src/main/resources/application.properties` e configure as propriedades de conexão com o seu banco de dados PostgreSQL e o segredo do JWT:
    ```properties
    # Configuração do Banco de Dados PostgreSQL
    spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_seu_banco
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha

    # Chave secreta para assinatura dos tokens JWT
    api.security.token.secret=seu-segredo-super-secreto
    ```

4.  **Instale as dependências e compile o projeto:**
    O projeto utiliza Maven. Execute o seguinte comando na raiz do projeto para baixar as dependências e compilar:
    ```bash
    mvn clean install
    ```

5.  **Execute a aplicação:**
    ```bash
    java -jar target/seu-projeto-0.0.1-SNAPSHOT.jar
    ```
    A API estará disponível em `http://localhost:8080` (ou na porta que você configurar).

## 🔐 Segurança e Autenticação

A segurança da API é baseada em JWT e controle de acesso por papéis (Role-Based Access Control).

* **Stateless:** A API não mantém sessões de usuário. Cada requisição para um endpoint protegido deve conter um token JWT válido no cabeçalho `Authorization`.
    ```
    Authorization: Bearer <seu-token-jwt>
    ```
* **Níveis de Acesso:**
    * **`ADMIN`**: Usuários com este papel têm permissão para gerenciar o catálogo (adicionar filmes, gêneros, elenco, etc.).
    * **`USER` (Autenticado)**: Qualquer usuário autenticado (com um token válido) pode acessar as funcionalidades públicas, como listar gêneros, gerenciar seus próprios favoritos e reviews.

## 📖 Endpoints da API

Aqui está a lista de todos os endpoints disponíveis, com seus respectivos métodos HTTP, descrição e permissões necessárias.

### Gerenciamento de Filmes
*Acesso restrito a administradores.*

| Método | Endpoint      | Descrição                           | Permissão |
| :----- | :------------ | :---------------------------------- | :-------- |
| `POST` | `/filmes/add` | Adiciona um novo filme ao catálogo. | `ADMIN`   |

---

### Gerenciamento de Gêneros
*Adicionar gêneros é restrito a administradores. Listar é público para usuários autenticados.*

| Método | Endpoint         | Descrição                           | Permissão   |
| :----- | :--------------- | :---------------------------------- | :---------- |
| `POST` | `/generos/add`   | Adiciona um novo gênero.            | `ADMIN`     |
| `GET`  | `/generos/list`  | Lista todos os gêneros disponíveis. | `Autenticado` |

---

### Gerenciamento de Elenco, Pessoas e Imagens
*Acesso restrito a administradores.*

| Método | Endpoint        | Descrição                                 | Permissão |
| :----- | :-------------- | :---------------------------------------- | :-------- |
| `POST` | `/elencos/add`  | Adiciona uma pessoa a um elenco de filme. | `ADMIN`   |
| `POST` | `/pessoas/add`  | Adiciona uma nova pessoa (ator, diretor). | `ADMIN`   |
| `POST` | `/imagens/add`  | Adiciona uma nova imagem (pôster, etc).   | `ADMIN`   |

---

### Gerenciamento de Favoritos
*Acesso disponível para qualquer usuário autenticado gerenciar sua própria lista.*

| Método   | Endpoint                              | Descrição                                           | Permissão   |
| :------- | :------------------------------------ | :-------------------------------------------------- | :---------- |
| `POST`   | `/favorites/add`                      | Adiciona um filme à lista de favoritos de um usuário.   | `Autenticado` |
| `GET`    | `/favorites/{userId}`                 | Lista todos os filmes favoritos de um usuário específico. | `Autenticado` |
| `DELETE` | `/favorites/remove/{userId}/{movieId}` | Remove um filme da lista de favoritos.               | `Autenticado` |

---

### Gerenciamento de Reviews
*Acesso disponível para qualquer usuário autenticado.*

| Método   | Endpoint             | Descrição                                     | Permissão   |
| :------- | :------------------- | :-------------------------------------------- | :---------- |
| `POST`   | `/reviews/add`       | Adiciona uma nova review para um filme.       | `Autenticado` |
| `GET`    | `/reviews/{idFilme}` | Lista todas as reviews de um filme específico. | `Autenticado` |
| `DELETE` | `/reviews/{id}`      | Deleta uma review específica pelo seu ID.     | `Autenticado` |

---

### 💡 Observação sobre a Arquitetura

Este serviço é um **microsserviço**. A validação do token JWT e a obtenção de dados do usuário (como o `userId` e seus papéis) são responsabilidades de um outro microsserviço de **Autenticação/Usuários**. Esta API confia no token recebido para autorizar as operações.