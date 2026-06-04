package farmacia.controller;

import farmacia.model.Administrador;
import farmacia.model.Fornecedor;
import farmacia.repository.FornecedorRepository;
import java.util.List;
import java.util.UUID;

public class FornecedorController {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorController(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.listarTodos();
    }

    public Fornecedor buscarPorId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID não pode ser vazio.");
        return fornecedorRepository.buscarPorId(id);
    }

    public void cadastrar(Administrador solicitante, String nome, String cnpj,
                          String telefone, String email) {
        validarCampo(nome, "Nome");
        validarCampo(cnpj, "CNPJ");

        Fornecedor forn = new Fornecedor();
        forn.setId(UUID.randomUUID().toString());
        forn.setNome(nome.trim());
        forn.setCnpj(cnpj.trim());
        forn.setTelefone(telefone != null ? telefone.trim() : "");
        forn.setEmail(email != null ? email.trim() : "");

        fornecedorRepository.salvar(forn);
    }

    public void editar(Administrador solicitante, String id, String nome, String cnpj,
                       String telefone, String email) {
        Fornecedor forn = fornecedorRepository.buscarPorId(id);
        if (forn == null) throw new IllegalArgumentException("Fornecedor não encontrado.");

        if (nome != null && !nome.isBlank()) forn.setNome(nome.trim());
        if (cnpj != null && !cnpj.isBlank()) forn.setCnpj(cnpj.trim());
        if (telefone != null && !telefone.isBlank()) forn.setTelefone(telefone.trim());
        if (email != null && !email.isBlank()) forn.setEmail(email.trim());

        fornecedorRepository.atualizar(forn);
    }

    public void remover(Administrador solicitante, String id) {
        Fornecedor forn = fornecedorRepository.buscarPorId(id);
        if (forn == null) throw new IllegalArgumentException("Fornecedor não encontrado.");
        fornecedorRepository.remover(id);
    }

    private void validarCampo(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
    }
}
