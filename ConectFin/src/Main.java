import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import model.Categoria;
import model.Transacao;
import model.Usuario;
import service.CategoriaService;
import service.TransacaoService;
import service.UsuarioService;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static int usuarioLogadoId = -1;
    private static String papelUsuarioLogado = null;

    public static void limparTela() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao limpar a tela: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        boolean executando = true;

        while (executando) {
            if (usuarioLogadoId == -1) {
                executando = exibirMenuAutenticacao();
            } else {
                executando = exibirMenuPrincipal();
            }
        }
        scanner.close();
    }

    private static boolean exibirMenuAutenticacao() {
        System.out.println("\n=== AUTENTICAÇÃO ===");
        System.out.println("1 - Fazer Login");
        System.out.println("2 - Cadastrar Novo Usuário");
        System.out.println("3 - Sair");
        System.out.println("==========================");

        int opcao = lerInteiro("Escolha uma opção: ");

        switch (opcao) {
            case 1:
                fazerLogin();
                break;
            case 2:
                cadastrarNovoUsuario();
                break;
            case 3:
                return false;
            default:
                System.out.println("Opção inválida!");
        }

        return true;
    }

    private static void fazerLogin() {
        System.out.println("\n=== LOGIN ===");
        String identificador = lerString("E-mail ou CPF: ");
        String senha = lerString("Senha: ");

        try {
            UsuarioService usuarioService = new UsuarioService();
            Usuario usuario = usuarioService.login(identificador, senha);
            usuarioLogadoId = usuario.getId();
            papelUsuarioLogado = usuario.getPapel();
            System.out.println("Login realizado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void cadastrarNovoUsuario() {
        System.out.println("\n=== CADASTRO DE USUÁRIO ===");
        String nome = lerString("Nome: ");
        String email = lerString("Email: ");
        String senha = lerString("Senha: ");
        String papel = lerString("Papel (ADMIN/USUARIO): ");
        String cpf = lerString("CPF (11 dígitos): ");

        try {
            UsuarioService usuarioService = new UsuarioService();
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setSenha(senha);
            novoUsuario.setPapel(papel);
            novoUsuario.setCpf(cpf);

            usuarioService.cadastrarUsuario(novoUsuario);
            System.out.println("Usuário cadastrado com sucesso! Por favor, faça login.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean exibirMenuPrincipal() {
        System.out.println("\n==== MENU PRINCIPAL =====");
        System.out.println("1 - Transações");
        if (papelUsuarioLogado.equalsIgnoreCase("ADMIN")) {
            System.out.println("2 - Usuários");
        }
        System.out.println("3 - Categorias");
        System.out.println("4 - Sair");
        System.out.println("5 - Logout");
        System.out.println("=========================");

        int opcao = lerInteiro("Escolha uma opção: ");
        limparTela();
        switch (opcao) {
            case 1:
                menuTransacoes();
                break;
            case 2:
                if (papelUsuarioLogado.equalsIgnoreCase("ADMIN")) {
                    menuUsuarios();
                } else {
                    System.out.println("Acesso negado: apenas administradores podem acessar o menu de usuários.");
                }
                break;
            case 3:
                menuCategorias();
                break;
            case 4:
                return false;
            case 5:
                fazerLogout();
                break;
            default:
                System.out.println("Opção inválida!");
        }

        return true;
    }

    private static void fazerLogout() {
        usuarioLogadoId = -1;
        papelUsuarioLogado = null;
        System.out.println("Logout realizado com sucesso!");
    }

    private static void menuTransacoes() {
        System.out.println("\n=== MENU DE TRANSAÇÕES ===");
        System.out.println("1 - Adicionar Transação");
        System.out.println("2 - Atualizar Transação");
        System.out.println("3 - Excluir Transação");
        System.out.println("4 - Gerar Exemplos");
        System.out.println("5 - Listar Transações");
        System.out.println("6 - Listar transações por período");
        System.out.println("7 - Listar por tipo (Entrada/Saída)");
        if (papelUsuarioLogado.equalsIgnoreCase("ADMIN")) {
            System.out.println("8 - Listar Todas as Transações");
        }
        System.out.println("9 - Voltar");
        System.out.println("=========================");

        int opcao = lerInteiro("Escolha uma opção: ");

        TransacaoService transacaoService = new TransacaoService();

        switch (opcao) {
            case 1:
                adicionarTransacao(transacaoService);
                break;
            case 2:
                atualizarTransacao(transacaoService);
                break;
            case 3:
                excluirTransacao(transacaoService);
                break;
            case 4:
                gerarTransacoesExemplo(transacaoService);
                break;
            case 5:
                listarTransacoes(transacaoService);
                break;
            case 6:
                listarTransacoesPorPeriodo(transacaoService);
                break;
            case 7:
                listarTransacoesPorTipo(transacaoService);
                break;
            case 8:
                if (papelUsuarioLogado.equalsIgnoreCase("ADMIN")) {
                    listarTodasTransacoes(transacaoService);
                } else {
                    System.out.println("Acesso negado: apenas administradores podem listar todas as transações.");
                }
                break;
            case 9:
                return;
            default:
                System.out.println("Opção inválida!");
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

            // Array de transações de exemplo
            Transacao[] transacoesExemplo = {
                new Transacao(BigDecimal.valueOf(1500.00), LocalDate.parse("2024-03-01"), "Salário", usuarioLogadoId, categorias.get(0).getId(), "E"),
                new Transacao(BigDecimal.valueOf(350.00), LocalDate.parse("2024-03-02"), "Freelance", usuarioLogadoId, categorias.get(0).getId(), "E"),
                new Transacao(BigDecimal.valueOf(1200.00), LocalDate.parse("2024-03-03"), "Aluguel", usuarioLogadoId, categorias.get(1).getId(), "S"),
                new Transacao(BigDecimal.valueOf(450.00), LocalDate.parse("2024-03-04"), "Supermercado", usuarioLogadoId, categorias.get(1).getId(), "S"),
                new Transacao(BigDecimal.valueOf(200.00), LocalDate.parse("2024-03-05"), "Conta de Luz", usuarioLogadoId, categorias.get(1).getId(), "S"),
                new Transacao(BigDecimal.valueOf(150.00), LocalDate.parse("2024-03-06"), "Internet", usuarioLogadoId, categorias.get(1).getId(), "S"),
                new Transacao(BigDecimal.valueOf(300.00), LocalDate.parse("2024-03-07"), "Bônus", usuarioLogadoId, categorias.get(0).getId(), "E"),
                new Transacao(BigDecimal.valueOf(180.00), LocalDate.parse("2024-03-08"), "Combustível", usuarioLogadoId, categorias.get(1).getId(), "S"),
                new Transacao(BigDecimal.valueOf(250.00), LocalDate.parse("2024-03-09"), "Lazer", usuarioLogadoId, categorias.get(1).getId(), "S"),
                new Transacao(BigDecimal.valueOf(400.00), LocalDate.parse("2024-03-10"), "Investimento", usuarioLogadoId, categorias.get(0).getId(), "E")
            };

            // Cadastrar cada transação
            for (Transacao transacao : transacoesExemplo) {
                transacaoService.cadastrarTransacao(transacao);
            }

            System.out.println("10 transações de exemplo foram geradas com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao gerar transações de exemplo: " + e.getMessage());
        }
    }

    private static void listarTransacoes(TransacaoService transacaoService) {
        try {
            List<Transacao> transacoes = transacaoService.listarTransacoes(usuarioLogadoId);
            if (transacoes.isEmpty()) {
                System.out.println("Nenhuma transação encontrada.");
                return;
            }

            System.out.println("\nLista de Transações:");
            System.out.printf("%-5s | %-30s | %-13s | %-12s | %-8s%n", 
                "ID", "Descrição", "Valor", "Data", "Tipo");
            System.out.println("-".repeat(75));
            
            for (Transacao t : transacoes) {
                System.out.printf("%-5d | %-30s | R$ %-10.2f | %-12s | %-8s%n",
                    t.getId(),
                    t.getDescricao().length() > 30 ? t.getDescricao().substring(0, 27) + "..." : t.getDescricao(),
                    t.getValor().doubleValue(),
                    t.getData(),
                    t.getTipo().equals("E") ? "Entrada" : "Saída");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar transações: " + e.getMessage());
        }
    }

    private static void adicionarTransacao(TransacaoService transacaoService) {
        try {
            System.out.println("\n=== NOVA TRANSAÇÃO ===");
            
            // Agora solicitar os dados da transação
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
            
            String tipo;
            do {
                tipo = lerString("Tipo (E=Entrada, S=Saída): ").trim().toUpperCase();
                if (!tipo.equals("E") && !tipo.equals("S")) {
                    System.out.println("Tipo inválido. Use 'E' para Entrada ou 'S' para Saída.");
                }
            } while (!tipo.equals("E") && !tipo.equals("S"));

            Transacao novaTransacao = new Transacao(valor, data, descricao, usuarioLogadoId, categoriaId, tipo);
            transacaoService.cadastrarTransacao(novaTransacao);
            System.out.println("Transação cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar transação: " + e.getMessage());
        }
    }

    private static void atualizarTransacao(TransacaoService transacaoService) {
        try {
            // Listar todas as transações primeiro
            List<Transacao> transacoes = transacaoService.listarTransacoes(usuarioLogadoId);
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
            int categoriaId = lerInteiro("Novo ID da categoria: ");
            
            String tipo;
            do {
                tipo = lerString("Novo tipo (E=Entrada, S=Saída): ").trim().toUpperCase();
                if (!tipo.equals("E") && !tipo.equals("S")) {
                    System.out.println("Tipo inválido. Use 'E' para Entrada ou 'S' para Saída.");
                }
            } while (!tipo.equals("E") && !tipo.equals("S"));

            Transacao transacao = new Transacao(valor, data, descricao, usuarioLogadoId, categoriaId, tipo);
            transacao.setId(id);
            
            transacaoService.atualizarTransacao(transacao);
            System.out.println("Transação atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar transação: " + e.getMessage());
        }
    }

    private static void excluirTransacao(TransacaoService transacaoService) {
        try {
            // Listar todas as transações primeiro
            List<Transacao> transacoes = transacaoService.listarTransacoes(usuarioLogadoId);
            if (transacoes.isEmpty()) {
                System.out.println("Nenhuma transação encontrada para excluir.");
                return;
            }

            System.out.println("\nLista de Transações Disponíveis:");
            for (Transacao t : transacoes) {
                System.out.printf("ID: %d | Descrição: %s | Valor: R$ %.2f | Data: %s | Tipo: %s%n",
                    t.getId(), t.getDescricao(), t.getValor().doubleValue(), t.getData(), 
                    t.getTipo().equals("E") ? "Entrada" : "Saída");
            }
            System.out.println("\n=== EXCLUIR TRANSAÇÃO ===");
            int id = lerInteiro("\nID da transação a ser excluída: ");

            transacaoService.excluirTransacao(id, usuarioLogadoId);
            System.out.println("Transação excluída com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao excluir transação: " + e.getMessage());
        }
    }

    private static void listarTodasTransacoes(TransacaoService transacaoService) {
        try {
            List<Transacao> transacoes = transacaoService.listarTodasTransacoes(papelUsuarioLogado);
            if (transacoes.isEmpty()) {
                System.out.println("Nenhuma transação encontrada.");
                return;
            }

            System.out.println("\nLista de Todas as Transações:");
            System.out.printf("%-5s | %-20s | %-30s | %-13s | %-12s | %-8s%n", 
                "ID", "Email do Usuário", "Descrição", "Valor", "Data", "Tipo");
            System.out.println("-".repeat(95));
            
            UsuarioService usuarioService = new UsuarioService();
            for (Transacao t : transacoes) {
                try {
                    // Buscar o email do usuário
                    Usuario usuario = usuarioService.buscarUsuarioPorId(t.getUsuarioId());
                    String emailUsuario = usuario != null ? usuario.getEmail() : "Usuário não encontrado";
                    
                    System.out.printf("%-5d | %-20s | %-30s | R$ %-10.2f | %-12s | %-8s%n",
                        t.getId(), 
                        emailUsuario,
                        t.getDescricao().length() > 30 ? t.getDescricao().substring(0, 12) + "..." : t.getDescricao(),
                        t.getValor().doubleValue(),
                        t.getData(),
                        t.getTipo().equals("E") ? "Entrada" : "Saída");
                } catch (SQLException e) {
                    System.out.printf("%-5d | %-20s | %-30s | R$ %-10.2f | %-12s | %-8s%n",
                        t.getId(), 
                        "Erro ao buscar usuário",
                        t.getDescricao().length() > 30 ? t.getDescricao().substring(0, 12) + "..." : t.getDescricao(),
                        t.getValor().doubleValue(),
                        t.getData(),
                        t.getTipo().equals("E") ? "Entrada" : "Saída");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar todas as transações: " + e.getMessage());
        }
    }

    private static void listarTransacoesPorPeriodo(TransacaoService transacaoService) {
        try {
            LocalDate dataInicial, dataFinal;
            
            // Solicitar e validar data inicial
            while (true) {
                try {
                    String dataInicialStr = lerString("Data inicial (yyyy-MM-dd): ");
                    dataInicial = LocalDate.parse(dataInicialStr);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use o formato yyyy-MM-dd (ex: 2024-03-20)");
                }
            }
            
            // Solicitar e validar data final
            while (true) {
                try {
                    String dataFinalStr = lerString("Data final (yyyy-MM-dd): ");
                    dataFinal = LocalDate.parse(dataFinalStr);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use o formato yyyy-MM-dd (ex: 2024-03-20)");
                }
            }

            // Se for admin, perguntar se quer ver todas ou apenas as próprias
            Integer usuarioId = usuarioLogadoId;
            if (papelUsuarioLogado.equalsIgnoreCase("ADMIN")) {
                System.out.println("\n1 - Todas as transações");
                System.out.println("2 - Minhas transações");
                String opcao = lerString("Escolha uma opção: ").toUpperCase();
                
                if (opcao.equals("1")) {
                    usuarioId = null;
                }
            }

            // Listar transações
            List<Transacao> transacoes = transacaoService.listarPorPeriodo(dataInicial, dataFinal, usuarioId, papelUsuarioLogado);
            
            if (transacoes.isEmpty()) {
                System.out.println("Nenhuma transação encontrada para o intervalo informado.");
                return;
            }

            System.out.println("\nLista de Transações no Período:");
            System.out.printf("%-5s | %-30s | %-13s | %-12s | %-8s%n", 
                "ID", "Descrição", "Valor", "Data", "Tipo");
            System.out.println("-".repeat(75));
            
            for (Transacao t : transacoes) {
                System.out.printf("%-5d | %-30s | R$ %-10.2f | %-12s | %-8s%n",
                    t.getId(),
                    t.getDescricao().length() > 30 ? t.getDescricao().substring(0, 27) + "..." : t.getDescricao(),
                    t.getValor().doubleValue(),
                    t.getData(),
                    t.getTipo().equals("E") ? "Entrada" : "Saída");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao listar transações: " + e.getMessage());
        }
    }

    private static void listarTransacoesPorTipo(TransacaoService transacaoService) {
        try {
            System.out.println("\n=== LISTAR POR TIPO ===");
            System.out.println("1 - Entradas");
            System.out.println("2 - Saídas");
            int opcao = lerInteiro("Escolha uma opção: ");

            String tipo;
            if (opcao == 1) {
                tipo = "E";
            } else if (opcao == 2) {
                tipo = "S";
            } else {
                System.out.println("Opção inválida!");
                return;
            }

            // Se for admin, perguntar se quer ver todas ou apenas as próprias
            Integer usuarioId = usuarioLogadoId;
            if (papelUsuarioLogado.equalsIgnoreCase("ADMIN")) {
                System.out.println("\n1 - Todas as transações");
                System.out.println("2 - Minhas transações");
                int opcaoAdmin = lerInteiro("Escolha uma opção: ");
                
                if (opcaoAdmin == 1) {
                    usuarioId = null;
                }
            }

            List<Transacao> transacoes = transacaoService.listarPorTipo(tipo, usuarioId, papelUsuarioLogado);
            
            if (transacoes.isEmpty()) {
                System.out.println("Nenhuma transação do tipo " + (tipo.equals("E") ? "Entrada" : "Saída") + " encontrada.");
                return;
            }

            System.out.println("\nLista de Transações - " + (tipo.equals("E") ? "Entradas" : "Saídas") + ":");
            System.out.printf("%-5s | %-30s | %-13s | %-12s | %-8s%n", 
                "ID", "Descrição", "Valor", "Data", "Tipo");
            System.out.println("-".repeat(75));
            
            for (Transacao t : transacoes) {
                System.out.printf("%-5d | %-30s | R$ %-10.2f | %-12s | %-8s%n",
                    t.getId(),
                    t.getDescricao().length() > 30 ? t.getDescricao().substring(0, 27) + "..." : t.getDescricao(),
                    t.getValor().doubleValue(),
                    t.getData(),
                    t.getTipo().equals("E") ? "Entrada" : "Saída");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar transações: " + e.getMessage());
        }
    }

    private static void menuUsuarios() {
        System.out.println("\n=== MENU DE USUÁRIOS ===");
        System.out.println("1 - Listar Usuários");
        System.out.println("2 - Adicionar Usuário");
        System.out.println("3 - Atualizar Usuário");
        System.out.println("4 - Excluir Usuário");
        System.out.println("5 - Voltar");
        System.out.println("========================");

        int opcao = lerInteiro("Escolha uma opção: ");

        UsuarioService usuarioService = new UsuarioService();

        switch (opcao) {
            case 1:
                listarUsuarios(usuarioService);
                break;
            case 2:
                adicionarUsuario(usuarioService);
                break;
            case 3:
                atualizarUsuario(usuarioService);
                break;
            case 4:
                excluirUsuario(usuarioService);
                break;
            case 5:
                return;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private static void listarUsuarios(UsuarioService usuarioService) {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário encontrado.");
                return;
            }

            System.out.println("\nLista de Usuários:");
            System.out.printf("%-5s | %-30s | %-30s | %-10s | %-15s%n", 
                "ID", "Nome", "Email", "Papel", "CPF");
            System.out.println("-".repeat(95));
            
            for (Usuario u : usuarios) {
                System.out.printf("%-5d | %-30s | %-30s | %-10s | %-15s%n",
                    u.getId(),
                    u.getNome().length() > 30 ? u.getNome().substring(0, 27) + "..." : u.getNome(),
                    u.getEmail().length() > 30 ? u.getEmail().substring(0, 27) + "..." : u.getEmail(),
                    u.getPapel(),
                    u.getCpf());
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
    }

    private static void adicionarUsuario(UsuarioService usuarioService) {
        System.out.println("\n=== NOVO USUÁRIO ===");
        String nome = lerString("Nome: ");
        String email = lerString("Email: ");
        String senha = lerString("Senha: ");
        String papel = lerString("Papel (ADMIN/USUARIO): ");

        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setSenha(senha);
            novoUsuario.setPapel(papel);

            usuarioService.cadastrarUsuario(novoUsuario);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    private static void atualizarUsuario(UsuarioService usuarioService) {
        int id = lerInteiro("ID do usuário a ser atualizado: ");
        String nome = lerString("Novo nome: ");
        String email = lerString("Novo email: ");
        String senha = lerString("Nova senha: ");
        String papel = lerString("Novo papel (ADMIN/USUARIO): ");

        try {
            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuario.setPapel(papel);

            usuarioService.atualizarUsuario(usuario);
            System.out.println("Usuário atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    private static void excluirUsuario(UsuarioService usuarioService) {
        int id = lerInteiro("ID do usuário a ser excluído: ");

        try {
            usuarioService.excluirUsuario(id);
            System.out.println("Usuário excluído com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
        }
    }

    private static void menuCategorias() {
        System.out.println("\n=== MENU DE CATEGORIAS ===");
        System.out.println("1 - Listar Categorias");
        System.out.println("2 - Adicionar Categoria");
        System.out.println("3 - Atualizar Categoria");
        System.out.println("4 - Excluir Categoria");
        System.out.println("5 - Voltar");
        System.out.println("=========================");

        int opcao = lerInteiro("Escolha uma opção: ");

        CategoriaService categoriaService = new CategoriaService();

        switch (opcao) {
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
            case 5:
                return;
            default:
                System.out.println("Opção inválida!");
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
}
