package br.com.joaohqa.serverest.usuarios;

import br.com.joaohqa.serverest.config.ApiConfig;
import br.com.joaohqa.serverest.utils.DadosUsuario;
import br.com.joaohqa.serverest.utils.UsuarioClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Usuários - /usuarios")
class UsuariosTest {

    /** Ids criados durante os testes, para limpeza no final. */
    private final List<String> idsCriados = new ArrayList<>();

    @BeforeAll
    static void setup() {
        ApiConfig.configurar();
    }

    @AfterEach
    void limparMassaDeDados() {
        idsCriados.forEach(UsuarioClient::excluir);
        idsCriados.clear();
    }

    @Test
    @DisplayName("Deve listar usuários cadastrados")
    void deveListarUsuarios() {
        given()
        .when()
            .get("/usuarios")
        .then()
            .statusCode(200)
            .body("quantidade", greaterThanOrEqualTo(0))
            .body("usuarios", notNullValue());
    }

    @Test
    @DisplayName("Deve cadastrar usuário com dados válidos")
    void deveCadastrarUsuarioComSucesso() {
        String id = UsuarioClient.cadastrar(DadosUsuario.usuarioValido())
            .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .body("_id", not(emptyOrNullString()))
                .extract().path("_id");

        idsCriados.add(id);
    }

    @Test
    @DisplayName("Não deve cadastrar usuário com email já utilizado")
    void naoDeveCadastrarEmailDuplicado() {
        Map<String, Object> usuario = DadosUsuario.usuarioValido();
        idsCriados.add(UsuarioClient.cadastrarERetornarId(usuario));

        UsuarioClient.cadastrar(usuario)
            .then()
                .statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário com nome em branco")
    void naoDeveCadastrarSemNome() {
        Map<String, Object> usuario = DadosUsuario.usuarioValido();
        usuario.put("nome", "");

        UsuarioClient.cadastrar(usuario)
            .then()
                .statusCode(400)
                .body("nome", equalTo("nome não pode ficar em branco"));
    }

    @Test
    @DisplayName("Deve buscar usuário pelo id")
    void deveBuscarUsuarioPorId() {
        Map<String, Object> usuario = DadosUsuario.usuarioValido();
        String id = UsuarioClient.cadastrarERetornarId(usuario);
        idsCriados.add(id);

        UsuarioClient.buscarPorId(id)
            .then()
                .statusCode(200)
                .body("_id", equalTo(id))
                .body("email", equalTo(usuario.get("email")))
                .body("nome", equalTo(usuario.get("nome")));
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar id inexistente")
    void deveRetornarErroParaIdInexistente() {
        UsuarioClient.buscarPorId("0000000000000000")
            .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    @DisplayName("Deve excluir usuário cadastrado")
    void deveExcluirUsuario() {
        String id = UsuarioClient.cadastrarERetornarId(DadosUsuario.usuarioValido());

        UsuarioClient.excluir(id)
            .then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));

        // Confirma que o usuário realmente não existe mais
        UsuarioClient.buscarPorId(id)
            .then()
                .statusCode(400);
    }
}
