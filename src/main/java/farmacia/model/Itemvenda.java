package farmacia.model;

public class ItemVenda {

    private String id;
    private Medicamento medicamento;
    private int quantidade;
    private double precoUnitario;

    public ItemVenda() {
    }

    public ItemVenda(String id, Medicamento medicamento, int quantidade) {
        this.id = id;
        this.medicamento = medicamento;
        this.quantidade = quantidade;
        this.precoUnitario = medicamento.getPreco();
    }

    public String getId() { return id; }
    public Medicamento getMedicamento() { return medicamento; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }

    public void setId(String id) { this.id = id; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }

    public double getSubtotal() {
        return quantidade * precoUnitario;
    }

    @Override
    public String toString() {
        return "ItemVenda{medicamento='" + medicamento.getNome() + "', quantidade=" + quantidade + ", precoUnitario=" + precoUnitario + "}";
    }
}