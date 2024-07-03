<%-- 
    Document   : listarProd
    Created on : 01/07/2024, 18:37:55
    Author     : Nova
--%>

<%@page import="configg.ProdutoDao"%>
<%@page import="java.util.List"%>
<%@page import="model.Produto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <style>
        /* Estilos para a tabela */
        table {
            width: 100%;
            border-collapse: collapse; /* Remove espaçamentos entre células */
            margin-bottom: 20px; /* Espaçamento abaixo da tabela */
            font-family: Arial, sans-serif;
            color: #333;
        }

        /* Estilos para cabeçalho da tabela */
        th {
            background-color: #f2f2f2; /* Cor de fundo do cabeçalho */
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd; /* Borda das células */
        }

        /* Estilos para células da tabela */
        td {
            padding: 10px;
            border: 1px solid #ddd; /* Borda das células */
        }

        /* Estilos alternados para linhas */
        tr:nth-child(even) {
            background-color: #f9f9f9; /* Cor de fundo das linhas pares */
        }
    </style>
    </head>
    <body>
        <%
            ProdutoDao produtoDao = new ProdutoDao();
            List<Produto> produtos = produtoDao.buscar();
        %>
        <table border="1">
            <thead>
                <tr>
                    <th>Código do Produto</th>
                    <th>Nome</th>
                    <th>Descrição</th>
                    <th>Marca</th>
                    <th>Preço</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Produto produto : produtos) {
                %>
                <tr>
                    <td><%= produto.getId()%></td>
                    <td><%= produto.getNome()%></td>
                    <td><%= produto.getDescricao()%></td>
                    <td><%= produto.getMarca()%></td>
                    <td><%= produto.getPreco()%></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

    </body>
</html>
