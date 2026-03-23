# Teste Técnico - API de Pagamentos Nível 1

Esta é uma API RESTful desenvolvida em Java com Spring Boot para gerenciar o recebimento e o processamento de pagamentos.

## Tecnologias que Utilizei

- **Java 17**
- **Spring Boot 3.2.4**
- **H2 Database** 
- **Swagger / OpenAPI 3** (Documentação interativa da API)
- **Maven** (Gerenciamento de dependências e build)

## Regras e Lógica de Negócios

A objetivo da API é gerenciar entidades de Pagamento que contêm código de débito, CPF/CNPJ, método de pagamento, número do cartão (se aplicável), valor e status.

As seguintes lógicas de negócios foram implementadas:

### 1. Recebimento de Pagamento
- Quando um novo pagamento é registrado, ele inicia obrigatoriamente com o status **`PENDENTE_DE_PROCESSAMENTO`**.
- Se o método de pagamento escolhido for **Cartão de Crédito** ou **Cartão de Débito**, é **obrigatório** informar o número do cartão no payload da requisição. Para outros métodos (como Pix ou Boleto), este campo fica em branco.

### 2. Transições de Status
A atualização de status, segue uma regra de estados para garantir a consistência das transações:
- `PENDENTE_DE_PROCESSAMENTO` ➡️ só pode ser alterado para `PROCESSADO_COM_SUCESSO` ou `PROCESSADO_COM_FALHA`.
- `PROCESSADO_COM_FALHA` ➡️ só pode voltar a ser `PENDENTE_DE_PROCESSAMENTO`.
- `PROCESSADO_COM_SUCESSO` ➡️ **Status final.** Um pagamento aprovado com sucesso não pode mais ter seu status alterado.

### 3. Exclusão Lógica
- Os registros de pagamentos não são removidos do banco de dados realmente, como seria com um delete por exemplo. Em vez disso, existe um **campo de inativação** que chamei de **Ativo** que é um campo booleano podendo ser True ou False.
- **Regra:** Um pagamento só pode ser inativado se o seu status for `PENDENTE_DE_PROCESSAMENTO`.

### 4. Filtros
A listagem de pagamentos está permitindo filtrar registros de forma combinada utilizando:
- Código do débito
- CPF/CNPJ
- Status atual do pagamento
- Se está ativo ou inativo (exclusão lógica)

---

## 🛠️ Como Executar o Projeto

1. É necessário ter o **Java 17** ou superior instalado na máquina, caso nao tenha é bem simples podendo ser baixado pelo link https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html.
2. Clone o repositório do projeto:
   ```bash
   git clone [https://github.com/Paulompj/Fadesp-Desafio.git](https://github.com/Paulompj/Fadesp-Desafio.git)
3. No terminal, navegue até a pasta raiz do projeto.
4. Execute o projeto usando o Maven Wrapper embutido:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(No Windows, utilize `mvnw.cmd spring-boot:run`)*

A aplicação iniciará localmente na porta padrão (geralmente `8080`). O banco de dados H2 é inicializado dinamicamente em memória a cada execução.

---

## Sobre a Documentação da API (Swagger)

A API foi documentada utilizando o Swagger. Após iniciar a aplicação localmente, pode visualizar e testar todos os endpoints acessando a interface do Swagger UI no navegador:

🔗 **Link do Swagger:** `http://localhost:8080/swagger-ui/index.html`

### Principais Endpoints

- `POST /api/pagamentos`: Registra um novo pagamento.
- `GET /api/pagamentos`: Filtra os Pagamentos.
- `PATCH /api/pagamentos/{id}/status`: Atualiza o status de um pagamento específico.
- `DELETE /api/pagamentos/{id}`: Realiza a exclusão lógica de um pagamento pendente.

---

## Banco de Dados H2
Caso precisem verificar a inserção de dados diretamente nas tabelas em tempo real, pode acessar a interface do H2 Console:
Segue abaixo o passo a passo:
🔗 **Link para o H2 Console:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:pagamentodb`
- **User Name:** `sa`
- **Password:** *(deixar em branco)*
## 👨‍💻 Autor
**Paulo Moraes** 
🔗 [Linkedin](https://www.linkedin.com/in/paulo-moraes-pessoa-jardim/)  
✉️ [paulompj010@gmail.com](mailto:paulompj010@gmail.com)
