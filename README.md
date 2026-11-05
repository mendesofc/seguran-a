# Segurança do ChapaQuente

Vitor, deixei os arquivos da segurança aqui separados pra você juntar com o back.

- **SecurityConfig:** regras de acesso às rotas.
- **ClienteUserDetailsService:** login pelo e-mail, usando os clientes do banco e o admin configurado no ambiente.
- **CsrfController:** token CSRF pro front.
- **AdminVerification:** verifica se quem tá logado é admin.

É só colocar as classes nos pacotes correspondentes do projeto. As três primeiras são do pacote `security`; a `AdminVerification` é do pacote `verification` e substitui a antiga.

No Maven, precisa adicionar `spring-boot-starter-security`. Pode manter o `PasswordConfig` que já tem aí, porque ele já configura o BCrypt. Pro admin, as variáveis são `ADMIN_EMAIL` e `ADMIN_PASSWORD_HASH` (hash BCrypt).

O login passa a ser pelo Spring Security, em `POST /login`, com `email` e `password`. O front pega o token em `GET /csrf` e manda junto nas requisições que alteram dados. A verificação de admin fica em `GET /verificacao/admin-status`.

**Só uns pontos pra acertar quando juntar:**

- O login antigo do cliente e a verificação antiga do admin não devem continuar sendo usados.
- Tem que impedir que a senha/hash do cliente apareça no JSON de resposta.
- Deixei pedidos e itens restritos ao admin por enquanto, porque os endpoints aceitam preços enviados na requisição. Pra liberar compra, o valor precisa ser calculado no servidor.
- Também deixei a criação de reserva só pro admin por enquanto. O controller recebe um cliente no JSON, então ainda precisa usar o cliente da sessão antes de liberar reservas comuns.
- O envio de código por e-mail ainda precisa de limite de reenvio, e o CPF vai ser retirado na parte do cadastro.
- Se o front estiver em outro domínio, vamos precisar configurar CORS e cookies direito.

Não mexi no resto do teu backend. Falta testar essas classes dentro do projeto completo e ajustar o front pro login novo.
