# farmacia
Sistema de Gerenciamento de Farmácia desenvolvido em Java com arquitetura MVC. Controla estoque de medicamentos, registro de vendas, cadastro de clientes, fornecedores e funcionários. Projeto acadêmico — UFERSA, disciplina de Programação Orientada a Objetos.

## Tecnologias

- Java puro
- Arquitetura MVC
- Persistência em JSON

## Estrutura do Projeto

```text
src/main/java/farmacia
├── model
├── repository
├── service
├── controller
├── view
├── util
└── Main.java
```

## Requisitos

É recomendado usar JDK 17 ou superior.

Para verificar se o Java está instalado:

```powershell
java -version
javac -version
```

## Como Compilar

Abra o terminal na raiz do projeto:

```powershell
cd C:\Users\gabri\sistema-farmacia
```

Para compilar as camadas principais:

```powershell
javac -d build src\main\java\farmacia\model\*.java src\main\java\farmacia\repository\*.java src\main\java\farmacia\service\*.java src\main\java\farmacia\util\*.java
```

Para compilar o projeto completo:

```powershell
javac -d build src\main\java\farmacia\*.java src\main\java\farmacia\model\*.java src\main\java\farmacia\repository\*.java src\main\java\farmacia\service\*.java src\main\java\farmacia\util\*.java src\main\java\farmacia\controller\*.java src\main\java\farmacia\view\*.java
```

Se o comando não exibir erro, a compilação foi concluída com sucesso.

## Como Executar

Após compilar o projeto completo, execute:

```powershell
java -cp build farmacia.Main
```

## Arquivos de Dados

Os dados do sistema são armazenados na pasta:

```text
dados
```

Exemplo de arquivo de usuários:

```text
dados/usuarios.json
```

Exemplo de conteúdo:

```json
[
  {
    "id": "1",
    "nome": "Administrador",
    "login": "admin",
    "senha": "admin123",
    "perfil": "ADMINISTRADOR"
  },
  {
    "id": "2",
    "nome": "Funcionario",
    "login": "func",
    "senha": "func123",
    "perfil": "FUNCIONARIO"
  }
]
```

Logins de teste:

```text
admin / admin123
func / func123
```

## Branches do Projeto

- `main`: estrutura inicial
- `develop`: branch de integração
- `feature/karllos-model`: camada Model
- `feature/gabriel-repository`: camadas Repository e Service
- `feature/valderi-controller-view`: camadas Controller e View

## Fluxo de Trabalho

Antes de começar a desenvolver:

```powershell
git checkout feature/gabriel-repository
git fetch origin
git pull origin feature/gabriel-repository
```

Para trazer atualizações da `develop`:

```powershell
git fetch origin
git merge origin/develop
```

Depois das alterações:

```powershell
git status
git add src/main/java/farmacia/repository src/main/java/farmacia/service src/main/java/farmacia/util
git commit -m "feat(repository-service): implementa persistencia JSON e regras de negocio"
git push origin feature/gabriel-repository
```

## Observações

A pasta `build` contém arquivos compilados `.class` e não precisa ser enviada para o GitHub.

Os arquivos JSON em `dados` são usados para persistência local dos dados do sistema.