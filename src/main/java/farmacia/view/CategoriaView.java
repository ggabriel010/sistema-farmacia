package farmacia.view;

import farmacia.controller.CategoriaController;
import farmacia.model.Administrador;
import farmacia.model.Categoria;
import java.util.List;

public class CategoriaView {

    private final Administrador admin;
    private final CategoriaController categoriaController;

    public CategoriaView(Administrador admin, CategoriaController categoriaController) {
        this.admin = admin;
        this.categoriaController = categoriaController;
    }

    public void exibir() {
        while (true) {
            Tela.cabecalho("CATEGORIAS");
            System.out.println("  1. Listar todas");
            System.out.println("  2. Cadastrar nova");
            System.out.println("  3. Editar");
            System.out.println("  4. Remover");
            System.out.println("  0. Voltar");
            Tela.separador();

            int opcao = Tela.lerOpcao(0, 4);

            switch (opcao) {
                case 1 -> listarTodas();
                case 2 -> cadastrar();
                case 3 -> editar();
                case 4 -> remover();
                case 0 -> { return; }
            }
        }
    }

    private void listarTodas() {
        Tela.cabecalho("TODAS AS CATEGORIAS");
        List<Categoria> lista = categoriaController.listarTodos();
        if (lista.isEmpty()) {
            Tela.aviso("Nenhuma categoria cadastrada.");
        } else {
            System.out.printf("%n  %-36s %-20s %-30s%n", "ID", "Nome", "Descrição");
            Tela.separador();
            for (Categoria c : lista) {
                System.out.printf("  %-36s %-20s %-30s%n",
                        c.getId(), c.getNome(), truncar(c.getDescricao(), 29));
            }
        }
        Tela.pausar();
    }

    private void cadastrar() {
        Tela.cabecalho("CADASTRAR CATEGORIA");
        String nome      = Tela.lerLinhaObrigatoria("Nome");
        String descricao = Tela.lerLinhaObrigatoria("Descrição");

        try {
            categoriaController.cadastrar(admin, nome, descricao);
            Tela.sucesso("Categoria cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void editar() {
        Tela.cabecalho("EDITAR CATEGORIA");
        listarTodas();
        String id = Tela.lerLinhaObrigatoria("ID da categoria a editar");

        Categoria cat = categoriaController.buscarPorId(id);
        if (cat == null) {
            Tela.erro("Categoria não encontrada.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Deixe em branco para manter o valor atual.");
        String nome      = Tela.lerLinha("Nome [" + cat.getNome() + "]");
        String descricao = Tela.lerLinha("Descrição [" + cat.getDescricao() + "]");

        try {
            categoriaController.editar(admin, id, nome, descricao);
            Tela.sucesso("Categoria atualizada com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void remover() {
        Tela.cabecalho("REMOVER CATEGORIA");
        listarTodas();
        String id = Tela.lerLinhaObrigatoria("ID da categoria a remover");

        Categoria cat = categoriaController.buscarPorId(id);
        if (cat == null) {
            Tela.erro("Categoria não encontrada.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Categoria: " + cat.getNome());
        if (!Tela.confirmar("Confirma a remoção?")) {
            Tela.aviso("Operação cancelada.");
            Tela.pausar();
            return;
        }

        try {
            categoriaController.remover(admin, id);
            Tela.sucesso("Categoria removida com sucesso.");
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
