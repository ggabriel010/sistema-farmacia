package farmacia.controller;

import farmacia.model.Funcionario;
import farmacia.model.Medicamento;
import farmacia.model.Usuario;
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

    public List<Medicamento> listarMedicamentosDisponiveis() {
        List<Medicamento> disponiveis = new ArrayList<>();
        for (Medicamento m : medicamentoRepository.listarAtivos()) {
            if (!m.isEstoqueZerado()) {
                disponiveis.add(m);
            }
        }
        return disponiveis;
    }

    public Venda registrarVenda(Usuario usuario, String clienteId, List<ItemVendaDto> itensDto) {
        if (usuario == null) {
            throw new IllegalArgumentException("Funcionário responsável não informado.");
        }
        if (!(usuario instanceof Funcionario)) {
            throw new IllegalArgumentException("Usuário não tem permissão para registrar vendas.");
        }
        if (itensDto == null || itensDto.isEmpty()) {
            throw new IllegalArgumentException("A venda deve ter ao menos um item.");
        }
        return vendaService.registrarVenda((Funcionario) usuario, clienteId, itensDto);
    }

    public List<Venda> listarVendas() {
        return vendaService.listarVendas();
    }

    public boolean verificarEstoque(String medicamentoId, int quantidade) {
        Medicamento med = medicamentoRepository.buscarPorId(medicamentoId);
        if (med == null) return false;
        return med.getQuantidadeEstoque() >= quantidade;
    }
}