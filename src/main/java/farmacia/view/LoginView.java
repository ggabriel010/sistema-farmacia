package farmacia.view;

public class LoginView {

    public LoginView() {}

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

    public boolean confirmarNovoLogin() {
        return Tela.confirmar("Deseja fazer login novamente?");
    }
}
