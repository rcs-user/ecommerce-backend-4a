package br.edu.unifio.ecommerce;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutenticacaoWebTests {

    @LocalServerPort
    private int port;

    @Test
    void paginaInicialEhPublicaEApiExigeCredenciaisBasic() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> pagina = get(client, "/", null);

        assertThat(pagina.statusCode()).isEqualTo(200);
        assertThat(pagina.body()).contains("login-form");

        HttpResponse<String> semCredenciais = get(client, "/produtos", null);
        assertThat(semCredenciais.statusCode()).isEqualTo(401);
        assertThat(semCredenciais.headers().firstValue("WWW-Authenticate").orElse(""))
                .startsWith("Basic");

        HttpResponse<String> autenticado = get(client, "/produtos", "admin:admin123");
        assertThat(autenticado.statusCode()).isEqualTo(200);
    }

    private HttpResponse<String> get(HttpClient client, String path, String credentials) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET();
        if (credentials != null) {
            String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            request.header("Authorization", "Basic " + encoded);
        }
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }
}
