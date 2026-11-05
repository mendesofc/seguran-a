# Segurança — ChapaQuente

Módulo de segurança para integrar ao projeto de [VitorCosta09/Projeto-Restaurante](https://github.com/VitorCosta09/Projeto-Restaurante).

Compatibilidade planejada: **Spring Boot 4.1.1 / Java 25**, pacote `dev.trapalhoes.ChapaQuente`. Este repositório contém apenas os arquivos da segurança, sem copiar controllers ou regras de negócio do Vitor.

## Integração
1. Copiar `src/main/java/dev/trapalhoes/ChapaQuente/security` para o projeto principal.
2. No `pom.xml` do Vitor, adicionar:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-security</artifactId>
   </dependency>
   ```
3. Manter o `config/PasswordConfig.java` original do Vitor. Ele já fornece um bean BCrypt; **não criar outro PasswordEncoder**.
4. Configurar `ADMIN_EMAIL` e `ADMIN_PASSWORD_HASH` no ambiente. O hash deve ser BCrypt; nunca usar senha em texto puro.
5. Para login pelo navegador: `GET /csrf` retorna token CSRF. Enviar esse token no header indicado ao fazer `POST /login`, com `email` e `password` como form-urlencoded. O navegador deve manter o cookie da sessão.
6. `GET /verificacao/admin-status` indica se a sessão atual tem permissão de administrador. Requer login admin.
7. Para login de cliente, é necessário existir cliente cadastrado com senha BCrypt na tabela `Cliente`. A busca usa JPA por e-mail, sem exigir alteração do repositório do Vitor.
8. Se frontend e backend estiverem em domínios diferentes, configurar CORS **somente** para o domínio real e ajustar cookies HTTPS. Não usar `origins="*"` para operações autenticadas.

## Permissões
- Catálogo de produtos: leitura pública.
- Cadastro de cliente e verificação de e-mail: públicos, com validações existentes.
- Operações administrativas: exigem `ROLE_ADMIN`.
- Reservas: escrita requer sessão de cliente ou admin; leitura e gestão exigem admin até adicionar filtro por proprietário.
- Criação de pedidos e itens: temporariamente apenas admin, pois a API original aceita preços/descontos informados pelo cliente. Antes de liberar compra para clientes, o Vitor precisa calcular valores no servidor.
- Demais endpoints: negados por padrão.

## Atenção: ajustes de integração indispensáveis
- O controller original `ClienteController` ainda contém login antigo `POST /cliente/{id}` e retorna a entidade `Cliente`, incluindo hash de senha. **Esse endpoint é bloqueado por esta configuração**, mas deve ser substituído pelo login Spring Security.
- Adicionar `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` no campo `senha` da entidade `Cliente` (ou usar DTOs de resposta), para não expor hashes em outros endpoints.
- O `AdminVerification` original compara senha em texto puro. **Substituir pelo arquivo de exemplo em `integration/AdminVerification.java`**, sem manter duas classes com o mesmo nome.
- O `EmailVerification` original permite envio ilimitado de e-mail e usa GET com corpo. Corrigir rate limiting e mudar para POST na integração.
- O projeto do Vitor ainda possui CPF; a remoção será feita por quem cuida do cadastro/banco. Este módulo não modifica a entidade.
- Revisar o frontend para CSRF e sessão, e realizar testes com banco e rotas reais.

**Não foi executado build nem teste integrado neste repositório isolado.** A segurança precisa ser validada no projeto completo antes da publicação.
