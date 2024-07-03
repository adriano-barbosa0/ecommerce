/*
 * The MIT License
 *
 * Copyright 2024 Nova.
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

import br.com.svflex.dal.ModuloConexao;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Nova
 */
public class TelaEmpresa extends javax.swing.JInternalFrame {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private String selectedFilePath;
    /**
     * Creates new form TelaEmpresa
     */
    public TelaEmpresa() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    private void limparDados(){
        cboEmp.setSelectedItem(null);
        txtEmpName.setText(null);
        txtEmpEmail.setText(null);
        txtEmpCNPJ.setText(null);
        txtEmpFant.setText(null);
        txtEmpImg.setText(null);
    }
    
    private boolean validacao(){
        return !(
                 txtEmpName.getText().isEmpty()
                || txtEmpCNPJ.getText().isEmpty()
                || txtEmpImg.getText().isEmpty()
                );
    }
    
    private void buscar(){
        String sql = "select * from tbempresa where id_empresa = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cboEmp.getSelectedItem().toString());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtEmpName.setText(rs.getString(2));
                txtEmpEmail.setText(rs.getString(3));
                txtEmpCNPJ.setText(rs.getString(4));
                txtEmpFant.setText(rs.getString(5));
                txtEmpImg.setText(rs.getString(6));
                btnCreate.setEnabled(false);
            }else {
                JOptionPane.showMessageDialog(null, "Empresa não cadastrada");
                limparDados();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao buscar empresa. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     private void adicionar(){
        String sql = "insert into tbempresa (id_empresa, nome, email, cnpj, nome_fantasia, imagem)values(?, ?, ?, ?, ?, ?);";
        try {
            //conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cboEmp.getSelectedItem().toString());
            pst.setString(2, txtEmpName.getText());
            pst.setString(3, txtEmpEmail.getText());
            pst.setString(4, txtEmpCNPJ.getText());
            pst.setString(5, txtEmpFant.getText());
            pst.setString(6, txtEmpImg.getText());
            if(validacao()){
            
            int adicionado = pst.executeUpdate();
            if(adicionado > 0){
                JOptionPane.showMessageDialog(null, "Empresa adicionada com sucesso!");
                limparDados();
                btnCreate.setEnabled(true);
            }
            } else{
                 JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao adicionar empresa. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
    }
     
     private void atualizar(){
         int confirma = JOptionPane
                .showConfirmDialog(null, "Tem certeza que deseja atualizar este empresa?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
        String sql = "update tbempresa set nome = ?, email = ?, cnpj = ?, nome_fantasia = ?, imagem = ? where id_empresa = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEmpName.getText());
            pst.setString(2, txtEmpEmail.getText());
            pst.setString(3, txtEmpCNPJ.getText());
            pst.setString(4, txtEmpFant.getText());
            pst.setString(5, txtEmpImg.getText());
            pst.setString(6, cboEmp.getSelectedItem().toString());
            
            if(validacao()){
                
               int adicionado = pst.executeUpdate();
            if(adicionado > 0){
                JOptionPane.showMessageDialog(null, "Dados da Empresa Atualizados com sucesso!");
                limparDados();
                btnCreate.setEnabled(true);
            }
            } else{
                 JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao atualizar empresa. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }}
     
     private void excluir(){
        int confirma = JOptionPane
                .showConfirmDialog(null, "Tem certeza que deseja remover este empresa?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            String sql = "delete from tbempresa where id_empresa = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, cboEmp.getSelectedItem().toString());
               int apagado = pst.executeUpdate();
               if(apagado > 0){ 
                    JOptionPane.showMessageDialog(null, "Empresa excluída com sucesso!");
                    limparDados();
                    btnCreate.setEnabled(true);
            }} catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Ocorreu um erro ao excluir empresa. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
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

        btnEmpArq = new javax.swing.JButton();
        txtEmpEmail = new javax.swing.JTextField();
        txtEmpFant = new javax.swing.JTextField();
        txtEmpCNPJ = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnExcluir = new javax.swing.JButton();
        txtEmpName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtEmpImg = new javax.swing.JTextField();
        cboEmp = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Empresa");
        setToolTipText("");

        btnEmpArq.setText("selecione");
        btnEmpArq.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEmpArq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpArqActionPerformed(evt);
            }
        });

        txtEmpEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpEmailActionPerformed(evt);
            }
        });

        txtEmpFant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpFantActionPerformed(evt);
            }
        });

        txtEmpCNPJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpCNPJActionPerformed(evt);
            }
        });

        jLabel1.setText("*Cód:");

        jLabel2.setText("*Nome:");

        btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/create.png"))); // NOI18N
        btnCreate.setToolTipText("Adicionar");
        btnCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCreate.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        jLabel3.setText("Email da Empresa:");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/read.png"))); // NOI18N
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel4.setText("Nome Fantasia:");

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/update.png"))); // NOI18N
        btnUpdate.setToolTipText("Atualizar");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jLabel5.setText("*CNPJ:");

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        txtEmpName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpNameActionPerformed(evt);
            }
        });

        jLabel7.setText("*Selecione a Logo:");

        txtEmpImg.setEditable(false);
        txtEmpImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpImgActionPerformed(evt);
            }
        });

        cboEmp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtEmpCNPJ, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(29, 29, 29)
                                    .addComponent(jLabel7)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtEmpImg))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtEmpEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                                    .addComponent(jLabel4)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtEmpFant, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnEmpArq))
                            .addComponent(txtEmpName, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cboEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEmpName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtEmpFant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtEmpEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtEmpCNPJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtEmpImg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEmpArq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63))
        );

        setBounds(0, 0, 912, 623);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEmpArqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpArqActionPerformed
  // Cria um novo diálogo de seleção de arquivo
  JFileChooser fileChooser = new JFileChooser();

  // Define o tipo de arquivo que o usuário pode selecionar
  fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos de imagem", "jpg", "jpeg", "png"));

  // Mostra o diálogo de seleção de arquivo
  int result = fileChooser.showOpenDialog(null);

  // Verifica se o usuário selecionou um arquivo
  if (result == JFileChooser.APPROVE_OPTION) {

    // Obtém o arquivo selecionado
    File selectedFile = fileChooser.getSelectedFile();

    // Salva o caminho do arquivo selecionado na variável
    selectedFilePath = selectedFile.getAbsolutePath();
    
    txtEmpImg.setText(selectedFilePath);

    // Exibe uma mensagem com o caminho do arquivo selecionado
    JOptionPane.showMessageDialog(null, "Você selecionou o arquivo: " + selectedFilePath);

  }


    }//GEN-LAST:event_btnEmpArqActionPerformed

    private void txtEmpEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpEmailActionPerformed

    private void txtEmpFantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpFantActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpFantActionPerformed

    private void txtEmpCNPJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpCNPJActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpCNPJActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        adicionar();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        atualizar();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtEmpNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpNameActionPerformed

    private void txtEmpImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpImgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpImgActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnEmpArq;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboEmp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txtEmpCNPJ;
    private javax.swing.JTextField txtEmpEmail;
    private javax.swing.JTextField txtEmpFant;
    private javax.swing.JTextField txtEmpImg;
    private javax.swing.JTextField txtEmpName;
    // End of variables declaration//GEN-END:variables
}
