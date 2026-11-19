package br.com.joaohqa.serverest.bdd;

import br.com.joaohqa.serverest.config.ApiConfig;
import br.com.joaohqa.serverest.utils.DadosUsuario;
import br.com.joaohqa.serverest.utils.UsuarioClient;
import io.cucumber.java.After;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

public class LoginSteps {

    private String email;
    private String idUsuario;
    private Response resposta;

    @Dado("que existe um usuário cadastrado")
    public void queExisteUmUsuarioCadastrado() {
        ApiConfig.configurar();
        Map<String, Object> usuario = DadosUsuario.usuarioValido();
        email = (String) usuario.get("email");
        idUsuario = UsuarioClient.cadastrarERetornarId(usuario);
    }

    @Quando("faço login com o email e a senha corretos")
    public void facoLoginComEmailESenhaCorretos() {
        resposta = UsuarioClient.login(email, DadosUsuario.SENHA_PADRAO);
    }

    @Quando("faço login com a senha {string}")
    public void facoLoginComASenha(String senha) {
        resposta = UsuarioClient.login(email, senha);
    }

    @Entao("o status da resposta deve ser {int}")
    public void oStatusDaRespostaDeveSer(int status) {
        resposta.then().statusCode(status);
    }

    @E("a mensagem deve ser {string}")
    public void aMensagemDeveSer(String mensagem) {
        resposta.then().body("message", equalTo(mensagem));
    }

    @E("devo receber um token de autorização")
    public void devoReceberUmTokenDeAutorizacao() {
        resposta.then().body("authorization", startsWith("Bearer "));
    }

    @After
    public void limpar() {
        if (idUsuario != null) {
            UsuarioClient.excluir(idUsuario);
        }
    }
}
