package br.com.joaohqa.serverest.config;

import io.restassured.RestAssured;

/**
 * Configuração central da API.
 * A URL vem da propriedade "baseUrl" (definida no pom.xml ou na linha de comando).
 */
public final class ApiConfig {

    private ApiConfig() {
    }

    public static void configurar() {
        RestAssured.baseURI = System.getProperty("baseUrl", "https://serverest.dev");
        // Mostra requisição e resposta no console somente quando um teste falha
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
