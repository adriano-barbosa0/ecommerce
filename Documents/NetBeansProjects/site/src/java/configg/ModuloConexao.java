package configg;


import java.sql.*;

/**
 * Conexão com o Banco de Dados
 * @author Adriano Barbosa
 * @version 1.1
 */
public class ModuloConexao {

    /**
     * Método Responsável pela conexão com o banco de dados
     *
     * @return conexao
     */
    public static Connection conector() {
        java.sql.Connection conexao = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/ecommerce?characterEncoding=utf-8";
        String user = "dba";
        String password = "Adriano.24127255";
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }

}
