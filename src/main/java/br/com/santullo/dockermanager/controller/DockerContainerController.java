package br.com.santullo.dockermanager.controller;

import br.com.santullo.dockermanager.service.DockerService;
import com.github.dockerjava.api.model.Container;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h3>DockerContainerController</h3>
 * <p>
 * Controlador REST para gerenciar containers Docker.
 *
 * <p>
 * Esta classe fornece endpoints para listar, iniciar, parar, criar e excluir containers Docker.
 * Certifique-se de que o Docker esteja em execução e que o cliente tenha permissões suficientes
 * para executar as operações.
 * </p>
 *
 * @author Gabriel Santullo
 * @version 1.0
 * @since 20/09/2024
 */
@RestController
@RequestMapping("/api/container")
public class DockerContainerController {
    private final DockerService dockerService;

    public DockerContainerController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    /**
     * Lista todos os containers Docker disponíveis.
     *
     * @param showAll se true, lista todos os containers, incluindo os parados;
     *                se false, lista apenas os containers em execução.
     * @return lista de containers disponíveis no Docker.
     */
    @GetMapping
    public List<Container> listarContainers(@RequestParam(required = false, defaultValue = "true") boolean showAll) {
        return dockerService.listarContainers(showAll);
    }

    /**
     * Inicia o container especificado pelo ID.
     *
     * @param id ID do container a ser iniciado.
     * @apiNote Certifique-se de que o container está em um estado que permite iniciar (ex: não deve estar em execução).
     */
    @PostMapping("/start/{id}")
    public void iniciarContainer(@PathVariable String id) {
        dockerService.iniciarContainer(id);
    }

    /**
     * Para o container especificado pelo ID.
     *
     * @param id ID do container a ser parado.
     * @apiNote Certifique-se de que o container está em um estado que permite parar (ex: deve estar em execução).
     */
    @PostMapping("/stop/{id}")
    public void pararContainer(@PathVariable String id) {
        dockerService.pararContainer(id);
    }

    /**
     * Cria um novo container a partir da imagem especificada.
     *
     * @param imageName Nome da imagem Docker a ser utilizada para criar o container.
     * @apiNote O nome da imagem deve estar disponível no cliente Docker.
     * Certifique-se de que a imagem está corretamente configurada.
     */
    @PostMapping
    public void criarContainer(@RequestParam String imageName) {
        dockerService.criarContainer(imageName);
    }

    /**
     * Remove o container especificado pelo ID.
     *
     * @param id ID do container a ser removido.
     * @apiNote Certifique-se de que o container está parado antes de tentar removê-lo.
     */
    @DeleteMapping("/{id}")
    public void excluirContainer(@PathVariable String id) {
        dockerService.excluirContainer(id);
    }
}
