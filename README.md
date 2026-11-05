# Segurança do ChapaQuente

Vitor, deixei a parte da segurança separada aqui pra ficar mais fácil de colocar no projeto sem misturar com o resto do back.

Usei a estrutura que já tá no teu projeto (`dev.trapalhoes.ChapaQuente`, Java 25 e Spring Boot 4.1.1). Não mexi nas classes de pedido, produto, reserva e nem no banco.

## O que tem aqui

- `SecurityConfig.java`: define quem pode acessar cada rota.
- `ClienteUserDetailsService.java`: pega o cliente pelo e-mail no banco e confere a senha pelo Spring Security. O admin fica configurado por variável de ambiente.
- `CsrfController.java`: entrega o token CSRF pro front mandar nas requisições.
- `integration/AdminVerification.java`: versão da verificação de admin usando a sessão, sem comparar senha direto no controller.

## Pra colocar no projeto

1. Copia a pasta `src/main/java/dev/trapalhoes/ChapaQuente/security` pra mesma pasta do teu projeto.
2. Adiciona essa dependência no `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

3. Pode manter teu `PasswordConfig`, porque ele já cria o BCrypt. Não precisa fazer outro bean de `PasswordEncoder`.
4. No ambiente, configura `ADMIN_EMAIL` e `ADMIN_PASSWORD_HASH`. A senha do admin precisa estar em hash BCrypt, não em texto normal.
5. O login do Spring fica no `POST /login`, mandando `email` e `password` como formulário. Antes do POST, o front consulta `GET /csrf` e manda o token no header que a resposta indicar. Tem que manter o cookie da sessão.
6. Pra consultar se é admin, usa `GET /verificacao/admin-status`. O arquivo em `integration/` é pra substituir o `AdminVerification` antigo, **não** pra deixar os dois.

## Como ficaram os acessos

A lista de produtos continua pública. Cadastro e verificação de e-mail também continuam acessíveis sem login. As rotas de estoque, alterações de status, exclusão e administração ficam só pro admin.

Reserva pode ser criada com login de cliente ou admin, mas a listagem geral fica só pro admin por enquanto.

**Deixei criação de pedidos e itens só pro admin por enquanto.** O endpoint atual recebe preço e desconto no corpo da requisição, então não dá pra liberar isso pro cliente sem calcular os valores no servidor. Pra compra normal funcionar, ainda precisa ajustar essa parte.

## Coisas que precisamos acertar na integração

- O login antigo do `ClienteController` (`POST /cliente/{id}`) fica bloqueado pelas regras novas. O front precisa usar o login do Spring.
- O `ClienteController` retorna o objeto `Cliente` e isso pode mandar o hash da senha no JSON. Coloca `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` no campo `senha` da entidade, ou usa DTO na resposta.
- A verificação antiga do admin compara a senha em texto direto no controller. Tem que substituir pelo arquivo da pasta `integration`.
- O envio de código por e-mail ainda precisa de limite de tentativas/reenvio. Hoje ele usa GET com body; seria melhor trocar pra POST e ajustar o front junto.
- Se o front rodar em outro domínio, precisa configurar CORS pro domínio certo e os cookies da sessão. Não deixar `*` em rotas autenticadas.
- A retirada do CPF fica na parte do cadastro/banco, não mexi nisso aqui.

Ainda falta testar tudo junto no projeto completo, principalmente login, permissões, CSRF e o front. Deixei separado justamente pra conseguir integrar e corrigir sem bagunçar o resto.
