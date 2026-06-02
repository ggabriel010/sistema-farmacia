package farmacia.view;

import farmacia.controller.ClienteController;
import farmacia.controller.VendaController;
import farmacia.model.Usuario;

/**
 * Menu principal do Funcionário. Apenas registro de vendas.
 */
public class MenuFuncionarioView {

    private final Usuario funcionario;
    private final VendaController vendaController;
    private final ClienteController clienteController;

    public MenuFuncionarioView(Usuario funcionario,
                                VendaController vendaController) {
        this.funcionario = funcionario;
        this.vendaController = vendaController;
        this.clienteController = null;
    }

    public MenuFuncionarioView(Usuario funcionario,
                                VendaController vendaController,
                                ClienteController clienteController) {
        this.funcionario = funcionario;
        this.vendaController = vendaController;
        this.clienteController = clienteController;
    }

    public void exibir() {
        while (true) {
            Tela.cabecalho("MENU FUNCIONÁRIO — " + funcionario.getNome());
            System.out.println("  1. Registrar Venda");
            System.out.println("  2. Listar Vendas");
            System.out.println("  0. Logout");
            Tela.separador();

            int opcao = Tela.lerOpcao(0, 2);

            switch (opcao) {
                case 1 -> new VendaView(funcionario, vendaController, clienteController).registrarVenda();
                case 2 -> new VendaView(funcionario, vendaController, clienteController).listarVendas();
                case 0 -> {
                    Tela.sucesso("Logout realizado. Até logo, " + funcionario.getNome() + "!");
                    return;
                }
            }
        }
    }
}