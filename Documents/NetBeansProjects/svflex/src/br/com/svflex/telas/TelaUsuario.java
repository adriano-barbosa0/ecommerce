/*
 * The MIT License
 *
 * Copyright 2024 Adriano Barbosa.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.svflex.telas;

import java.sql.*;
import br.com.svflex.dal.ModuloConexao;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/**
 * Manipulação de dados de usuário
 * @author Adriano Barbosa
 * @version 1.1
 */
public class TelaUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    
    public TelaUsuario() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
/**
 * Método para limpar os dados
 */    
    private void LimparDados (){

        
            txtUserName.setText(null);
                txtUserTel.setText(null);
                txtUserLogin.setText(null);
                txtUserSenha.setText(null);     
    }
/**Método responsável pela validação dos dados
 * 
 * @return validacao
 */    
    private boolean validacao(){
        return !(txtUserId.getText().isEmpty() || 
                txtUserName.getText().isEmpty() ||
                txtUserLogin.getText().isEmpty() ||
                txtUserSenha.getText().isEmpty());
    }
        
/**
 * Método para pesquisar usuário
 */    
    private void buscar(){
        String sql = "select * from tbusuarios where id_user = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserId.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtUserName.setText(rs.getString (2));
                txtUserTel.setText(rs.getString (3));
                txtUserLogin.setText(rs.getString (4));
                txtUserSenha.setText(rs.getString (5));
                cboUserPerfil.setSelectedItem(rs.getString(6));
            } else {
                JOptionPane.showMessageDialog(null, "Usuário não cadastrado");
               LimparDados();
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao buscar usuário. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
/**
 * Método para adicionar usuário
 */ 
    private void adicionar(){
        String sql = "insert into tbusuarios (id_user, usuario, telefone, login, senha, perfil)values(?, ?, ?, ?, ?, ?);";
        try {
            //conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserId.getText());
            pst.setString(2, txtUserName.getText());
            pst.setString(3, txtUserTel.getText());
            pst.setString(4, txtUserLogin.getText());
            pst.setString(5, txtUserSenha.getText());
            pst.setString(6, cboUserPerfil.getSelectedItem().toString());
            if(validacao()){
            
            int adicionado = pst.executeUpdate();
            if(adicionado > 0){
                JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!");
                LimparDados();
            }
            } else{
                 JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao adicionar usuário. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
    }
/**
 * Método para alterar usuário
 */    
    private void atualizar(){
        String sql = "update tbusuarios set usuario = ?, telefone = ?, login = ?, senha = ?, perfil = ? where id_user = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserName.getText());
            pst.setString(2, txtUserTel.getText());
            pst.setString(3, txtUserLogin.getText());
            pst.setString(4, txtUserSenha.getText());
            pst.setString(5, cboUserPerfil.getSelectedItem().toString());
            pst.setString(6, txtUserId.getText());
            
            if(validacao()){
                
               int adicionado = pst.executeUpdate();
            if(adicionado > 0){
                JOptionPane.showMessageDialog(null, "Dados do Usuário Atualizados com sucesso!");
                LimparDados();
            }
            } else{
                 JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao atualizar usuário. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
/**
 * Método para excluir usuário
 */    
    private void excluir(){
        int confirma = JOptionPane
                .showConfirmDialog(null, "Tem certeza que deseja remover este usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            String sql = "delete from tbusuarios where id_user = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUserId.getText());
               int apagado = pst.executeUpdate();
               if(apagado > 0){ 
                    JOptionPane.showMessageDialog(null, "Usuário excluído com sucesso!");
                    LimparDados();
            }} catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Ocorreu um erro ao excluir usuário. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtUserId = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtUserTel = new javax.swing.JTextField();
        txtUserLogin = new javax.swing.JTextField();
        txtUserSenha = new javax.swing.JTextField();
        cboUserPerfil = new javax.swing.JComboBox<>();
        btnUserCreate = new javax.swing.JButton();
        btnUserBuscar = new javax.swing.JButton();
        btnUserUpdate = new javax.swing.JButton();
        btnUserExcluir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Usuários");
        setPreferredSize(new java.awt.Dimension(913, 700));

        jLabel1.setText("*id");

        jLabel2.setText("*Nome");

        jLabel3.setText("Telefone");

        jLabel4.setText("*Login");

        jLabel5.setText("*Senha");

        jLabel6.setText("*Perfil");

        txtUserId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserIdActionPerformed(evt);
            }
        });

        txtUserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserNameActionPerformed(evt);
            }
        });

        txtUserTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserTelActionPerformed(evt);
            }
        });

        txtUserLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserLoginActionPerformed(evt);
            }
        });

        txtUserSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserSenhaActionPerformed(evt);
            }
        });

        cboUserPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));

        btnUserCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/create.png"))); // NOI18N
        btnUserCreate.setToolTipText("Adicionar");
        btnUserCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserCreate.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUserCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserCreateActionPerformed(evt);
            }
        });

        btnUserBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/read.png"))); // NOI18N
        btnUserBuscar.setToolTipText("Buscar");
        btnUserBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserBuscarActionPerformed(evt);
            }
        });

        btnUserUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/update.png"))); // NOI18N
        btnUserUpdate.setToolTipText("Atualizar");
        btnUserUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserUpdateActionPerformed(evt);
            }
        });

        btnUserExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/delete.png"))); // NOI18N
        btnUserExcluir.setToolTipText("Excluir");
        btnUserExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUserSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUserTel, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(97, 97, 97)
                                        .addComponent(btnUserBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(178, 178, 178)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel6))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboUserPerfil, 0, 246, Short.MAX_VALUE)
                                            .addComponent(txtUserLogin)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(btnUserUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(74, 74, 74)
                                        .addComponent(btnUserExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(66, 66, 66))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btnUserCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtUserTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(txtUserLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtUserSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cboUserPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUserUpdate, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnUserExcluir, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnUserBuscar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnUserCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnUserBuscar, btnUserCreate, btnUserExcluir, btnUserUpdate});

        setBounds(0, 0, 912, 623);
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNameActionPerformed

    private void txtUserTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserTelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserTelActionPerformed

    private void txtUserLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserLoginActionPerformed

    private void txtUserSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserSenhaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserSenhaActionPerformed

    private void txtUserIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserIdActionPerformed

    private void btnUserBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserBuscarActionPerformed
         buscar();      
    }//GEN-LAST:event_btnUserBuscarActionPerformed

    private void btnUserCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserCreateActionPerformed
        adicionar();
    }//GEN-LAST:event_btnUserCreateActionPerformed

    private void btnUserUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserUpdateActionPerformed
        atualizar();
    }//GEN-LAST:event_btnUserUpdateActionPerformed

    private void btnUserExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnUserExcluirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUserBuscar;
    private javax.swing.JButton btnUserCreate;
    private javax.swing.JButton btnUserExcluir;
    private javax.swing.JButton btnUserUpdate;
    private javax.swing.JComboBox<String> cboUserPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtUserId;
    private javax.swing.JTextField txtUserLogin;
    private javax.swing.JTextField txtUserName;
    private javax.swing.JTextField txtUserSenha;
    private javax.swing.JTextField txtUserTel;
    // End of variables declaration//GEN-END:variables

    
}
