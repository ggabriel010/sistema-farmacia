package farmacia.model;

import java.util.ArrayList;
import java.util.List;

public class Venda {

    private String id;
    private String dataHora;   // formato ISO: "2025-06-01T14:30:00"
    private Funcionario funcionario;
    private Cliente cliente;
    private List<ItemVenda> itens;

    public Venda() {
        this.itens = new ArrayList<>();
    }

    public Venda(String id, Funcionario funcionario, Cliente cliente) {
        this.id = id;
        this.funcionario = funcionario;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getDataHora() { return dataHora; }
    public Funcionario getFuncionario() { return funcionario; }
    public Cliente getCliente() { return cliente; }
    public List<ItemVenda> getItens() { return itens; }

    public void setId(String id) { this.id = id; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }

    public void adicionarItem(ItemVenda item) {
        itens.add(item);
    }

    public double calcularTotal() {
        return itens.stream()
                .mapToDouble(ItemVenda::getSubtotal)
                .sum();
    }

    @Override
    public String toString() {
        return "Venda{id='" + id + "', funcionario='" + (funcionario != null ? funcionario.getNome() : "—") +
               "', cliente='" + (cliente != null ? cliente.getNome() : "Avulso") +
               "', total=" + calcularTotal() + "}";
    }
}