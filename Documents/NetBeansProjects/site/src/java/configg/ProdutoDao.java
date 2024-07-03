/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configg;

import dto.ProdutoDto;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Produto;

/**
 *
 * @author Nova
 */
public class ProdutoDao {

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public boolean validacao(String nome, String descricao, String marca, Double preco) {
        return nome != null && !nome.isEmpty()
                || descricao != null && !descricao.isEmpty()
                || marca != null && !marca.isEmpty()
                || preco > 0;
    }

    public boolean adicionarProduto(String nome, String descricao, String marca, double preco) {

        try {
            con = ModuloConexao.conector();
            String sql = "INSERT INTO produtos (nomeProduto, descricaoProduto, marcaProduto, precoProduto) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setString(2, descricao);
            pst.setString(3, marca);
            pst.setDouble(4, preco);
            int adicionado = pst.executeUpdate();
            return adicionado > 0;
        } catch (SQLException e) {
            out.print(e.getMessage());
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public List<Produto> buscar() {
        List<Produto> produtos = new ArrayList<>();
        con = ModuloConexao.conector();
        String sql = "SELECT * FROM produtos";
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("idProduto"));
                produto.setNome(rs.getString("nomeProduto"));
                produto.setDescricao(rs.getString("descricaoProduto"));
                produto.setMarca(rs.getString("marcaProduto"));
                produto.setPreco(rs.getDouble("precoProduto"));
                produtos.add(produto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return produtos;
    }

    public void alterarProduto(String nome, String descricao, String marca, double preco) {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja alterar este Produto?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                con = ModuloConexao.conector();
                String sql = "UPDATE produtos SET nomeProduto=?, descricaoProduto=?, marcaProduto=?, precoProduto=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, nome);
                pst.setString(2, descricao);
                pst.setString(3, marca);
                pst.setDouble(4, preco);
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto Alterado com sucesso!");
                }
            } catch (SQLException e) {
                out.print(e.getMessage());
            } finally {
                try {
                    if (pst != null) {
                        pst.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                }
            }
        }
    }

    public ProdutoDto buscarProdutoPorId(int id) {
        ProdutoDto produto = null;
        try {
            con = ModuloConexao.conector();
            String sql = "SELECT * FROM produtos WHERE idProduto=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                produto = new ProdutoDto();
                produto.setId(rs.getInt("idProduto"));
                produto.setNome(rs.getString("nomeProduto"));
                produto.setDescricao(rs.getString("descricaoProduto"));
                produto.setMarca(rs.getString("marcaProduto"));
                produto.setPreco(rs.getDouble("precoProduto"));
            }
        } catch (SQLException e) {
            out.print(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
        return produto;
    }

}
