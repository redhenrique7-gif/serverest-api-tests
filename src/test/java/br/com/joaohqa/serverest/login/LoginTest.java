package br.com.joaohqa.serverest.login;

import br.com.joaohqa.serverest.config.ApiConfig;
import br.com.joaohqa.serverest.utils.DadosUsuario;
import br.com.joaohqa.serverest.utils.UsuarioClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.*;

@DisplayName("Login - /login")
class LoginTest {

    private static String email;
    private static String idUsuario;

    @BeforeAll
    static void setup() {
        ApiConfig.configurar();
        Map<String, Object> usuario = DadosUsuario.usuarioValido();
        email = (String) usuario.get("email");
        idUsuario = UsuarioClient.cadastrarERetornarId(usuario);
    }

    @AfterAll
    static void limpar() {
        UsuarioClient.excluir(idUsuario);
    }

    @Test
    @DisplayName("Deve fazer login com credenciais válidas e receber token")
    void deveFazerLoginComSucesso() {
        UsuarioClient.login(email, DadosUsuario.SENHA_PADRAO)
            .then()
                .statusCode(200)
                .body("message", equalTo("Login realizado com sucesso"))
                .body("authorization", startsWith("Bearer "));
    }

    @Test
    @DisplayName("Não deve fazer login com senha incorreta")
    void naoDeveLogarComSenhaIncorreta() {
        UsuarioClient.login(email, "senhaErrada")
            .then()
                .statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }

    @Test
    @DisplayName("Não deve aceitar email em formato inválido")
    void naoDeveAceitarEmailInvalido() {
        UsuarioClient.login("email-invalido", DadosUsuario.SENHA_PADRAO)
            .then()
                .statusCode(400)
                .body("email", equalTo("email deve ser um email válido"));
    }
}
