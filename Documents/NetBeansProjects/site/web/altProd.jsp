<%@page import="java.util.List"%>
<%@page import="model.Produto"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="configg.ProdutoDao" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<html>
    <head>
       
    </head>
    <body>
        <%
            
            String nome = request.getParameter("nomeProduto");
            String descricao = request.getParameter("descricaoProduto");
            String marca = request.getParameter("marcaProduto");
            double preco = 0.0;
            try {
                preco = Double.parseDouble(request.getParameter("precoProduto"));
            } catch (NumberFormatException e) {
                out.println("<script>alert('Preço inválido. Por favor, insira um valor numérico.');</script>");
                return;
            }

            ProdutoDao produtoDAO = new ProdutoDao();          
                produtoDAO.alterarProduto(nome, descricao, null, preco);
                    out.println("<script>alert('Produto alterado com sucesso!');</script>");
                
            
        %>
        
    </body>
</html>
