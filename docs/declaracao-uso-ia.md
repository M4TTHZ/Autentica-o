# Declaração de uso de IA

**Ferramenta utilizada:** Claude (Anthropic)

## 1. Para que a IA foi usada

- Estruturação inicial do projeto (definição de pacotes, camadas: model, repository, service, controller, dto, security, exception).
- Geração da lógica de código nas camadas de autenticação e autorização (hash de senha com BCrypt, bloqueio por tentativas, RBAC via mapa Papel→Ação, MFA/TOTP com QR Code).
- Geração do script de ataque de dicionário em Python (B5).
- Depuração de erros reais encontrados ao rodar o projeto localmente (exceções do Hibernate, erros de validação do Bean Validation, 404/500 em endpoints, CORS).
- Tirar dúvidas conceituais durante o desenvolvimento (como funciona TOTP/MFA, o que é timing attack, o que é enumeração de usuário, diferença entre autenticação e autorização).
- Geração de uma interface HTML+JS simples para testar a API visualmente.
- Revisão e alinhamento da Parte A do relatório com o conteúdo das aulas (taxonomia de ataques e motivações vista em aula).
- Redação desta própria declaração de uso de IA, a partir do histórico real da conversa.

## 2. Exemplos reais de prompts usados

**Prompt 1:**
> "Explique exatamente oq cada role terá a permissão de realizar"

**O que a IA devolveu:** uma tabela detalhando, para cada papel (VENDEDOR, FINANCEIRO, ADMIN), quais ações do RBAC cada um deveria ter (ver pedido, ver dados de cliente, ver pagamento, ver margem, gerenciar usuários, etc.), com justificativa de princípio de menor privilégio.

**O que foi mantido/alterado:** a tabela foi usada como base direta para a implementação do `PermissaoService` (mapa `Papel -> Set<Acao>`). Os papéis foram depois ajustados nas conversas seguintes (remoção do papel CLIENTE como conta de login, mudança de ATENDENTE para VENDEDOR) conforme o cenário evoluiu.

---

**Prompt 2:**
> "Build the Python attack script"

**O que a IA devolveu:** um script Python (`ataque.py`) usando a biblioteca `requests`, com uma wordlist baseada no padrão da senha fraca definida no B4, testando login via `POST /login`, parando automaticamente ao detectar sucesso (200) ou bloqueio de conta (detectando a palavra "bloqueada" na resposta).

**O que foi mantido/alterado:** o script foi mantido na estrutura gerada. Antes de criá-lo, foi necessário voltar ao `DataSeeder` para garantir que existisse de fato uma conta com senha fraca (`carlos.vendas` / `vendedor2024`) — sem isso o script não teria um alvo real, o que foi identificado como uma lacuna antes de o script ser escrito.

---

**Prompt 3 (depuração real):**
> Colagem do stack trace: "Incorrect use of entity type 'com.matheusramalho.Autenticacao.model.Cliente' (possibly due to missing association mapping annotation)"

**O que a IA devolveu:** diagnóstico de que os campos `produto`, `cliente` e `vendedor` da entidade `Venda` estavam anotados com `@Column` em vez de `@ManyToOne`, causando o erro do Hibernate.

**O que foi mantido/alterado:** a correção sugerida (trocar `@Column` por `@ManyToOne(optional = false)` nos três campos) foi aplicada diretamente no projeto local, resolvendo o erro de inicialização da aplicação.

## 3. Observação

Vários erros ao longo do desenvolvimento vieram de arquivos que não haviam sido copiados corretamente do ambiente de geração para o projeto local (ex.: `MeController`, `MfaController`, DTOs de MFA, um valor de enum digitado errado). Isso foi identificado e corrigido de forma incremental, comparando os erros reais do console com o código gerado.

---

*Esta declaração foi redigida com apoio da IA a partir do histórico real de interações do projeto, e revisada por Matheus antes da entrega.*
