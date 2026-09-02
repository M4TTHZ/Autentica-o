# Parte A — Modelagem de Segurança

**Projeto:** AdminShop
**Cenário:** Painel administrativo interno de uma loja virtual, utilizado por funcionários (vendedores, financeiro e administradores) para gerenciar clientes, produtos e vendas. Não é a loja voltada ao cliente final — é o sistema de bastidores, sem login de cliente.

---

## 1. Ativos de informação protegidos pelo login

### Ativo 1 — Dados de pagamento e transação das vendas

- **Ameaça:** força bruta / credential stuffing contra o endpoint `/login` — um atacante testando senhas sistematicamente (ou reutilizando credenciais vazadas de outro serviço) até obter acesso a uma conta FINANCEIRO ou ADMIN.
- **Vulnerabilidade:** política de senha fraca combinada à ausência de bloqueio por tentativas — a mesma falha estrutural que serviços expostos sem controle de tentativas (ex.: RDP, Telnet) apresentam.

### Ativo 2 — Dados cadastrais de clientes (nome, CPF, CEP)

- **Ameaça:** exploração de vulnerabilidade conhecida e não corrigida — por exemplo, uma falha de autorização tipo IDOR (Insecure Direct Object Reference), em que o ID de um cliente é trocado diretamente na URL para acessar registros fora do escopo permitido.
- **Vulnerabilidade:** ausência de verificação de posse/escopo no endpoint de consulta de cliente, permitindo acesso a dados fora do previsto para o papel do usuário autenticado.

### Ativo 3 — Dados financeiros internos (custo de produto, margem)

- **Ameaça:** insider threat — um funcionário com acesso legítimo ao sistema (ex.: papel VENDEDOR), motivado por interesse próprio ou vingança, repassando dado estratégico a um concorrente.
- **Vulnerabilidade:** ausência de RBAC em nível de campo — o mesmo endpoint de consulta de produto expõe tanto `precoVenda` (deveria ser público) quanto `custo` (deveria ser restrito), sem diferenciar por papel do usuário.

---

## 2. Protocolo e porta em uma aplicação real

O sistema usaria **HTTPS (HTTP sobre TLS), porta 443**.

As credenciais de login (`username` e `senha`) trafegam do navegador do funcionário até o servidor a cada requisição de `/login`. Em HTTP puro (porta 80), esses dados trafegariam em texto claro, e qualquer pessoa capaz de interceptar o tráfego na mesma rede (ex.: Wi-Fi público, ataque man-in-the-middle) conseguiria capturar login e senha diretamente — mesmo que o servidor armazene a senha com hash, já que o hash protege o dado *em repouso* no banco, não o dado *em trânsito*. TLS na porta 443 garante que essa comunicação seja criptografada ponta a ponta, o que é ainda mais crítico aqui porque o sistema concede acesso a dados de pagamento e dados pessoais de terceiros (os clientes), não apenas à conta do próprio funcionário.

---

## 3. Por que autenticação forte importa especificamente para esses ativos

Diferente de um sistema onde o login apenas personaliza uma experiência, aqui a autenticação é a única barreira entre um agente malicioso e dados de pagamento e dados pessoais de terceiros que não deram consentimento algum para que aquele funcionário específico os acesse sem necessidade — ou seja, uma senha fraca ou um controle de acesso mal implementado não compromete apenas a conta do funcionário, mas a privacidade e a segurança financeira de clientes que sequer sabem que o sistema existe.
