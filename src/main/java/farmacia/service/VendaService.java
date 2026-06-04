package farmacia.service;

import farmacia.model.*;
import farmacia.repository.ClienteRepository;
import farmacia.repository.MedicamentoRepository;
import farmacia.repository.VendaRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


public class VendaService {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final VendaRepository vendaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ClienteRepository clienteRepository;
    private final EstoqueService estoqueService;

    public VendaService(VendaRepository vendaRepository,
                        MedicamentoRepository medicamentoRepository,
                        ClienteRepository clienteRepository,
                        EstoqueService estoqueService) {
        this.vendaRepository = vendaRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.clienteRepository = clienteRepository;
        this.estoqueService = estoqueService;
    }


    public Venda registrarVenda(Funcionario funcionario,
                                 String clienteId,
                                 List<ItemVendaDto> itensDto) {

       
        if (funcionario == null) {
            throw new IllegalArgumentException("É necessário informar o funcionário responsável pela venda.");
        }
        if (itensDto == null || itensDto.isEmpty()) {
            throw new IllegalArgumentException("A venda deve conter ao menos um item.");
        }

      
        for (ItemVendaDto dto : itensDto) {
            estoqueService.validarEstoqueParaVenda(dto.getMedicamentoId(), dto.getQuantidade());
        }

      
        Venda venda = new Venda();
        venda.setId(UUID.randomUUID().toString());
        venda.setDataHora(LocalDateTime.now().format(FORMATO_DATA));
        venda.setFuncionario(funcionario);

       
        if (clienteId != null && !clienteId.isBlank()) {
            Cliente cliente = clienteRepository.buscarPorId(clienteId);
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente não encontrado: id = " + clienteId);
            }
            venda.setCliente(cliente);
        }

       
        for (ItemVendaDto dto : itensDto) {
            Medicamento med = medicamentoRepository.buscarPorId(dto.getMedicamentoId());
            ItemVenda item = new ItemVenda();
            item.setId(UUID.randomUUID().toString());
            item.setMedicamento(med);
            item.setQuantidade(dto.getQuantidade());
            item.setPrecoUnitario(med.getPreco()); 
            venda.adicionarItem(item);
        }

      
        vendaRepository.salvar(venda);

       
        for (ItemVendaDto dto : itensDto) {
            estoqueService.decrementarEstoque(dto.getMedicamentoId(), dto.getQuantidade());
        }

        return venda;
    }

    public List<Venda> listarVendas() {
        return vendaRepository.listarTodos();
    }

    public List<Venda> listarVendasPorFuncionario(String funcionarioId) {
        return vendaRepository.buscarPorFuncionario(funcionarioId);
    }

    
    public List<Venda> listarVendasPorCliente(String clienteId) {
        return vendaRepository.buscarPorCliente(clienteId);
    }

   
    public static class ItemVendaDto {

        private final String medicamentoId;
        private final int quantidade;

        public ItemVendaDto(String medicamentoId, int quantidade) {
            if (medicamentoId == null || medicamentoId.isBlank()) {
                throw new IllegalArgumentException("medicamentoId não pode ser nulo ou vazio.");
            }
            if (quantidade <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
            }
            this.medicamentoId = medicamentoId;
            this.quantidade = quantidade;
        }

        public String getMedicamentoId() { return medicamentoId; }
        public int getQuantidade()        { return quantidade; }
    }
}