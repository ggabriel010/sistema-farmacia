package farmacia.controller;

import farmacia.model.Administrador;
import farmacia.model.Categoria;
import farmacia.repository.CategoriaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Controla as operações sobre categorias. Apenas Administrador.
 */
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodos() {
        return categoriaRepository.listarTodos();
    }

    public Categoria buscarPorId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID não pode ser vazio.");
        return categoriaRepository.buscarPorId(id);
    }

    public void cadastrar(Administrador solicitante, String nome, String descricao) {
        validarCampo(nome, "Nome");
        validarCampo(descricao, "Descrição");

        Categoria cat = new Categoria();
        cat.setId(UUID.randomUUID().toString());
        cat.setNome(nome.trim());
        cat.setDescricao(descricao.trim());

        categoriaRepository.salvar(cat);
    }

    public void editar(Administrador solicitante, String id, String nome, String descricao) {
        Categoria cat = categoriaRepository.buscarPorId(id);
        if (cat == null) throw new IllegalArgumentException("Categoria não encontrada.");

        if (nome != null && !nome.isBlank()) cat.setNome(nome.trim());
        if (descricao != null && !descricao.isBlank()) cat.setDescricao(descricao.trim());

        categoriaRepository.atualizar(cat);
    }

    public void remover(Administrador solicitante, String id) {
        Categoria cat = categoriaRepository.buscarPorId(id);
        if (cat == null) throw new IllegalArgumentException("Categoria não encontrada.");
        categoriaRepository.remover(id);
    }

    private void validarCampo(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
    }
}
