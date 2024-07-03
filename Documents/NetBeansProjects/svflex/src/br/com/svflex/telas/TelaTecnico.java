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

import br.com.svflex.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Manipulação de dados de técnico
 * @author Adriano Barbosa
 * @version 1.1
 */
public class TelaTecnico extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public TelaTecnico() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    /**
 * Método para limpar os dados
 */
    private void LimparDados() {

            txtTecName.setText(null);
            txtTecTel.setText(null);
            txtTecEsp.setText(null);
            txtTecLog.setText(null);
            txtTecCid.setText(null);
            txtTecBai.setText(null);
            txtTecEst.setText(null);
            txtTecPesquisar.setText(null);
            txtTecId.setText(null);
            ((DefaultTableModel) tblTec.getModel()).setRowCount(0);
    }
 /**Método responsável pela validação dos dados
 * 
 * @return validacao
 */
     private boolean validacao() {
        return !(txtTecName.getText().isEmpty()
                || txtTecTel.getText().isEmpty()
                || txtTecLog.getText().isEmpty()
                || txtTecEsp.getText().isEmpty());
    }
/**
 * Método para adicionar técnico
 */
      private void adicionar() {
        String sql = "insert into tbtecnico(nome, logradouro, bairro, cidade, estado, telefone, especialidade)values(?, ?, ?, ?, ?, ?, ?);";
        try {
            //conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtTecName.getText());
            pst.setString(2, txtTecLog.getText());
            pst.setString(3, txtTecBai.getText());
            pst.setString(4, txtTecCid.getText());
            pst.setString(5, txtTecEst.getText());
            pst.setString(6, txtTecTel.getText());
            pst.setString(7, txtTecEsp.getText());

            if (validacao()) {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Técnico adicionado com sucesso!");
                    //TelaCliente.LimparDados limpar = new TelaCliente.LimparDados();
                    LimparDados();
                    btnAdicionar.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao adicionar técnico. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }
/**
 * Método para alterar técnico
 */      
       private void atualizar() {
        String sql = "update tbtecnico set nome = ?, logradouro = ?, bairro = ?, cidade = ?, estado = ?, telefone = ?, especialidade = ? where id_tecnico = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtTecName.getText());
            pst.setString(2, txtTecLog.getText());
            pst.setString(3, txtTecBai.getText());
            pst.setString(4, txtTecCid.getText());
            pst.setString(5, txtTecEst.getText());
            pst.setString(6, txtTecTel.getText());
            pst.setString(7, txtTecEsp.getText());
            pst.setString(8, txtTecId.getText());

            if (validacao()) {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do Técnico Atualizados com sucesso!");
                    LimparDados();
                    btnAdicionar.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao atualizar técnico. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
/**
 * Método para excluir técnico
 */
    private void excluir() {
        int confirma = JOptionPane
                .showConfirmDialog(null, "Tem certeza que deseja remover este técnico?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbtecnico where id_tecnico = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtTecId.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Técnico excluído com sucesso!");
                    LimparDados();
                    btnAdicionar.setEnabled(true);
                    
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao excluir técnico. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
/**
 * Método para pesquisar técnico
 */
    private void pesquisar() {
        String sql = " select id_tecnico as Cod_Tecnico, nome, logradouro, bairro, cidade, estado, telefone, especialidade from tbtecnico where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtTecPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tblTec.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao pesquisar técnico. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
/**
 * Método para setar campos
 */
    private void setar_campos() {
        int setar = tblTec.getSelectedRow();
        
        txtTecId.setText(tblTec.getModel().getValueAt(setar, 0).toString());
        txtTecName.setText(tblTec.getModel().getValueAt(setar, 1).toString());
        txtTecLog.setText(tblTec.getModel().getValueAt(setar, 2).toString());
        txtTecBai.setText(tblTec.getModel().getValueAt(setar, 3).toString());
        txtTecCid.setText(tblTec.getModel().getValueAt(setar, 4).toString());
        txtTecEst.setText(tblTec.getModel().getValueAt(setar, 5).toString());
        txtTecTel.setText(tblTec.getModel().getValueAt(setar, 6).toString());
        txtTecEsp.setText(tblTec.getModel().getValueAt(setar, 7).toString());
        btnAdicionar.setEnabled(false);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnUserExcluir = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtTecPesquisar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTec = new javax.swing.JTable();
        txtTecCid = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtTecLog = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTecBai = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTecName = new javax.swing.JTextField();
        txtTecEst = new javax.swing.JTextField();
        txtTecId = new javax.swing.JTextField();
        txtTecTel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTecEsp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnAdicionar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnUserUpdate = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Técnicos");
        setPreferredSize(new java.awt.Dimension(912, 623));

        btnUserExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/delete.png"))); // NOI18N
        btnUserExcluir.setToolTipText("Excluir");
        btnUserExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserExcluirActionPerformed(evt);
            }
        });

        jLabel7.setText("*Logradouro");

        txtTecPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTecPesquisarActionPerformed(evt);
            }
        });
        txtTecPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTecPesquisarKeyReleased(evt);
            }
        });

        tblTec = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblTec.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cód_Técnico", "nome", "logradouro", "bairro", "cidade", "estado", "telefone", "especialidade"
            }
        ));
        tblTec.getTableHeader().setReorderingAllowed(false);
        tblTec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTecMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblTec);

        txtTecCid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTecCidActionPerformed(evt);
            }
        });

        jLabel1.setText("*Nome");

        txtTecLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTecLogActionPerformed(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/pesquisar.png"))); // NOI18N

        jLabel2.setText("*Telefone");

        jLabel10.setText("Cód");

        txtTecId.setEnabled(false);
        txtTecId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTecIdActionPerformed(evt);
            }
        });

        jLabel3.setText("Cidade");

        jLabel4.setText("Estado");

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        jLabel5.setText("Bairro");

        btnUserUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/update.png"))); // NOI18N
        btnUserUpdate.setToolTipText("Atualizar");
        btnUserUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserUpdateActionPerformed(evt);
            }
        });

        jLabel6.setText("*Especialidade");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTecName, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTecCid, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTecBai)
                                    .addComponent(txtTecEsp)
                                    .addComponent(txtTecTel, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtTecLog, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 828, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(80, 80, 80)
                                .addComponent(btnUserUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(70, 70, 70)
                                .addComponent(btnUserExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTecEst, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 31, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(txtTecPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(txtTecId, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9)
                        .addComponent(txtTecPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(txtTecId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtTecLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtTecEsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTecCid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtTecBai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTecEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTecName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtTecTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnUserUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUserExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );

        setBounds(0, 0, 912, 623);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUserExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnUserExcluirActionPerformed

    private void txtTecPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecPesquisarActionPerformed

    private void txtTecPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTecPesquisarKeyReleased
        pesquisar();
    }//GEN-LAST:event_txtTecPesquisarKeyReleased

    private void tblTecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTecMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblTecMouseClicked

    private void txtTecCidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecCidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecCidActionPerformed

    private void txtTecLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecLogActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecLogActionPerformed

    private void txtTecIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecIdActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnUserUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserUpdateActionPerformed
        atualizar();
    }//GEN-LAST:event_btnUserUpdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnUserExcluir;
    private javax.swing.JButton btnUserUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblTec;
    private javax.swing.JTextField txtTecBai;
    private javax.swing.JTextField txtTecCid;
    private javax.swing.JTextField txtTecEsp;
    private javax.swing.JTextField txtTecEst;
    private javax.swing.JTextField txtTecId;
    private javax.swing.JTextField txtTecLog;
    private javax.swing.JTextField txtTecName;
    private javax.swing.JTextField txtTecPesquisar;
    private javax.swing.JTextField txtTecTel;
    // End of variables declaration//GEN-END:variables
}
