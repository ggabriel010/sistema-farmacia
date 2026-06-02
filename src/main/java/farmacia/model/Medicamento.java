package farmacia.model;

import java.time.format.DateTimeParseException;
import java.time.LocalDate;

public class Medicamento {

    private String id;
    private String nome;
    private String descricao;
    private String principioAtivo;
    private double preco;
    private int quantidadeEstoque;
    private int quantidadeMinima;
    private String dataValidade;
    private boolean ativo;
    private Categoria categoria;
    private Fornecedor fornecedor;

    public Medicamento() {
        this.ativo = true;
    }

    public Medicamento(String id, String nome, String descricao, String principioAtivo,
                       double preco, int quantidadeEstoque, int quantidadeMinima,
                       String dataValidade, Categoria categoria, Fornecedor fornecedor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.principioAtivo = principioAtivo;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.quantidadeMinima = quantidadeMinima;
        this.dataValidade = dataValidade;
        this.ativo = true;
        this.categoria = categoria;
        this.fornecedor = fornecedor;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getPrincipioAtivo() { return principioAtivo; }
    public double getPreco() { return preco; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public int getQuantidadeMinima() { return quantidadeMinima; }
    public String getDataValidade() { return dataValidade; }
    public boolean isAtivo() { return ativo; }
    public Categoria getCategoria() { return categoria; }
    public Fornecedor getFornecedor() { return fornecedor; }

    public void setId(String id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPrincipioAtivo(String principioAtivo) { this.principioAtivo = principioAtivo; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public void setQuantidadeMinima(int quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public boolean isEstoqueZerado() {
        return quantidadeEstoque == 0;
    }

    public boolean isEstoqueBaixo() {
        return quantidadeEstoque < quantidadeMinima;
    }

    public boolean isValidadeProxima() {
        if (dataValidade == null || dataValidade.isBlank()) return false;
        try {
            LocalDate validade = LocalDate.parse(dataValidade.trim());
            return !validade.isAfter(LocalDate.now().plusDays(30));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Medicamento{id='" + id + "', nome='" + nome + "', estoque=" + quantidadeEstoque + ", ativo=" + ativo + "}";
    }
}
