package farmacia.service;

import farmacia.model.Administrador;
import farmacia.model.Medicamento;
import farmacia.model.Usuario;
import farmacia.repository.MedicamentoRepository;
import java.util.List;
import java.util.stream.Collectors;


public class EstoqueService {

    private final MedicamentoRepository medicamentoRepository;

    public EstoqueService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }


    public boolean verificarDisponibilidade(String medicamentoId, int quantidade) {
        Medicamento med = buscarOuLancar(medicamentoId);
        return med.getQuantidadeEstoque() >= quantidade;
    }

  
    public void validarEstoqueParaVenda(String medicamentoId, int quantidade) {
        Medicamento med = buscarOuLancar(medicamentoId);

        if (med.isEstoqueZerado()) {
            throw new IllegalStateException(
                    "Venda bloqueada: o medicamento '" + med.getNome() + "' está com estoque zerado.");
        }
        if (med.getQuantidadeEstoque() < quantidade) {
            throw new IllegalStateException(
                    "Estoque insuficiente para '" + med.getNome() + "'. " +
                    "Disponível: " + med.getQuantidadeEstoque() + ", solicitado: " + quantidade + ".");
        }
    }

 
  
    public List<Medicamento> listarEstoqueBaixo() {
        return medicamentoRepository.listarAtivos().stream()
                .filter(Medicamento::isEstoqueBaixo)
                .collect(Collectors.toList());
    }

    public List<Medicamento> listarValidadeProxima() {
        return medicamentoRepository.listarAtivos().stream()
                .filter(Medicamento::isValidadeProxima)
                .collect(Collectors.toList());
    }

 
 
    public void ajustarEstoque(String medicamentoId, int quantidade, Usuario solicitante) {
        if (!(solicitante instanceof Administrador)) {
            throw new SecurityException(
                    "Acesso negado: apenas Administradores podem ajustar o estoque manualmente.");
        }

        Medicamento med = buscarOuLancar(medicamentoId);
        int novoEstoque = med.getQuantidadeEstoque() + quantidade;

        if (novoEstoque < 0) {
            throw new IllegalStateException(
                    "Ajuste inválido: o estoque de '" + med.getNome() +
                    "' ficaria negativo (" + novoEstoque + ").");
        }

        med.setQuantidadeEstoque(novoEstoque);
        medicamentoRepository.atualizar(med);
    }


    void decrementarEstoque(String medicamentoId, int quantidade) {
        Medicamento med = buscarOuLancar(medicamentoId);
        med.setQuantidadeEstoque(med.getQuantidadeEstoque() - quantidade);
        medicamentoRepository.atualizar(med);
    }


    private Medicamento buscarOuLancar(String medicamentoId) {
        Medicamento med = medicamentoRepository.buscarPorId(medicamentoId);
        if (med == null) {
            throw new IllegalArgumentException("Medicamento não encontrado: id = " + medicamentoId);
        }
        return med;
    }
}