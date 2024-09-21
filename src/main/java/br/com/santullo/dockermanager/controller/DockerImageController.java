package br.com.santullo.dockermanager.controller;

import br.com.santullo.dockermanager.service.DockerService;
import com.github.dockerjava.api.model.Image;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <h3>DockerImageController</h3>
 * <p>
 * Controlador REST para gerenciar operações relacionadas a imagens Docker.
 *
 * <p>
 * Este controlador fornece endpoints para listar todas as imagens Docker disponíveis
 * e para filtrar as imagens com base em um nome fornecido.
 * </p>
 *
 * @author Gabriel Santullo
 * @version 1.0
 * @since 20/09/2024
 */
@RestController
@RequestMapping("/api/images")
public class DockerImageController {

    private final DockerService dockerService;

    /**
     * Construtor que inicializa o DockerImageController com um serviço Docker.
     *
     * @param dockerService Serviço que fornece operações para gerenciar imagens Docker.
     */
    public DockerImageController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    /**
     * Retorna uma lista de todas as imagens Docker disponíveis no cliente Docker.
     *
     * @return lista de imagens Docker.
     * @apiNote Este método consulta o serviço Docker para obter todas as imagens
     * disponíveis, incluindo versões não utilizadas.
     */
    @GetMapping
    public List<Image> listarImagens() {
        return dockerService.listarImagens();
    }

    /**
     * Filtra as imagens Docker com base no nome fornecido.
     *
     * @param filterName Nome a ser utilizado como filtro para buscar imagens.
     *                   O valor padrão é "image-".
     * @return lista de imagens que correspondem ao filtro.
     * @apiNote O filtro é aplicado ao nome da imagem; certifique-se de fornecer
     * um padrão que corresponda às imagens desejadas.
     */
    @GetMapping("/filter")
    public List<Image> filtrarImagens(@RequestParam(required = false, defaultValue = "image-") String filterName) {
        return dockerService.filtrarImagens(filterName);
    }
}
