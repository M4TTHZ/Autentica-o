# AdminShop — Protótipo de Autenticação Segura

Painel administrativo interno de uma loja virtual, usado por funcionários (vendedores, financeiro e administradores) para gerenciar clientes, produtos e vendas. Projeto acadêmico com foco em autenticação e segurança (não em funcionalidades de e-commerce em si).

## Onde está cada parte da entrega

| Parte | Onde encontrar |
|---|---|
| **Parte A** — Modelagem de segurança | [`docs/parte-a-modelagem-seguranca.md`](docs/parte-a-modelagem-seguranca.md) |
| **Parte B** — Código | Backend em `src/main/java` (Spring Boot), descrito abaixo |
| **Parte C** — Relatório de incidente | [`docs/parte-c-relatorio-incidente.md`](docs/parte-c-relatorio-incidente.md) |
| **Declaração de uso de IA** | [`docs/declaracao-uso-ia.md`](docs/declaracao-uso-ia.md) |

## Requisitos do enunciado (Parte B) e onde estão implementados

| Item | Implementação |
|---|---|
| B1 — Política de senha | `service/PasswordPolicyService.java` |
| B2 — Papéis com permissões diferentes (RBAC) | `model/Papel.java`, `model/Acao.java`, `service/PermissaoService.java` |
| B3 — Login com bloqueio por tentativas | `service/LoginService.java` |
| B4 — Contas cadastradas, uma com senha fraca | `config/DataSeeder.java` (conta `carlos.vendas` / `vendedor2024`) |
| B5 — Ataque de dicionário | `attack-script/ataque.py` |
| B6 — MFA (TOTP + QR Code), papel ADMIN | `service/MfaService.java`, `controller/MfaController.java` |

## Stack

- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA, H2 (banco em memória)
- **MFA/TOTP:** biblioteca `dev.samstevens.totp`
- **Documentação da API:** springdoc-openapi (Swagger UI)
- **Frontend:** HTML + JavaScript puro (sem framework), consumindo a API via `fetch`
- **Script de ataque (B5):** Python + `requests`

## Como executar

### 1. Backend

Pré-requisitos: Java 21 e Maven (ou usar o Maven embutido do IntelliJ).

No IntelliJ: abrir o projeto, deixar o Maven importar as dependências, rodar `AutenticacaoApplication` (botão Run).

Pelo terminal, na raiz do projeto:
```bash
mvn spring-boot:run
```

O backend sobe em `http://localhost:8080`. No console, ao subir, é impressa a lista de contas já cadastradas (ver seção abaixo).

### 2. Frontend

Não precisa de instalação. Basta abrir o arquivo `frontend/index.html` diretamente no navegador (duplo clique, ou "abrir com" o navegador), com o backend já rodando.

### 3. Documentação da API (Swagger)

Com o backend rodando, acessar:
```
http://localhost:8080/swagger-ui.html
```
Usar o botão **Authorize** para testar endpoints protegidos com usuário/senha (Basic Auth).

### 4. Script de ataque (B5)

Com o backend rodando:
```bash
cd attack-script
pip install requests
python ataque.py
```

## Contas já cadastradas (seed automático)

O `DataSeeder` popula o banco automaticamente toda vez que a aplicação sobe (o H2 é em memória, então os dados são recriados do zero a cada restart).

| Usuário | Senha | Papel | Observação |
|---|---|---|---|
| `Matheus Ramalho` | `Batata12345` | ADMIN | MFA já ativo — ver chave impressa no console ao subir a aplicação |
| `joao.vendas` | `Vendedor123` | VENDEDOR | |
| `ana.financeiro` | `Financeiro123` | FINANCEIRO | |
| `carlos.vendas` | `vendedor2024` | VENDEDOR | Senha fraca de propósito (B4) — alvo do ataque em `attack-script/ataque.py` |

Também são criados 2 clientes, 2 produtos e 2 vendas de exemplo.

**MFA do ADMIN:** já vem ativo. Ao subir a aplicação, o console imprime a chave secreta — cadastre-a manualmente em um app autenticador (Google Authenticator, Authy, etc., opção "inserir chave manualmente") para gerar os códigos de login.

## Estrutura do projeto

```
adminshop/
├── docs/                      Parte A, Parte C e declaração de IA
├── attack-script/             Script de ataque em Python (B5)
├── frontend/                  Interface HTML+JS
└── src/main/java/.../
    ├── model/                 Entidades JPA e enums
    ├── repository/            Interfaces Spring Data JPA
    ├── config/                Segurança (Spring Security), CORS, Swagger, DataSeeder
    ├── security/               Integração do Usuario com o Spring Security
    ├── dto/                   Objetos de entrada/saída da API
    ├── service/               Regras de negócio (senha, login, RBAC, MFA, log)
    ├── controller/            Endpoints REST
    └── exception/             Exceções customizadas e tratamento central de erros
```

## Endpoints principais

| Endpoint | Método | Acesso |
|---|---|---|
| `/cadastro` | POST | Autenticado como ADMIN |
| `/login` | POST | Público |
| `/mfa/setup`, `/mfa/ativar` | POST | Autenticado (ADMIN) |
| `/clientes` | GET/POST | Autenticado (todos os papéis) |
| `/produtos` | GET | Autenticado (todos) |
| `/produtos` | POST | Autenticado (ADMIN) |
| `/vendas` | POST | VENDEDOR, ADMIN |
| `/pedidos` | GET | Autenticado (todos) |
| `/pedidos/{id}/status` | PUT | VENDEDOR, ADMIN |
| `/pagamentos` | GET | FINANCEIRO, ADMIN |
| `/usuarios` | GET, `/usuarios/{id}/desbloquear`, `/usuarios/{id}/papel` | ADMIN |
| `/me` | GET | Autenticado (todos) |

Autenticação via **HTTP Basic Auth** (usuário e senha reenviados em cada requisição, sem sessão no servidor).
