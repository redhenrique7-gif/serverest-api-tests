package br.com.joaohqa.serverest.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Agrupa as chamadas à API para reaproveitar nos testes JUnit e nos steps do Cucumber.
 */
public final class UsuarioClient {

    private UsuarioClient() {
    }

    public static Response cadastrar(Map<String, Object> usuario) {
        return given()
                .contentType(ContentType.JSON)
                .body(usuario)
            .when()
                .post("/usuarios");
    }

    /** Cadastra um usuário e devolve o _id gerado pela API. */
    public static String cadastrarERetornarId(Map<String, Object> usuario) {
        return cadastrar(usuario)
            .then()
                .statusCode(201)
                .extract().path("_id");
    }

    public static Response buscarPorId(String id) {
        return given().when().get("/usuarios/{id}", id);
    }

    public static Response excluir(String id) {
        return given().when().delete("/usuarios/{id}", id);
    }

    public static Response login(String email, String senha) {
        return given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", email, "password", senha))
            .when()
                .post("/login");
    }
}
