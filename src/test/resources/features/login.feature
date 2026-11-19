# language: pt
Funcionalidade: Login na API ServeRest
  Como usuário cadastrado
  Quero fazer login na API
  Para receber um token e acessar rotas protegidas

  Contexto:
    Dado que existe um usuário cadastrado

  Cenário: Login com credenciais válidas
    Quando faço login com o email e a senha corretos
    Então o status da resposta deve ser 200
    E a mensagem deve ser "Login realizado com sucesso"
    E devo receber um token de autorização

  Esquema do Cenário: Login com senha incorreta
    Quando faço login com a senha "<senha>"
    Então o status da resposta deve ser 401
    E a mensagem deve ser "Email e/ou senha inválidos"

    Exemplos:
      | senha       |
      | senhaErrada |
      | 123         |
