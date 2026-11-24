package br.edu.unifio.ecommerce;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutenticacaoWebTests {
    @LocalServerPort
    private int port;

    @Test
    void protegeApiEDemonstrarPermissaoDeAdministrador() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> pagina = get(client, "/", null);
        assertThat(pagina.statusCode()).isEqualTo(200);
        assertThat(pagina.body()).contains("Tipo de acesso");
        assertThat(pagina.body()).contains("rcs1");
        assertThat(pagina.body()).contains("rcs3");

        HttpResponse<String> semCredenciais = get(client, "/responsaveis", null);
        assertThat(semCredenciais.statusCode()).isEqualTo(401);

        HttpResponse<String> admin = get(client, "/responsaveis", "rcs4:1234");
        assertThat(admin.statusCode()).isEqualTo(200);

        HttpResponse<String> monitor = get(client, "/responsaveis", "rcs2:1234");
        assertThat(monitor.statusCode()).isEqualTo(200);
        HttpResponse<String> responsavel = get(client, "/responsaveis", "rcs1:1234");
        assertThat(responsavel.statusCode()).isEqualTo(200);
        HttpResponse<String> aluno = get(client, "/responsaveis", "rcs3:1234");
        assertThat(aluno.statusCode()).isEqualTo(200);
        HttpResponse<String> semPermissao = delete(client, "/responsaveis/1", "rcs2:1234");
        assertThat(semPermissao.statusCode()).isEqualTo(403);
    }

    private HttpResponse<String> get(HttpClient client, String path, String credentials) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET();
        if (credentials != null) {
            builder.header("Authorization", basic(credentials));
        }
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> delete(HttpClient client, String path, String credentials) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .DELETE();
        builder.header("Authorization", basic(credentials));
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private String basic(String credentials) {
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
