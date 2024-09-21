package br.com.santullo.dockermanager.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.Duration;

/**
 * <h3>DockerClientConfig</h3>
 *
 * @author Gabriel Santullo
 * @version 1.0
 * @apiNote Esta classe configura a conexão com o Docker. Certifique-se de que o Docker esteja em execução e que o cliente tenha permissões suficientes.
 * @since 20/09/2024
 */
@Configuration
public class DockerClientConfig {

    @Value("${docker.host}")
    private String dockerHost;

    /**
     * Cria e configura uma instância do DockerClient.
     *
     * @return uma instância configurada de DockerClient
     * @apiNote Se for uma conexão TCP, a verificação TLS deve ser verdadeira para garantir a segurança da comunicação.
     * Se a verificação TLS falhar, a conexão com o Docker não será estabelecida e uma exceção pode ser lançada,
     * o que pode comprometer a segurança da comunicação.
     * Para conexões Unix, a verificação TLS deve ser falsa, pois não é necessária.
     */

    @Bean
    @Lazy(false)
    public DockerClient buildDockerClient() {
        DefaultDockerClientConfig.Builder dockerClientConfigBuilder = DefaultDockerClientConfig
                .createDefaultConfigBuilder();

        if (this.dockerHost != null && this.dockerHost.startsWith("tcp://")) {
            dockerClientConfigBuilder
                    .withDockerHost(this.dockerHost)
                    .withDockerTlsVerify(false)
                    .withApiVersion(RemoteApiVersion.VERSION_1_24);
        }

        DefaultDockerClientConfig dockerClientConfig = dockerClientConfigBuilder.build();

        ApacheDockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .maxConnections(5)
                .connectionTimeout(Duration.ofMillis(100))
                .responseTimeout(Duration.ofSeconds(3))
                .build();

        DockerClient client = DockerClientBuilder.getInstance(dockerClientConfig)
                .withDockerHttpClient(dockerHttpClient)
                .build();

        client.pingCmd().exec();

        return client;
    }
}
