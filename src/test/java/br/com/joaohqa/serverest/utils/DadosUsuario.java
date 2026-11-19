package br.com.joaohqa.serverest.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gera massa de dados para os testes.
 * O email é sempre único, para que um teste não dependa de outro.
 */
public final class DadosUsuario {

    public static final String SENHA_PADRAO = "teste123";

    private DadosUsuario() {
    }

    public static String emailUnico() {
        return "qa." + UUID.randomUUID().toString().substring(0, 8) + "@teste.com";
    }

    public static Map<String, Object> usuarioValido() {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nome", "Usuário de Teste");
        usuario.put("email", emailUnico());
        usuario.put("password", SENHA_PADRAO);
        usuario.put("administrador", "true");
        return usuario;
    }
}
