import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import model.Categoria;
import model.FormaPagamento;
import model.MetaFinanceira;
import model.Transacao;
import model.Usuario;
import service.CategoriaService;
import service.FormaPagamentoService;
import service.MetaFinanceiraService;
import service.TransacaoService;
import service.UsuarioService;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Usuario usuarioLogado = null;

    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        menuPrincipal();
    }

    private static void menuPrincipal() {
        UsuarioService usuarioService = new UsuarioService();
        
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Login");
            System.out.println("2. Cadastrar Usuário");
            System.out.println("0. Sair");
            System.out.print("Opção: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 0:
                    System.out.println("Saindo do sistema...");
                    return;
                case 1:
                    login(usuarioService);
                    if (usuarioLogado != null) {
                        menuSistema();
                    }
                    break;
                case 2:
                    cadastrarUsuario(usuarioService);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void login(UsuarioService usuarioService) {
        try {
            System.out.println("\n=== Login ===");
            System.out.print("Email ou CPF: ");
            String identificador = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            Usuario usuario = usuarioService.login(identificador, senha);
            if (usuario != null) {
                usuarioLogado = usuario;
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Credenciais inválidas!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar login: " + e.getMessage());
        }
    }

    private static void cadastrarUsuario(UsuarioService usuarioService) {
        try {
            System.out.println("\n=== Cadastro de Usuário ===");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Papel (ADMIN/USER): ");
            String papel = scanner.nextLine().toUpperCase();

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuario.setCpf(cpf);
            usuario.setPapel(papel);

            usuarioService.cadastrarUsuario(usuario);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    private static boolean exibirMenuPrincipal() {
        System.out.println("\n==== MENU PRINCIPAL =====");
        System.out.println("1 - Transações");
        if (usuarioLogado.getPapel().equalsIgnoreCase("ADMIN")) {
            System.out.println("2 - Usuários");
        }
        System.out.println("3 - Categorias");
        System.out.println("4 - Meta Financeira");
        System.out.println("5 - Formas de Pagamento");
        System.out.println("6 - Sair");
        System.out.println("7 - Logout");
        System.out.println("=========================");

        int opcao = lerInteiro("Escolha uma opção: ");
        limparTela();
        switch (opcao) {
            case 1:
                menuTransacoes();
                break;
            case 2:
                if (usuarioLogado.getPapel().equalsIgnoreCase("ADMIN")) {
                    menuUsuarios();
                } else {
                    System.out.println("Acesso negado: apenas administradores podem acessar o menu de usuários.");
                }
                break;
            case 3:
                menuCategorias();
                break;
            case 4:
                menuMetasFinanceiras();
                break;
            case 5:
                menuFormasPagamento();
                break;
            case 6:
                return false;
            case 7:
                fazerLogout();
                break;
            default:
                System.out.println("Opção inválida!");
        }

        return true;
    }

    private static void fazerLogout() {
        usuarioLogado = null;
        System.out.println("Logout realizado com sucesso!");
    }

    private static void menuTransacoes() {
        TransacaoService transacaoService = new TransacaoService();
        
        while (true) {
            System.out.println("\n=== Menu de Transações ===");
            System.out.println("1. Listar Transações");
            System.out.println("2. Adicionar Transação");
            System.out.println("3. Atualizar Transação");
            System.out.println("4. Excluir Transação");
            System.out.println("5. Listar por Período");
            System.out.println("6. Listar por Tipo");
            if (usuarioLogado.getPapel().equals("ADMIN")) {
                System.out.println("7. Listar Todas as Transações");
            }
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 0:
                    return;
                case 1:
                    listarTransacoes(transacaoService);
                    break;
                case 2:
                    adicionarTransacao(transacaoService);
                    break;
                case 3:
                    atualizarTransacao(transacaoService);
                    break;
                case 4:
                    excluirTransacao(transacaoService);
                    break;
                case 5:
                    listarTransacoesPorPeriodo(transacaoService);
                    break;
                case 6:
                    listarTransacoesPorTipo(transacaoService);
                    break;
                case 7:
                    if (usuarioLogado.getPapel().equals("ADMIN")) {
                        listarTodasTransacoes(transacaoService);
                    } else {
                        System.out.println("Acesso negado! Apenas administradores podem acessar esta opção.");
                    }
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void gerarTransacoesExemplo(TransacaoService transacaoService) {
        try {
            // Primeiro, vamos verificar se existem categorias
            CategoriaService categoriaService = new CategoriaService();
            List<Categoria> categorias = categoriaService.listarCategorias();
            
            if (categorias.isEmpty()) {
                System.out.println("Erro: Não existem categorias cadastradas. Por favor, cadastre algumas categorias primeiro.");
                return;
            }

            // Verificar se existem formas de pagamento
            FormaPagamentoService formaService = new FormaPagamentoService();
            List<FormaPagamento> formas = formaService.listarFormas();
            
            if (formas.isEmpty()) {
                System.out.println("Erro: Não existem formas de pagamento cadastradas. Por favor, cadastre algumas formas primeiro.");
                return;
            }

            // Array de transações de exemplo
            Transacao[] transacoesExemplo = {
                new Transacao(BigDecimal.valueOf(1500.00), LocalDate.parse("2024-03-01"), "Salário", usuarioLogado.getId(), categorias.get(0).getId(), formas.get(0).getId(), "E"),
                new Transacao(BigDecimal.valueOf(350.00), LocalDate.parse("2024-03-02"), "Freelance", usuarioLogado.getId(), categorias.get(0).getId(), formas.get(0).getId(), "E"),
                new Transacao(BigDecimal.valueOf(1200.00), LocalDate.parse("2024-03-03"), "Aluguel", usuarioLogado.getId(), categorias.get(1).getId(), formas.get(0).getId(), "S"),
                new Transacao(BigDecimal.valueOf(450.00), LocalDate.parse("2024-03-04"), "Supermercado", usuarioLogado.getId(), categorias.get(1).getId(), formas.get(0).getId(), "S"),
                new Transacao(BigDecimal.valueOf(200.00), LocalDate.parse("2024-03-05"), "Conta de Luz", usuarioLogado.getId(), categorias.get(1).getId(), formas.get(0).getId(), "S"),
                new Transacao(BigDecimal.valueOf(150.00), LocalDate.parse("2024-03-06"), "Internet", usuarioLogado.getId(), categorias.get(1).getId(), formas.get(0).getId(), "S"),
                new Transacao(BigDecimal.valueOf(300.00), LocalDate.parse("2024-03-07"), "Bônus", usuarioLogado.getId(), categorias.get(0).getId(), formas.get(0).getId(), "E"),
                new Transacao(BigDecimal.valueOf(180.00), LocalDate.parse("2024-03-08"), "Combustível", usuarioLogado.getId(), categorias.get(1).getId(), formas.get(0).getId(), "S"),
                new Transacao(BigDecimal.valueOf(250.00), LocalDate.parse("2024-03-09"), "Lazer", usuarioLogado.getId(), categorias.get(1).getId(), formas.get(0).getId(), "S"),
                new Transacao(BigDecimal.valueOf(400.00), LocalDate.parse("2024-03-10"), "Investimento", usuarioLogado.getId(), categorias.get(0).getId(), formas.get(0).getId(), "E")
            };

            // Cadastrar cada transação
            for (Transacao transacao : transacoesExemplo) {
                transacaoService.adicionarTransacao(transacao);
            }

            System.out.println("10 transações de exemplo foram geradas com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao gerar transações de exemplo: " + e.getMessage());
        }
    }

    private static void listarTransacoes(TransacaoService transacaoService) {
        try {
            System.out.println("\n=== Listar Transações ===");
            List<Transacao> transacoes = transacaoService.listarTransacoes(usuarioLogado.getId());
            
            if (transacoes.isEmpty()) {
                System.out.println("Não há transações cadastradas.");
                return;
            }

            System.out.println("\nTransações:");
            for (Transacao transacao : transacoes) {
                System.out.println(transacao);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar transações: " + e.getMessage());
        }
    }

    private static void adicionarTransacao(TransacaoService transacaoService) {
        try {
            System.out.println("\n=== ADICIONAR TRANSAÇÃO ===");
            String descricao = lerString("Descrição: ");
            BigDecimal valor = BigDecimal.valueOf(lerDouble("Valor: "));
            LocalDate data = LocalDate.parse(lerString("Data (YYYY-MM-DD): "));
            
            // listar todas as categorias disponíveis
            CategoriaService categoriaService = new CategoriaService();
            List<Categoria> categorias = categoriaService.listarCategorias();
            
            if (categorias.isEmpty()) {
                System.out.println("Erro: Não existem categorias cadastradas. Por favor, cadastre algumas categorias primeiro.");
                return;
            }
            System.out.println("\nCategorias Disponíveis:");
            System.out.printf("%-5s | %-20s | %-10s%n", "ID", "Nome", "Tipo");
            System.out.println("-".repeat(40));
            for (Categoria c : categorias) {
                System.out.printf("%-5d | %-20s | %-10s%n", 
                    c.getId(), c.getNome(), c.getTipo());
            }
            System.out.println();
            
            int categoriaId;
            boolean categoriaValida;
            do {
                categoriaId = lerInteiro("ID da Categoria: ");
                categoriaValida = false;
                for (Categoria c : categorias) {
                    if (c.getId() == categoriaId) {
                        categoriaValida = true;
                        break;
                    }
                }
                if (!categoriaValida) {
                    System.out.println("Categoria inválida! Por favor, escolha um ID da lista acima.");
                }
            } while (!categoriaValida);

            // listar todas as formas de pagamento disponíveis
            FormaPagamentoService formaService = new FormaPagamentoService();
            List<FormaPagamento> formas = formaService.listarFormas();
            
            if (formas.isEmpty()) {
                System.out.println("Erro: Não existem formas de pagamento cadastradas. Por favor, cadastre algumas formas primeiro.");
                return;
            }
            System.out.println("\nFormas de Pagamento Disponíveis:");
            System.out.printf("%-5s | %-20s%n", "ID", "Formato");
            System.out.println("-".repeat(30));
            for (FormaPagamento f : formas) {
                System.out.printf("%-5d | %-20s%n", 
                    f.getId(), f.getFormato());
            }
            System.out.println();
            
            int formaId;
            boolean formaValida;
            do {
                formaId = lerInteiro("ID da Forma de Pagamento: ");
                formaValida = false;
                for (FormaPagamento f : formas) {
                    if (f.getId() == formaId) {
                        formaValida = true;
                        break;
                    }
                }
                if (!formaValida) {
                    System.out.println("Forma de pagamento inválida! Por favor, escolha um ID da lista acima.");
                }
            } while (!formaValida);
            
            String tipo;
            do {
                tipo = lerString("Tipo (E=Entrada, S=Saída): ").trim().toUpperCase();
                if (!tipo.equals("E") && !tipo.equals("S")) {
                    System.out.println("Tipo inválido. Use 'E' para Entrada ou 'S' para Saída.");
                }
            } while (!tipo.equals("E") && !tipo.equals("S"));

            Transacao transacao = new Transacao(valor, data, descricao, usuarioLogado.getId(), categoriaId, formaId, tipo);
            transacaoService.adicionarTransacao(transacao);
            System.out.println("Transação adicionada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao adicionar transação: " + e.getMessage());
        }
    }

    private static void atualizarTransacao(TransacaoService transacaoService) {
        try {
            // Listar todas as transações primeiro
            List<Transacao> transacoes = transacaoService.listarTransacoes(usuarioLogado.getId());
            if (transacoes.isEmpty()) {
                System.out.println("Nenhuma transação encontrada para atualizar.");
                return;
            }

            System.out.println("\nLista de Transações Disponíveis:");
            for (Transacao t : transacoes) {
                System.out.printf("ID: %d | Descrição: %s | Valor: R$ %.2f | Data: %s | Tipo: %s%n",
                    t.getId(), t.getDescricao(), t.getValor().doubleValue(), t.getData(), 
                    t.getTipo().equals("E") ? "Entrada" : "Saída");
            }

            System.out.println("\n=== ATUALIZAR TRANSAÇÃO ===");
            int id = lerInteiro("\nID da transação a ser atualizada: ");
            String descricao = lerString("Nova descrição: ");
            BigDecimal valor = BigDecimal.valueOf(lerDouble("Novo valor: "));
            LocalDate data = LocalDate.parse(lerString("Nova data (YYYY-MM-DD): "));
            
            // listar todas as categorias disponíveis
            CategoriaService categoriaService = new CategoriaService();
            List<Categoria> categorias = categoriaService.listarCategorias();
            
            if (categorias.isEmpty()) {
                System.out.println("Erro: Não existem categorias cadastradas. Por favor, cadastre algumas categorias primeiro.");
                return;
            }
            System.out.println("\nCategorias Disponíveis:");
            System.out.printf("%-5s | %-20s | %-10s%n", "ID", "Nome", "Tipo");
            System.out.println("-".repeat(40));
            for (Categoria c : categorias) {
                System.out.printf("%-5d | %-20s | %-10s%n", 
                    c.getId(), c.getNome(), c.getTipo());
            }
            System.out.println();
            
            int categoriaId;
            boolean categoriaValida;
            do {
                categoriaId = lerInteiro("ID da Categoria: ");
                categoriaValida = false;
                for (Categoria c : categorias) {
                    if (c.getId() == categoriaId) {
                        categoriaValida = true;
                        break;
                    }
                }
                if (!categoriaValida) {
                    System.out.println("Categoria inválida! Por favor, escolha um ID da lista acima.");
                }
            } while (!categoriaValida);

            // listar todas as formas de pagamento disponíveis
            FormaPagamentoService formaService = new FormaPagamentoService();
            List<FormaPagamento> formas = formaService.listarFormas();
            
            if (formas.isEmpty()) {
                System.out.println("Erro: Não existem formas de pagamento cadastradas. Por favor, cadastre algumas formas primeiro.");
                return;
            }
            System.out.println("\nFormas de Pagamento Disponíveis:");
            System.out.printf("%-5s | %-20s%n", "ID", "Formato");
            System.out.println("-".repeat(30));
            for (FormaPagamento f : formas) {
                System.out.printf("%-5d | %-20s%n", 
                    f.getId(), f.getFormato());
            }
            System.out.println();
            
            int formaId;
            boolean formaValida;
            do {
                formaId = lerInteiro("ID da Forma de Pagamento: ");
                formaValida = false;
                for (FormaPagamento f : formas) {
                    if (f.getId() == formaId) {
                        formaValida = true;
                        break;
                    }
                }
                if (!formaValida) {
                    System.out.println("Forma de pagamento inválida! Por favor, escolha um ID da lista acima.");
                }
            } while (!formaValida);
            
            String tipo;
            do {
                tipo = lerString("Novo tipo (E=Entrada, S=Saída): ").trim().toUpperCase();
                if (!tipo.equals("E") && !tipo.equals("S")) {
                    System.out.println("Tipo inválido. Use 'E' para Entrada ou 'S' para Saída.");
                }
            } while (!tipo.equals("E") && !tipo.equals("S"));

            Transacao transacao = new Transacao(valor, data, descricao, usuarioLogado.getId(), categoriaId, formaId, tipo);
            transacao.setId(id);
            
            transacaoService.atualizarTransacao(transacao);
            System.out.println("Transação atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar transação: " + e.getMessage());
        }
    }

    private static void excluirTransacao(TransacaoService transacaoService) {
        try {
            System.out.println("\n=== Excluir Transação ===");
            List<Transacao> transacoes = transacaoService.listarTransacoes(usuarioLogado.getId());
            
            if (transacoes.isEmpty()) {
                System.out.println("Não há transações cadastradas.");
                return;
            }

            System.out.println("\nTransações disponíveis:");
            for (Transacao transacao : transacoes) {
                System.out.println(transacao);
            }

            System.out.print("\nDigite o ID da transação que deseja excluir: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            transacaoService.excluirTransacao(id, usuarioLogado.getId());
            System.out.println("Transação excluída com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao excluir transação: " + e.getMessage());
        }
    }

    private static void listarTodasTransacoes(TransacaoService transacaoService) {
        try {
            System.out.println("\n=== Listar Todas as Transações ===");
            List<Transacao> transacoes = transacaoService.listarTodasTransacoes();
            
            if (transacoes.isEmpty()) {
                System.out.println("Não há transações cadastradas.");
                return;
            }

            System.out.println("\nTransações:");
            for (Transacao transacao : transacoes) {
                System.out.println(transacao);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar todas as transações: " + e.getMessage());
        }
    }

    private static void listarTransacoesPorPeriodo(TransacaoService transacaoService) {
        try {
            System.out.println("\n=== Listar Transações por Período ===");
            
            System.out.print("Digite a data inicial (YYYY-MM-DD): ");
            LocalDate dataInicio = LocalDate.parse(scanner.nextLine());
            
            System.out.print("Digite a data final (YYYY-MM-DD): ");
            LocalDate dataFim = LocalDate.parse(scanner.nextLine());

            List<Transacao> transacoes = transacaoService.listarPorPeriodo(usuarioLogado.getId(), dataInicio, dataFim);
            
            if (transacoes.isEmpty()) {
                System.out.println("Não há transações no período especificado.");
                return;
            }

            System.out.println("\nTransações no período:");
            for (Transacao transacao : transacoes) {
                System.out.println(transacao);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar transações por período: " + e.getMessage());
        }
    }

    private static void listarTransacoesPorTipo(TransacaoService transacaoService) {
        try {
            System.out.println("\n=== Listar Transações por Tipo ===");
            System.out.print("Digite o tipo (E para Entrada, S para Saída): ");
            String tipo = scanner.nextLine().toUpperCase();

            if (!tipo.equals("E") && !tipo.equals("S")) {
                System.out.println("Tipo inválido. Use E para Entrada ou S para Saída.");
                return;
            }

            List<Transacao> transacoes = transacaoService.listarPorTipo(usuarioLogado.getId(), tipo);
            
            if (transacoes.isEmpty()) {
                System.out.println("Não há transações do tipo especificado.");
                return;
            }

            System.out.println("\nTransações do tipo " + (tipo.equals("E") ? "Entrada" : "Saída") + ":");
            for (Transacao transacao : transacoes) {
                System.out.println(transacao);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar transações por tipo: " + e.getMessage());
        }
    }

    private static void menuUsuarios() {
        UsuarioService usuarioService = new UsuarioService();
        
        while (true) {
            System.out.println("\n=== Menu de Usuários ===");
            System.out.println("1. Listar Usuários");
            System.out.println("2. Cadastrar Usuário");
            System.out.println("3. Atualizar Usuário");
            System.out.println("4. Excluir Usuário");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 0:
                    return;
                case 1:
                    listarUsuarios(usuarioService);
                    break;
                case 2:
                    cadastrarUsuario(usuarioService);
                    break;
                case 3:
                    atualizarUsuario(usuarioService);
                    break;
                case 4:
                    excluirUsuario(usuarioService);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void listarUsuarios(UsuarioService usuarioService) {
        try {
            System.out.println("\n=== Lista de Usuários ===");
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            
            if (usuarios.isEmpty()) {
                System.out.println("Não há usuários cadastrados.");
                return;
            }

            for (Usuario usuario : usuarios) {
                System.out.println(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
    }

    private static void atualizarUsuario(UsuarioService usuarioService) {
        try {
            System.out.println("\n=== Atualização de Usuário ===");
            System.out.print("ID do usuário: ");
            int id = Integer.parseInt(scanner.nextLine());

            Usuario usuario = usuarioService.buscarUsuarioPorId(id);
            if (usuario == null) {
                System.out.println("Usuário não encontrado!");
                return;
            }

            System.out.print("Novo nome [" + usuario.getNome() + "]: ");
            String nome = scanner.nextLine();
            if (!nome.isEmpty()) {
                usuario.setNome(nome);
            }

            System.out.print("Novo email [" + usuario.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                usuario.setEmail(email);
            }

            System.out.print("Nova senha: ");
            String senha = scanner.nextLine();
            if (!senha.isEmpty()) {
                usuario.setSenha(senha);
            }

            System.out.print("Novo CPF [" + usuario.getCpf() + "]: ");
            String cpf = scanner.nextLine();
            if (!cpf.isEmpty()) {
                usuario.setCpf(cpf);
            }

            System.out.print("Novo papel [" + usuario.getPapel() + "]: ");
            String papel = scanner.nextLine().toUpperCase();
            if (!papel.isEmpty()) {
                usuario.setPapel(papel);
            }

            usuarioService.atualizarUsuario(usuario);
            System.out.println("Usuário atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    private static void excluirUsuario(UsuarioService usuarioService) {
        try {
            System.out.println("\n=== Exclusão de Usuário ===");
            System.out.print("ID do usuário: ");
            int id = Integer.parseInt(scanner.nextLine());

            usuarioService.excluirUsuario(id);
            System.out.println("Usuário excluído com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
        }
    }

    private static void menuCategorias() {
        CategoriaService categoriaService = new CategoriaService();
        
        while (true) {
            System.out.println("\n=== Menu de Categorias ===");
            System.out.println("1. Listar Categorias");
            System.out.println("2. Adicionar Categoria");
            System.out.println("3. Atualizar Categoria");
            System.out.println("4. Excluir Categoria");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 0:
                    return;
                case 1:
                    listarCategorias(categoriaService);
                    break;
                case 2:
                    adicionarCategoria(categoriaService);
                    break;
                case 3:
                    atualizarCategoria(categoriaService);
                    break;
                case 4:
                    excluirCategoria(categoriaService);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void listarCategorias(CategoriaService categoriaService) {
        try {
            List<Categoria> categorias = categoriaService.listarCategorias();
            if (categorias.isEmpty()) {
                System.out.println("Nenhuma categoria encontrada.");
                return;
            }

            System.out.println("\nLista de Categorias:");
            System.out.printf("%-5s | %-30s | %-10s%n", 
                "ID", "Nome", "Tipo");
            System.out.println("-".repeat(50));
            
            for (Categoria c : categorias) {
                System.out.printf("%-5d | %-30s | %-10s%n",
                    c.getId(),
                    c.getNome().length() > 30 ? c.getNome().substring(0, 27) + "..." : c.getNome(),
                    c.getTipo());
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }
    }

    private static void adicionarCategoria(CategoriaService categoriaService) {
        System.out.println("\n=== NOVA CATEGORIA ===");
        String nome = lerString("Nome: ");
        String tipo = lerString("Tipo (RECEITA/DESPESA): ");

        try {
            Categoria novaCategoria = new Categoria();
            novaCategoria.setNome(nome);
            novaCategoria.setTipo(tipo);

            categoriaService.cadastrarCategoria(novaCategoria);
            System.out.println("Categoria cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar categoria: " + e.getMessage());
        }
    }

    private static void atualizarCategoria(CategoriaService categoriaService) {
        int id = lerInteiro("ID da categoria a ser atualizada: ");
        String nome = lerString("Novo nome: ");
        String tipo = lerString("Novo tipo (RECEITA/DESPESA): ");

        try {
            Categoria categoria = new Categoria();
            categoria.setId(id);
            categoria.setNome(nome);
            categoria.setTipo(tipo);

            categoriaService.atualizarCategoria(categoria);
            System.out.println("Categoria atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar categoria: " + e.getMessage());
        }
    }

    private static void excluirCategoria(CategoriaService categoriaService) {
        int id = lerInteiro("ID da categoria a ser excluída: ");

        try {
            categoriaService.excluirCategoria(id);
            System.out.println("Categoria excluída com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao excluir categoria: " + e.getMessage());
        }
    }

    private static void menuMetasFinanceiras() {
        MetaFinanceiraService metaService = new MetaFinanceiraService(usuarioLogado.getCpf());
        
        while (true) {
            System.out.println("\n=== Menu de Metas Financeiras ===");
            System.out.println("1. Listar Metas");
            System.out.println("2. Adicionar Meta");
            System.out.println("3. Atualizar Meta");
            System.out.println("4. Excluir Meta");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 0:
                    return;
                case 1:
                    listarMetasFinanceiras(metaService);
                    break;
                case 2:
                    adicionarMetaFinanceira(metaService);
                    break;
                case 3:
                    atualizarMetaFinanceira(metaService);
                    break;
                case 4:
                    excluirMetaFinanceira(metaService);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void listarMetasFinanceiras(MetaFinanceiraService service) {
        try {
            List<MetaFinanceira> metas = service.listarMetas();
            
            if (metas.isEmpty()) {
                System.out.println("Nenhuma meta encontrada.");
                return;
            }

            System.out.println("\nSuas metas:");
            for (MetaFinanceira meta : metas) {
                System.out.println("ID: " + meta.getId());
                System.out.println("Saldo Atual: " + meta.getSaldoAtual());
                System.out.println("Meta: " + meta.getMeta());
                System.out.println("Data Prevista: " + meta.getDataPrevista());
                System.out.println("------------------------");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar metas: " + e.getMessage());
        }
    }

    private static void adicionarMetaFinanceira(MetaFinanceiraService service) {
        try {
            MetaFinanceira meta = new MetaFinanceira();
            meta.setSaldoAtual(new BigDecimal(lerString("Saldo Atual: ")));
            meta.setMeta(new BigDecimal(lerString("Meta: ")));
            meta.setDataPrevista(LocalDate.parse(lerString("Data Prevista (YYYY-MM-DD): ")));

            service.cadastrarMeta(meta);
            System.out.println("Meta cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar meta: " + e.getMessage());
        }
    }

    private static void atualizarMetaFinanceira(MetaFinanceiraService service) {
        try {
            int id = lerInteiro("ID da meta a ser atualizada: ");
            MetaFinanceira meta = new MetaFinanceira();
            meta.setId(id);
            meta.setSaldoAtual(new BigDecimal(lerString("Novo Saldo Atual: ")));
            meta.setMeta(new BigDecimal(lerString("Nova Meta: ")));
            meta.setDataPrevista(LocalDate.parse(lerString("Nova Data Prevista (YYYY-MM-DD): ")));

            service.atualizarMeta(meta);
            System.out.println("Meta atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar meta: " + e.getMessage());
        }
    }

    private static void excluirMetaFinanceira(MetaFinanceiraService service) {
        try {
            int id = lerInteiro("ID da meta a ser excluída: ");
            service.deletarMeta(id);
            System.out.println("Meta excluída com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao excluir meta: " + e.getMessage());
        }
    }

    private static void menuFormasPagamento() {
        FormaPagamentoService formaPagamentoService = new FormaPagamentoService();
        
        while (true) {
            System.out.println("\n=== Menu de Formas de Pagamento ===");
            System.out.println("1. Listar Formas de Pagamento");
            System.out.println("2. Adicionar Forma de Pagamento");
            System.out.println("3. Atualizar Forma de Pagamento");
            System.out.println("4. Excluir Forma de Pagamento");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 0:
                    return;
                case 1:
                    listarFormasPagamento(formaPagamentoService);
                    break;
                case 2:
                    adicionarFormaPagamento(formaPagamentoService);
                    break;
                case 3:
                    atualizarFormaPagamento(formaPagamentoService);
                    break;
                case 4:
                    excluirFormaPagamento(formaPagamentoService);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void listarFormasPagamento(FormaPagamentoService service) {
        try {
            List<FormaPagamento> formas = service.listarFormas();
            
            if (formas.isEmpty()) {
                System.out.println("Nenhuma forma de pagamento cadastrada.");
                return;
            }

            System.out.println("\nFormas de pagamento cadastradas:");
            for (FormaPagamento forma : formas) {
                System.out.println("ID: " + forma.getId());
                System.out.println("Formato: " + forma.getFormato());
                System.out.println("------------------------");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar formas de pagamento: " + e.getMessage());
        }
    }

    private static void adicionarFormaPagamento(FormaPagamentoService service) {
        try {
            FormaPagamento forma = new FormaPagamento();
            forma.setFormato(lerString("Formato de Pagamento: "));

            service.cadastrarForma(forma);
            System.out.println("Forma de pagamento cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar forma de pagamento: " + e.getMessage());
        }
    }

    private static void atualizarFormaPagamento(FormaPagamentoService service) {
        try {
            FormaPagamento forma = new FormaPagamento();
            forma.setId(lerInteiro("ID da forma a ser atualizada: "));
            forma.setFormato(lerString("Novo Formato: "));

            service.atualizarForma(forma);
            System.out.println("Forma de pagamento atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar forma de pagamento: " + e.getMessage());
        }
    }

    private static void excluirFormaPagamento(FormaPagamentoService service) {
        try {
            int id = lerInteiro("ID da forma a ser excluída: ");
            service.deletarForma(id);
            System.out.println("Forma de pagamento excluída com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao excluir forma de pagamento: " + e.getMessage());
        }
    }

    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }

    private static void menuSistema() {
        while (true) {
            System.out.println("\n=== Menu do Sistema ===");
            System.out.println("1. Usuários");
            System.out.println("2. Categorias");
            System.out.println("3. Formas de Pagamento");
            System.out.println("4. Transações");
            System.out.println("5. Metas Financeiras");
            System.out.println("0. Logout");
            System.out.print("Opção: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 0:
                    usuarioLogado = null;
                    return;
                case 1:
                    if (usuarioLogado.getPapel().equals("ADMIN")) {
                        menuUsuarios();
                    } else {
                        System.out.println("Acesso negado! Apenas administradores podem acessar este menu.");
                    }
                    break;
                case 2:
                    menuCategorias();
                    break;
                case 3:
                    menuFormasPagamento();
                    break;
                case 4:
                    menuTransacoes();
                    break;
                case 5:
                    menuMetasFinanceiras();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
