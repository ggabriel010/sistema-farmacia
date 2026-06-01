package farmacia.controller;

import farmacia.model.Funcionario;
import farmacia.model.Medicamento;
import farmacia.model.Venda;
import farmacia.repository.MedicamentoRepository;
import farmacia.service.VendaService;
import farmacia.service.VendaService.ItemVendaDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Controla o registro de vendas. Pode ser usado por Funcionário e Administrador.
 */
public class VendaController {

    private final VendaService vendaService;
    private final MedicamentoRepository medicamentoRepository;

    public VendaController(VendaService vendaService, MedicamentoRepository medicamentoRepository) {
        this.vendaService = vendaService;
        this.medicamentoRepository = medicamentoRepository;
    }

    /**
     * Retorna os medicamentos disponíveis para venda (ativos e com estoque > 0).
     */
    public List<Medicamento> listarMedicamentosDisponiveis() {
        List<Medicamento> disponiveis = new ArrayList<>();
        for (Medicamento m : medicamentoRepository.listarAtivos()) {
            if (!m.isEstoqueZerado()) {
                disponiveis.add(m);
            }
        }
        return disponiveis;
    }

    /**
     * Registra uma venda completa.
     *
     * @param funcionario   funcionário responsável
     * @param clienteId     id do cliente (pode ser null para venda avulsa)
     * @param itensDto      lista de itens [medicamentoId, quantidade]
     * @return a Venda registrada
     */
    public Venda registrarVenda(Funcionario funcionario, String clienteId, List<ItemVendaDto> itensDto) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário responsável não informado.");
        }
        if (itensDto == null || itensDto.isEmpty()) {
            throw new IllegalArgumentException("A venda deve ter ao menos um item.");
        }
        return vendaService.registrarVenda(funcionario, clienteId, itensDto);
    }

    /**
     * Retorna todas as vendas registradas.
     */
    public List<Venda> listarVendas() {
        return vendaService.listarVendas();
    }

    /**
     * Verifica se há estoque suficiente para um medicamento/quantidade antes de adicionar o item.
     * Útil para dar feedback ao usuário na View antes de confirmar.
     */
    public boolean verificarEstoque(String medicamentoId, int quantidade) {
        Medicamento med = medicamentoRepository.buscarPorId(medicamentoId);
        if (med == null) return false;
        return med.getQuantidadeEstoque() >= quantidade;
    }
}
