package farmacia;

import farmacia.controller.*;
import farmacia.repository.*;
import farmacia.service.*;
import farmacia.view.AlertaView;

public class Main {

    public static void main(String[] args) {

        CategoriaRepository   categoriaRepo   = new CategoriaRepository();
        FornecedorRepository  fornecedorRepo  = new FornecedorRepository();
        ClienteRepository     clienteRepo     = new ClienteRepository();
        UsuarioRepository     usuarioRepo     = new UsuarioRepository();
        MedicamentoRepository medicamentoRepo =
                new MedicamentoRepository(categoriaRepo, fornecedorRepo);
        VendaRepository vendaRepo =
                new VendaRepository(usuarioRepo, clienteRepo, medicamentoRepo);

        AutenticacaoService autenticacaoService = new AutenticacaoService(usuarioRepo);
        EstoqueService      estoqueService      = new EstoqueService(medicamentoRepo);
        VendaService        vendaService        = new VendaService(
                vendaRepo, medicamentoRepo, clienteRepo, estoqueService);

        UsuarioController    usuarioController    = new UsuarioController(usuarioRepo);
        MedicamentoController medicamentoController =
                new MedicamentoController(medicamentoRepo, categoriaRepo, fornecedorRepo);
        CategoriaController  categoriaController  = new CategoriaController(categoriaRepo);
        FornecedorController fornecedorController = new FornecedorController(fornecedorRepo);
        ClienteController    clienteController    = new ClienteController(clienteRepo);
        VendaController      vendaController      = new VendaController(vendaService, medicamentoRepo);

        LoginController loginController = new LoginController(
                autenticacaoService,
                medicamentoController,
                categoriaController,
                fornecedorController,
                clienteController,
                vendaController,
                usuarioController
        );

        new AlertaView(estoqueService).exibirAlertas();

        loginController.iniciar();

        System.out.println("\n  Sistema encerrado. Até logo!\n");
    }
}
