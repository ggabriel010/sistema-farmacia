package farmacia.view;

/**
 * Tela de login do sistema.
 */
public class LoginView {

    public LoginView() {}

    /**
     * Solicita login e senha ao usuário.
     *
     * @return array [login, senha]
     */
    public String[] solicitarCredenciais() {
        Tela.cabecalho("SISTEMA DE GERENCIAMENTO DE FARMÁCIA — LOGIN");
        String login = Tela.lerLinhaObrigatoria("Login");
        String senha = Tela.lerLinhaObrigatoria("Senha");
        return new String[]{login, senha};
    }

    public void exibirSucesso(String nomeUsuario) {
        Tela.sucesso("Bem-vindo(a), " + nomeUsuario + "!");
    }

    public void exibirErro(String mensagem) {
        Tela.erro(mensagem);
        Tela.pausar();
    }

    /**
     * Pergunta se o usuário deseja tentar novo login após logout.
     *
     * @return true se quiser, false para encerrar o sistema
     */
    public boolean confirmarNovoLogin() {
        return Tela.confirmar("Deseja fazer login novamente?");
    }
}
