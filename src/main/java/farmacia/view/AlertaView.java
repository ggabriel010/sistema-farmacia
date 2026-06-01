package farmacia.view;

import farmacia.model.Medicamento;
import farmacia.service.EstoqueService;

import java.util.List;

/**
 * Exibe alertas de estoque baixo e validade próxima ao iniciar o sistema.
 */
public class AlertaView {

    private final EstoqueService estoqueService;

    public AlertaView(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    /**
     * Verifica e exibe todos os alertas do sistema. Chamado pela Main antes do login.
     */
    public void exibirAlertas() {
        List<Medicamento> estoqueBaixo  = estoqueService.listarEstoqueBaixo();
        List<Medicamento> validadeProxima = estoqueService.listarValidadeProxima();

        boolean temAlerta = !estoqueBaixo.isEmpty() || !validadeProxima.isEmpty();

        if (!temAlerta) {
            Tela.sucesso("Nenhum alerta de estoque ou validade no momento.");
            return;
        }

        Tela.cabecalho("⚠  ALERTAS DO SISTEMA");

        if (!estoqueBaixo.isEmpty()) {
            System.out.println("\n  ESTOQUE BAIXO (" + estoqueBaixo.size() + " medicamento(s)):");
            Tela.separador();
            for (Medicamento m : estoqueBaixo) {
                System.out.printf("  %-35s  Estoque: %3d  |  Mínimo: %3d%n",
                        m.getNome(), m.getQuantidadeEstoque(), m.getQuantidadeMinima());
            }
        }

        if (!validadeProxima.isEmpty()) {
            System.out.println("\n  VALIDADE PRÓXIMA (≤ 30 dias) — " + validadeProxima.size() + " medicamento(s):");
            Tela.separador();
            for (Medicamento m : validadeProxima) {
                System.out.printf("  %-35s  Validade: %s%n",
                        m.getNome(), m.getDataValidade());
            }
        }

        Tela.pausar();
    }
}
