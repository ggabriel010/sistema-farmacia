package farmacia.view;

import farmacia.controller.*;
import farmacia.model.Administrador;

public class MenuAdminView {

    private final Administrador admin;
    private final MedicamentoController medicamentoController;
    private final CategoriaController categoriaController;
    private final FornecedorController fornecedorController;
    private final ClienteController clienteController;
    private final VendaController vendaController;
    private final UsuarioController usuarioController;

    public MenuAdminView(Administrador admin,
                         MedicamentoController medicamentoController,
                         CategoriaController categoriaController,
                         FornecedorController fornecedorController,
                         ClienteController clienteController,
                         VendaController vendaController,
                         UsuarioController usuarioController) {
        this.admin = admin;
        this.medicamentoController = medicamentoController;
        this.categoriaController = categoriaController;
        this.fornecedorController = fornecedorController;
        this.clienteController = clienteController;
        this.vendaController = vendaController;
        this.usuarioController = usuarioController;
    }

    public void exibir() {
        while (true) {
            Tela.cabecalho("MENU ADMINISTRADOR — " + admin.getNome());
            System.out.println("  1. Medicamentos");
            System.out.println("  2. Categorias");
            System.out.println("  3. Fornecedores");
            System.out.println("  4. Clientes");
            System.out.println("  5. Usuários");
            System.out.println("  6. Registrar Venda");
            System.out.println("  7. Listar Vendas");
            System.out.println("  0. Logout");
            Tela.separador();

            int opcao = Tela.lerOpcao(0, 7);

            switch (opcao) {
                case 1 -> new MedicamentoView(admin, medicamentoController, categoriaController, fornecedorController).exibir();
                case 2 -> new CategoriaView(admin, categoriaController).exibir();
                case 3 -> new FornecedorView(admin, fornecedorController).exibir();
                case 4 -> new ClienteView(admin, clienteController).exibir();
                case 5 -> new UsuarioView(admin, usuarioController).exibir();
                case 6 -> new VendaView(admin, vendaController, clienteController).registrarVenda();
                case 7 -> new VendaView(admin, vendaController, clienteController).listarVendas();
                case 0 -> {
                    Tela.sucesso("Logout realizado. Até logo, " + admin.getNome() + "!");
                    return;
                }
            }
        }
    }
}
