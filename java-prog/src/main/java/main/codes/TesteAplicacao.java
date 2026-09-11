package main.codes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import database.DatabaseConfig;
import missao.IMissaoDAO;
import missao.IUsuarioMissaoDAO;
import missao.Missao;
import missao.MissaoDAO;
import missao.UsuarioMissaoDAO;
import usuario.Cadastro;
import usuario.IUsuarioDAO;
import usuario.UsuarioDAO;

/**
 * Classe de Teste exigida pela Sprint 3: um main() separado do Main.java
 * (que é a aplicação interativa), usado só para instanciar objetos e
 * simular a utilização das principais funcionalidades já implementadas,
 * sem depender de Scanner/entrada do usuário.
 *
 * Repare que as DAOs são declaradas pelo tipo da interface (IUsuarioDAO,
 * IMissaoDAO, IUsuarioMissaoDAO) e não pelo tipo concreto — é o principal
 * ganho prático de ter criado as interfaces: este teste depende só do
 * "contrato", não de como cada DAO resolve por dentro.
 */
public class TesteAplicacao {

    public static void main(String[] args) {

        String url = DatabaseConfig.get("db.url");
        String usuario = DatabaseConfig.get("db.user");
        String senha = DatabaseConfig.get("db.password");

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {

            System.out.println("===== TESTE DE APLICAÇÃO — SoulUp =====");
            System.out.println("Conexão com o banco realizada com sucesso!\n");

            IUsuarioDAO usuarioDAO = new UsuarioDAO(conn);
            IMissaoDAO missaoDAO = new MissaoDAO(conn);
            IUsuarioMissaoDAO usuarioMissaoDAO = new UsuarioMissaoDAO(conn);

            // --- TESTE 1: criar um usuário de teste ---
            System.out.println("--- Teste 1: criar usuário ---");
            String sufixo = String.valueOf(System.currentTimeMillis()).substring(8);
            Cadastro usuarioTeste = new Cadastro(
                    "teste_" + sufixo,
                    "Usuário de Teste",
                    "teste" + sufixo + "@soulup.com",
                    "senhaTeste123"
            );
            boolean criado = usuarioDAO.criarUsuario(usuarioTeste);
            System.out.println("Usuário criado? " + criado);
            System.out.println(usuarioTeste);

            // --- TESTE 2: verificar duplicidade (deve retornar true, pois acabou de ser criado) ---
            System.out.println("\n--- Teste 2: checar duplicidade de e-mail ---");
            boolean existe = usuarioDAO.existeUsuarioRegistro("USUARIO", "EMAIL", usuarioTeste.getEmail());
            System.out.println("E-mail já cadastrado (esperado: true)? " + existe);

            // --- TESTE 3: login com o usuário recém-criado ---
            System.out.println("\n--- Teste 3: login ---");
            boolean logou = usuarioDAO.logarUsuario(
                    usuarioTeste.getEmail().toLowerCase(),
                    usuarioTeste.getSenha().toLowerCase()
            );
            System.out.println("Login realizado (esperado: true)? " + logou);

            // --- TESTE 4: listar missões disponíveis ---
            System.out.println("\n--- Teste 4: listar missões ativas ---");
            List<Missao> missoes = missaoDAO.listarMissoes();
            System.out.println("Quantidade de missões ativas encontradas: " + missoes.size());
            if (!missoes.isEmpty()) {
                Missao primeira = missoes.get(0);
                System.out.println("Exemplo: " + primeira);

                // --- TESTE 5: iniciar e concluir uma missão para o usuário de teste ---
                System.out.println("\n--- Teste 5: iniciar e concluir missão ---");
                boolean iniciada = usuarioMissaoDAO.iniciarMissao(usuarioTeste.getId(), primeira.getIdMissao());
                System.out.println("Missão iniciada? " + iniciada);

                boolean concluida = usuarioMissaoDAO.concluirMissao(usuarioTeste.getId(), primeira.getIdMissao());
                System.out.println("Missão concluída? " + concluida);
            }

            // --- TESTE 6: alterar dados do usuário de teste ---
            System.out.println("\n--- Teste 6: alterar dados do usuário ---");
            usuarioTeste.setNomeCompleto("Usuário de Teste Atualizado");
            boolean alterado = usuarioDAO.alterarDadosUsuario(usuarioTeste);
            System.out.println("Dados alterados? " + alterado);

            // --- TESTE 7: remover o usuário de teste (limpeza) ---
            System.out.println("\n--- Teste 7: remover usuário (limpeza dos dados de teste) ---");
            boolean removido = usuarioDAO.removerPerfil(usuarioTeste.getId(), usuarioTeste.getSenha());
            System.out.println("Usuário removido? " + removido);

            System.out.println("\n===== FIM DOS TESTES =====");

        } catch (SQLException e) {
            System.out.println("Erro ao conectar/operar no banco durante o teste: " + e.getMessage());
        }
    }
}
