package br.com.santullo.dockermanager.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h3>DockerService</h3>
 * <p>
 * Serviço para gerenciar operações relacionadas a containers e imagens Docker.
 *
 * <p>
 * Esta classe encapsula as operações que podem ser realizadas em containers e imagens Docker,
 * como listar, iniciar, parar, criar e excluir containers e listar e filtrar imagens. Ela utiliza o cliente Docker
 * para se comunicar com a API do Docker e executar comandos.
 * </p>
 *
 * @author Gabriel Santullo
 * @version 1.0
 * @apiNote Certifique-se de que o Docker esteja em execução e que o cliente tenha permissões suficientes para executar os métodos.
 * @since 20/09/2024
 */
@Service
public class DockerService {

    private final DockerClient dockerClient;

    /**
     * Construtor que inicializa o DockerService com um cliente Docker.
     *
     * @param dockerClient cliente Docker utilizado para executar os comandos.
     */
    public DockerService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * Retorna uma lista de containers Docker atualmente disponíveis no cliente Docker.
     *
     * @param showAll boolean que determina se todos os containers devem ser apresentados
     *                (incluindo os parados) ou somente os que estão em execução.
     * @return lista de containers disponíveis no Docker.
     * @apiNote A lista pode incluir containers em estados diversos, dependendo do valor de showAll.
     */
    public List<Container> listarContainers(boolean showAll) {
        return dockerClient.listContainersCmd().withShowAll(showAll).exec();
    }

    /**
     * Retorna uma lista de imagens Docker atualmente disponíveis no cliente Docker.
     *
     * @return lista de imagens disponíveis no Docker.
     * @apiNote A lista de imagens pode incluir versões não utilizadas que não estão em containers ativos.
     */
    public List<Image> listarImagens() {
        return dockerClient.listImagesCmd().exec();
    }

    /**
     * Retorna uma lista de imagens Docker filtradas pelo nome da imagem.
     *
     * @param filterName Nome a ser utilizado como filtro para buscar imagens.
     * @return lista de imagens que correspondem ao filtro.
     * @apiNote O filtro é sensível ao nome da imagem; portanto, certifique-se de fornecer
     * um nome exato ou um padrão que corresponda às imagens desejadas.
     */
    public List<Image> filtrarImagens(String filterName) {
        return dockerClient.listImagesCmd().withImageNameFilter(filterName).exec();
    }

    /**
     * Inicia o container especificado pelo ID.
     *
     * @param containerId ID do container a ser iniciado.
     * @apiNote O container deve estar em um estado que permita iniciar (por exemplo, não pode estar já em execução).
     */
    public void iniciarContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    /**
     * Para o container especificado pelo ID.
     *
     * @param containerId ID do container a ser parado.
     * @apiNote O container deve estar em execução para ser parado. Caso contrário, pode ocorrer uma exceção.
     */
    public void pararContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * Remove o container especificado pelo ID.
     *
     * @param containerId ID do container a ser removido.
     * @apiNote Certifique-se de que o container está parado antes de tentar removê-lo.
     * A remoção de um container em execução resultará em um erro.
     */
    public void excluirContainer(String containerId) {
        try (RemoveContainerCmd removeContainerCmd = dockerClient.removeContainerCmd(containerId)) {
            removeContainerCmd.exec();
        }
    }

    /**
     * Cria um novo container a partir da imagem especificada.
     *
     * @param imageName Nome da imagem Docker a ser utilizada para criar o container.
     * @apiNote A imagem deve estar disponível no cliente Docker; caso contrário, a operação falhará.
     */
    public void criarContainer(String imageName) {
        try (CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageName)) {
            createContainerCmd.exec();
        }
    }
}
