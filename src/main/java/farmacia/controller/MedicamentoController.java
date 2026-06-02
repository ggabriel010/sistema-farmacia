package farmacia.controller;

import farmacia.model.Administrador;
import farmacia.model.Categoria;
import farmacia.model.Fornecedor;
import farmacia.model.Medicamento;
import farmacia.repository.CategoriaRepository;
import farmacia.repository.FornecedorRepository;
import farmacia.repository.MedicamentoRepository;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

public class MedicamentoController {

    private final MedicamentoRepository medicamentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    public MedicamentoController(MedicamentoRepository medicamentoRepository,
                                  CategoriaRepository categoriaRepository,
                                  FornecedorRepository fornecedorRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<Medicamento> listarTodos() {
        return medicamentoRepository.listarTodos();
    }

    public List<Medicamento> listarAtivos() {
        return medicamentoRepository.listarAtivos();
    }

    public List<Medicamento> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome de busca não pode ser vazio.");
        }
        return medicamentoRepository.buscarPorNome(nome);
    }

    public Medicamento buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID não pode ser vazio.");
        }
        return medicamentoRepository.buscarPorId(id);
    }

    public void cadastrar(Administrador solicitante, String nome, String descricao,
                          String principioAtivo, String precoStr, String qtdEstoqueStr,
                          String qtdMinimaStr, String dataValidadeStr,
                          String categoriaId, String fornecedorId) {

        validarCampoObrigatorio(nome, "Nome");
        validarCampoObrigatorio(descricao, "Descrição");
        validarCampoObrigatorio(principioAtivo, "Princípio ativo");
        validarCampoObrigatorio(precoStr, "Preço");
        validarCampoObrigatorio(qtdEstoqueStr, "Quantidade em estoque");
        validarCampoObrigatorio(qtdMinimaStr, "Quantidade mínima");
        validarCampoObrigatorio(dataValidadeStr, "Data de validade");
        validarCampoObrigatorio(categoriaId, "Categoria");
        validarCampoObrigatorio(fornecedorId, "Fornecedor");

        double preco = parseDouble(precoStr, "Preço");
        int qtdEstoque = parseInt(qtdEstoqueStr, "Quantidade em estoque");
        int qtdMinima = parseInt(qtdMinimaStr, "Quantidade mínima");
        LocalDate dataValidade = parseData(dataValidadeStr);

        Categoria categoria = categoriaRepository.buscarPorId(categoriaId);
        if (categoria == null) throw new IllegalArgumentException("Categoria não encontrada.");

        Fornecedor fornecedor = fornecedorRepository.buscarPorId(fornecedorId);
        if (fornecedor == null) throw new IllegalArgumentException("Fornecedor não encontrado.");

        Medicamento med = new Medicamento();
        med.setId(UUID.randomUUID().toString());
        med.setNome(nome.trim());
        med.setDescricao(descricao.trim());
        med.setPrincipioAtivo(principioAtivo.trim());
        med.setPreco(preco);
        med.setQuantidadeEstoque(qtdEstoque);
        med.setQuantidadeMinima(qtdMinima);
        med.setDataValidade(dataValidadeStr.trim());
        med.setAtivo(true);
        med.setCategoria(categoria);
        med.setFornecedor(fornecedor);

        medicamentoRepository.salvar(med);
    }

    public void editar(Administrador solicitante, String id, String nome, String descricao,
                       String principioAtivo, String precoStr, String qtdEstoqueStr,
                       String qtdMinimaStr, String dataValidadeStr,
                       String categoriaId, String fornecedorId) {

        Medicamento med = medicamentoRepository.buscarPorId(id);
        if (med == null) throw new IllegalArgumentException("Medicamento não encontrado.");

        if (nome != null && !nome.isBlank()) med.setNome(nome.trim());
        if (descricao != null && !descricao.isBlank()) med.setDescricao(descricao.trim());
        if (principioAtivo != null && !principioAtivo.isBlank()) med.setPrincipioAtivo(principioAtivo.trim());
        if (precoStr != null && !precoStr.isBlank()) med.setPreco(parseDouble(precoStr, "Preço"));
        if (qtdEstoqueStr != null && !qtdEstoqueStr.isBlank()) med.setQuantidadeEstoque(parseInt(qtdEstoqueStr, "Quantidade em estoque"));
        if (qtdMinimaStr != null && !qtdMinimaStr.isBlank()) med.setQuantidadeMinima(parseInt(qtdMinimaStr, "Quantidade mínima"));
        if (dataValidadeStr != null && !dataValidadeStr.isBlank()) {
            parseData(dataValidadeStr);
            med.setDataValidade(dataValidadeStr.trim());
        }
        if (categoriaId != null && !categoriaId.isBlank()) {
            Categoria cat = categoriaRepository.buscarPorId(categoriaId);
            if (cat == null) throw new IllegalArgumentException("Categoria não encontrada.");
            med.setCategoria(cat);
        }
        if (fornecedorId != null && !fornecedorId.isBlank()) {
            Fornecedor forn = fornecedorRepository.buscarPorId(fornecedorId);
            if (forn == null) throw new IllegalArgumentException("Fornecedor não encontrado.");
            med.setFornecedor(forn);
        }

        medicamentoRepository.atualizar(med);
    }

    public void desativar(Administrador solicitante, String id) {
        Medicamento med = medicamentoRepository.buscarPorId(id);
        if (med == null) throw new IllegalArgumentException("Medicamento não encontrado.");
        if (!med.isAtivo()) throw new IllegalStateException("Medicamento já está inativo.");
        med.setAtivo(false);
        medicamentoRepository.atualizar(med);
    }

    private void validarCampoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
    }

    private double parseDouble(String valor, String campo) {
        try {
            double d = Double.parseDouble(valor.trim().replace(",", "."));
            if (d < 0) throw new IllegalArgumentException(campo + " não pode ser negativo.");
            return d;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " inválido: '" + valor + "'.");
        }
    }

    private int parseInt(String valor, String campo) {
        try {
            int i = Integer.parseInt(valor.trim());
            if (i < 0) throw new IllegalArgumentException(campo + " não pode ser negativo.");
            return i;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " inválido: '" + valor + "'.");
        }
    }

    private LocalDate parseData(String valor) {
        try {
            return LocalDate.parse(valor.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de validade inválida. Use o formato yyyy-MM-dd (ex: 2026-12-31).");
        }
    }
}
