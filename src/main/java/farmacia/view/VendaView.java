package farmacia.view;

import farmacia.controller.ClienteController;
import farmacia.controller.VendaController;
import farmacia.model.Cliente;
import farmacia.model.Funcionario;
import farmacia.model.ItemVenda;
import farmacia.model.Medicamento;
import farmacia.model.Venda;
import farmacia.service.VendaService.ItemVendaDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Tela de registro e listagem de vendas.
 * Usada tanto por Funcionário quanto por Administrador (que também é Funcionario no fluxo de venda).
 */
public class VendaView {

    private final Funcionario funcionario;
    private final VendaController vendaController;
    private final ClienteController clienteController; // pode ser null para Funcionário simples

    public VendaView(Funcionario funcionario,
                     VendaController vendaController,
                     ClienteController clienteController) {
        this.funcionario = funcionario;
        this.vendaController = vendaController;
        this.clienteController = clienteController;
    }

    /**
     * Fluxo completo de registro de uma venda.
     */
    public void registrarVenda() {
        Tela.cabecalho("REGISTRAR VENDA");

        // 1. Verificar se há medicamentos disponíveis
        List<Medicamento> disponiveis = vendaController.listarMedicamentosDisponiveis();
        if (disponiveis.isEmpty()) {
            Tela.aviso("Nenhum medicamento disponível para venda no momento.");
            Tela.pausar();
            return;
        }

        // 2. Vincular cliente (opcional)
        String clienteId = selecionarCliente();

        // 3. Montar os itens da venda
        List<ItemVendaDto> itens = new ArrayList<>();
        boolean adicionandoItens = true;

        while (adicionandoItens) {
            exibirMedicamentosDisponiveis(disponiveis);

            String medId = Tela.lerLinhaObrigatoria("ID do medicamento (ou ENTER para finalizar)");

            // Permite finalizar digitando qualquer coisa que não seja um ID válido,
            // mas damos opção explícita de sair após cada item
            Medicamento medSelecionado = encontrarMedicamento(disponiveis, medId);
            if (medSelecionado == null) {
                Tela.erro("Medicamento não encontrado ou indisponível.");
                Tela.pausar();
                // Perguntar se quer tentar de novo
                if (!Tela.confirmar("Deseja adicionar outro medicamento?")) {
                    adicionandoItens = false;
                }
                continue;
            }

            // Quantidade
            int quantidade = lerQuantidade(medSelecionado);
            if (quantidade <= 0) continue;

            // Verificar estoque antes de adicionar
            if (!vendaController.verificarEstoque(medSelecionado.getId(), quantidade)) {
                Tela.erro("Estoque insuficiente. Disponível: " + medSelecionado.getQuantidadeEstoque());
                Tela.pausar();
                continue;
            }

            itens.add(new ItemVendaDto(medSelecionado.getId(), quantidade));
            Tela.sucesso("Item adicionado: " + medSelecionado.getNome() + " x" + quantidade);

            exibirResumoItens(itens, disponiveis);

            adicionandoItens = Tela.confirmar("Adicionar mais itens?");
        }

        if (itens.isEmpty()) {
            Tela.aviso("Nenhum item adicionado. Venda cancelada.");
            Tela.pausar();
            return;
        }

        // 4. Confirmar a venda
        System.out.println();
        exibirResumoItens(itens, disponiveis);
        if (!Tela.confirmar("Confirmar a venda?")) {
            Tela.aviso("Venda cancelada.");
            Tela.pausar();
            return;
        }

        // 5. Registrar
        try {
            Venda venda = vendaController.registrarVenda(funcionario, clienteId, itens);
            Tela.sucesso("Venda registrada com sucesso!");
            exibirComprovante(venda);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    /**
     * Lista todas as vendas registradas.
     */
    public void listarVendas() {
        Tela.cabecalho("VENDAS REGISTRADAS");
        List<Venda> vendas = vendaController.listarVendas();
        if (vendas.isEmpty()) {
            Tela.aviso("Nenhuma venda registrada.");
        } else {
            System.out.printf("%n  %-36s %-20s %-20s %10s%n", "ID", "Data/Hora", "Funcionário", "Total");
            Tela.separador();
            for (Venda v : vendas) {
                String cliente = v.getCliente() != null ? v.getCliente().getNome() : "Avulso";
                System.out.printf("  %-36s %-20s %-20s R$%8.2f%n",
                        v.getId(),
                        v.getDataHora() != null ? String.valueOf(v.getDataHora()).replace("T", " ") : "—",
                        truncar(v.getFuncionario().getNome(), 19),
                        v.calcularTotal());
                System.out.printf("  %36s Cliente: %s%n", "", cliente);
                Tela.separador();
            }
        }
        Tela.pausar();
    }

    // ---- auxiliares ----

    private String selecionarCliente() {
        if (!Tela.confirmar("Vincular esta venda a um cliente cadastrado?")) {
            return null;
        }

        if (clienteController != null) {
            List<Cliente> clientes = clienteController.listarTodos();
            if (clientes.isEmpty()) {
                Tela.aviso("Nenhum cliente cadastrado. Prosseguindo como venda avulsa.");
                return null;
            }
            System.out.println("\n  Clientes disponíveis:");
            for (Cliente c : clientes) {
                System.out.printf("    [%s] %s — CPF: %s%n", c.getId(), c.getNome(), c.getCpf());
            }
        }

        return Tela.lerLinhaObrigatoria("ID do cliente");
    }

    private void exibirMedicamentosDisponiveis(List<Medicamento> lista) {
        System.out.println("\n  MEDICAMENTOS DISPONÍVEIS:");
        System.out.printf("  %-36s %-25s %8s %6s%n", "ID", "Nome", "Preço", "Estq.");
        Tela.separador();
        for (Medicamento m : lista) {
            System.out.printf("  %-36s %-25s R$%5.2f %6d%n",
                    m.getId(), truncar(m.getNome(), 24), m.getPreco(), m.getQuantidadeEstoque());
        }
        System.out.println();
    }

    private Medicamento encontrarMedicamento(List<Medicamento> lista, String id) {
        for (Medicamento m : lista) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    private int lerQuantidade(Medicamento med) {
        while (true) {
            String entrada = Tela.lerLinha("Quantidade (máx. " + med.getQuantidadeEstoque() + ")");
            try {
                int qtd = Integer.parseInt(entrada);
                if (qtd <= 0) {
                    Tela.erro("Quantidade deve ser maior que zero.");
                    continue;
                }
                return qtd;
            } catch (NumberFormatException e) {
                Tela.erro("Digite um número inteiro válido.");
            }
        }
    }

    private void exibirResumoItens(List<ItemVendaDto> itens, List<Medicamento> disponiveis) {
        System.out.println("\n  ITENS DA VENDA:");
        Tela.separador();
        double total = 0;
        int numero = 1;
        for (ItemVendaDto dto : itens) {
            Medicamento med = encontrarMedicamento(disponiveis, dto.getMedicamentoId());
            if (med != null) {
                double subtotal = med.getPreco() * dto.getQuantidade();
                total += subtotal;
                System.out.printf("  %2d. %-25s x%3d  R$%6.2f  =>  R$%7.2f%n",
                        numero++, truncar(med.getNome(), 24),
                        dto.getQuantidade(), med.getPreco(), subtotal);
            }
        }
        Tela.separador();
        System.out.printf("  %-39s TOTAL:  R$%7.2f%n", "", total);
        System.out.println();
    }

    private void exibirComprovante(Venda venda) {
        Tela.cabecalho("COMPROVANTE DE VENDA");
        Tela.info("ID da Venda:  " + venda.getId());
        Tela.info("Data/Hora:   " + (venda.getDataHora() != null ? String.valueOf(venda.getDataHora()).replace("T", " ") : "—"));
        Tela.info("Funcionário: " + venda.getFuncionario().getNome());
        Tela.info("Cliente:     " + (venda.getCliente() != null ? venda.getCliente().getNome() : "Avulso"));
        System.out.println();
        System.out.println("  Itens:");
        Tela.separador();
        for (ItemVenda item : venda.getItens()) {
            System.out.printf("  %-28s x%3d  R$%6.2f  =>  R$%7.2f%n",
                    truncar(item.getMedicamento().getNome(), 27),
                    item.getQuantidade(),
                    item.getPrecoUnitario(),
                    item.getSubtotal());
        }
        Tela.separador();
        System.out.printf("  %-39s TOTAL:  R$%7.2f%n", "", venda.calcularTotal());
    }

    private String truncar(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
