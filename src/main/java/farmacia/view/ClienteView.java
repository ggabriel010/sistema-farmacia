package farmacia.view;

import farmacia.controller.ClienteController;
import farmacia.model.Administrador;
import farmacia.model.Cliente;
import java.util.List;

public class ClienteView {

    private final Administrador admin;
    private final ClienteController clienteController;

    public ClienteView(Administrador admin, ClienteController clienteController) {
        this.admin = admin;
        this.clienteController = clienteController;
    }

    public void exibir() {
        while (true) {
            Tela.cabecalho("CLIENTES");
            System.out.println("  1. Listar todos");
            System.out.println("  2. Cadastrar novo");
            System.out.println("  3. Editar");
            System.out.println("  4. Remover");
            System.out.println("  0. Voltar");
            Tela.separador();

            int opcao = Tela.lerOpcao(0, 4);

            switch (opcao) {
                case 1 -> listarTodos();
                case 2 -> cadastrar();
                case 3 -> editar();
                case 4 -> remover();
                case 0 -> { return; }
            }
        }
    }

    private void listarTodos() {
        Tela.cabecalho("TODOS OS CLIENTES");
        List<Cliente> lista = clienteController.listarTodos();
        if (lista.isEmpty()) {
            Tela.aviso("Nenhum cliente cadastrado.");
        } else {
            System.out.printf("%n  %-36s %-22s %-14s %-15s%n", "ID", "Nome", "CPF", "Telefone");
            Tela.separador();
            for (Cliente c : lista) {
                System.out.printf("  %-36s %-22s %-14s %-15s%n",
                        c.getId(), truncar(c.getNome(), 21), c.getCpf(), c.getTelefone());
            }
        }
        Tela.pausar();
    }

    private void cadastrar() {
        Tela.cabecalho("CADASTRAR CLIENTE");
        String nome     = Tela.lerLinhaObrigatoria("Nome");
        String cpf      = Tela.lerLinhaObrigatoria("CPF");
        String telefone = Tela.lerLinha("Telefone");
        String email    = Tela.lerLinha("E-mail");

        try {
            clienteController.cadastrar(admin, nome, cpf, telefone, email);
            Tela.sucesso("Cliente cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void editar() {
        Tela.cabecalho("EDITAR CLIENTE");
        listarTodos();
        String id = Tela.lerLinhaObrigatoria("ID do cliente a editar");

        Cliente cli = clienteController.buscarPorId(id);
        if (cli == null) {
            Tela.erro("Cliente não encontrado.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Deixe em branco para manter o valor atual.");
        String nome     = Tela.lerLinha("Nome [" + cli.getNome() + "]");
        String cpf      = Tela.lerLinha("CPF [" + cli.getCpf() + "]");
        String telefone = Tela.lerLinha("Telefone [" + cli.getTelefone() + "]");
        String email    = Tela.lerLinha("E-mail [" + cli.getEmail() + "]");

        try {
            clienteController.editar(admin, id, nome, cpf, telefone, email);
            Tela.sucesso("Cliente atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void remover() {
        Tela.cabecalho("REMOVER CLIENTE");
        listarTodos();
        String id = Tela.lerLinhaObrigatoria("ID do cliente a remover");

        Cliente cli = clienteController.buscarPorId(id);
        if (cli == null) {
            Tela.erro("Cliente não encontrado.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Cliente: " + cli.getNome());
        if (!Tela.confirmar("Confirma a remoção?")) {
            Tela.aviso("Operação cancelada.");
            Tela.pausar();
            return;
        }

        try {
            clienteController.remover(admin, id);
            Tela.sucesso("Cliente removido com sucesso.");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private String truncar(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
