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
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Nova
 */
public class TelaNota extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    /**
     * Creates new form TelaNota
     */
    public TelaNota() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
     private boolean validacao() {
        return !(txtOs.getText().isEmpty()
                || txtCli.getText().isEmpty()
                || txtIdEmp.getText().isEmpty()
                || txtIdCli.getText().isEmpty()
                || cboPag.getSelectedItem().toString().isEmpty()
                || cboParc.getSelectedItem().toString().isEmpty()
                || cboGar.getSelectedItem().toString().isEmpty());
    }
/**
 * Método para limpar os dados
 */
    private void limparDados() {
        txtOs.setText(null);
        txtCli.setText(null);
        txtIdEmp.setText(null);
        txtIdCli.setText(null);
        cboPag.setSelectedItem(null);
        cboParc.setSelectedItem(null);
        cboGar.setSelectedItem(null);
        txtObs.setText(null);       
    }
    
    private void pesquisar_os() {
        String sql = "select id_os as OS, nome_cli as Cliente, id_empresa as Empresa,id_cliente as Cod_Cliente from tbos where id_os like ? and tipo = 'Ordem de Serviço' ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtOsPesq.getText() + "%");
            rs = pst.executeQuery();
            tblOs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao pesquisar cliente. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     private void setar_campos() {
        int setar = tblOs.getSelectedRow();
        txtOs.setText(tblOs.getModel().getValueAt(setar, 0).toString());
        txtCli.setText(tblOs.getModel().getValueAt(setar, 1).toString());
        txtIdEmp.setText(tblOs.getModel().getValueAt(setar, 2).toString());
        txtIdCli.setText(tblOs.getModel().getValueAt(setar, 3).toString());
    }

     private void emitir_nota(){
         String sql = "insert into tbnota(forma_pagamento, parcelamento,garantia, observacao, id_os ) values(?, ?, ?, ?, ?)";
         try {
             pst = conexao.prepareStatement(sql);
             pst.setString(1, cboPag.getSelectedItem().toString());
             pst.setString(2, cboParc.getSelectedItem().toString());
             pst.setString(3, cboGar.getSelectedItem().toString());
             pst.setString(4, txtObs.getText());
             pst.setString(5, txtOs.getText());
             if (validacao()) {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Nota Emitida com sucesso!");
                    limparDados();
                    btnAdicionar.setEnabled(true);
                    txtOsPesq.setEnabled(true);
                    tblOs.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
         } catch (SQLException e) {
              JOptionPane.showMessageDialog(null, "Ocorreu um erro ao emitir Nota. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
         }
     }
     
     private void pesquisar_nota() {
            
            String nota = JOptionPane.showInputDialog("Número da Nota:");
            String sql = "select n.*, c.id_cliente, c.nome, o.id_empresa from tbnota n inner join tbos o on n.id_os = o.id_os inner join tbclientes c on o.id_cliente = c.id_cliente where id_nota = " + nota;
            if (nota != null && !nota.isEmpty()) {
            try {
                
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();

                if (rs.next()) {
                    txtIdNota.setText(rs.getString(1));
                    cboPag.setSelectedItem(rs.getString(2));
                    cboParc.setSelectedItem(rs.getString(3));
                    cboGar.setSelectedItem(rs.getString(4));
                    txtObs.setText(rs.getString(5));
                    txtOs.setText(rs.getString(6));
                    txtIdCli.setText(rs.getString(7));
                    txtCli.setText(rs.getString(8));
                    txtIdEmp.setText(rs.getString(9));
                    btnAdicionar.setEnabled(false);
                }else{
                    JOptionPane.showMessageDialog(null, "Nota não cadastrada");
                }
            }catch (java.sql.SQLSyntaxErrorException e) {
                JOptionPane.showMessageDialog(null, "Nota Inválida");
                //System.out.println(e);
            } catch (HeadlessException | SQLException e2) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao pesquisar Nota. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
     }
     }
     
     private void alterar(){
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja alterar esta Nota?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            String sql = "update tbnota set forma_pagamento=?, parcelamento=?,garantia=?, observacao=?, id_os=? where id_nota = ? ";
            try {
             pst = conexao.prepareStatement(sql);
             pst.setString(1, cboPag.getSelectedItem().toString());
             pst.setString(2, cboParc.getSelectedItem().toString());
             pst.setString(3, cboGar.getSelectedItem().toString());
             pst.setString(4, txtObs.getText());
             pst.setString(5, txtOs.getText());
             pst.setString(6, txtIdNota.getText());
             if (validacao()) {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Nota Emitida com sucesso!");
                    limparDados();
                    btnAdicionar.setEnabled(true);
                    txtOsPesq.setEnabled(true);
                    tblOs.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
         } catch (SQLException e) {
              JOptionPane.showMessageDialog(null, "Ocorreu um erro ao alterar Nota. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
         }
        }
     }
     
      private void excluir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esta Nota?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbnota where id_nota = ?";
            try {
                pst = conexao.prepareStatement(sql);
                 pst.setString(1, txtIdNota.getText());
                String os = txtOs.getText();
                int apagado = pst.executeUpdate();
                if (apagado > 0 && !os.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nota Excluída com sucesso!");
                    limparDados();
                    btnAdicionar.setEnabled(true);
                    txtOsPesq.setEnabled(true);
                    tblOs.setVisible(true);
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao excluir Nota. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtOs = new javax.swing.JTextField();
        txtCli = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblOs = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtIdCli = new javax.swing.JTextField();
        txtOsPesq = new javax.swing.JTextField();
        txtIdEmp = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cboPag = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cboParc = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cboGar = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtObs = new javax.swing.JTextField();
        txtIdNota = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Notas");

        jLabel2.setText("*OS");

        txtOs.setEditable(false);
        txtOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOsActionPerformed(evt);
            }
        });

        txtCli.setEditable(false);

        jLabel1.setText("*Cód_Cli");

        tblOs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "OS", "Cliente", "Empresa", "Cod_Cliente"
            }
        ));
        tblOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOsMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblOs);

        jLabel3.setText("*Empresa");

        txtIdCli.setEditable(false);
        txtIdCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdCliActionPerformed(evt);
            }
        });

        txtOsPesq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtOsPesqMouseReleased(evt);
            }
        });
        txtOsPesq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOsPesqActionPerformed(evt);
            }
        });
        txtOsPesq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOsPesqKeyReleased(evt);
            }
        });

        txtIdEmp.setEditable(false);
        txtIdEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdEmpActionPerformed(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/pesquisar.png"))); // NOI18N

        jLabel12.setText("Cliente");

        jLabel25.setText("Pesquisar OS:");

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/update.png"))); // NOI18N
        btnUpdate.setToolTipText("Atualizar");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/print.png"))); // NOI18N
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/read.png"))); // NOI18N
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel4.setText("Forma de Pagamento:");

        cboPag.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dinheiro", "Pix", "Cartão de Débito", "Cartão de Crédito" }));

        jLabel5.setText("Parcelamento:");

        cboParc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sem Parcelamento", "1 X", "2 X", "3 X", "4 X", "5 X", "6 X", "7 X", "8 X", "9 X", "10 X", "11 X", "12 X", "13 X", "14 X ", "15 X", "16 X" }));
        cboParc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboParcActionPerformed(evt);
            }
        });

        jLabel6.setText("Garantia:");

        cboGar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sem Garantia", "1 Mês", "2 Meses", "3 Meses", "4 Meses", "5 Meses", "6 Meses", "7 Meses", "8 Meses", "9 Meses", "10 Meses", "11 Meses", "12 Meses", "14 Meses", "15 Meses", "16 Meses", "17 Meses", "18 Meses", "19 Meses", "20 Meses", "21 Meses", "22 Meses", "23 Meses", "24 Meses" }));

        jLabel7.setText("Observação:");

        txtIdNota.setEditable(false);
        txtIdNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdNotaActionPerformed(evt);
            }
        });

        jLabel8.setText("Cód_Nota");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCli, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtIdNota, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(157, 157, 157)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboParc, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtIdEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(140, 140, 140)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(62, 62, 62))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtOsPesq, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtOs, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtObs))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboGar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboPag, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(12, 12, 12))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel25)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(txtOsPesq, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtIdEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5)
                                .addComponent(cboParc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(txtIdNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(cboGar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel7))
                    .addComponent(txtObs, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnImprimir))
                .addGap(28, 28, 28))
        );

        setBounds(0, 0, 912, 623);
    }// </editor-fold>//GEN-END:initComponents

    private void txtOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsActionPerformed

    private void tblOsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOsMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblOsMouseClicked

    private void txtIdCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdCliActionPerformed

    private void txtOsPesqMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOsPesqMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsPesqMouseReleased

    private void txtOsPesqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsPesqActionPerformed
        //pesquisar();
    }//GEN-LAST:event_txtOsPesqActionPerformed

    private void txtOsPesqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsPesqKeyReleased
        pesquisar_os();
    }//GEN-LAST:event_txtOsPesqKeyReleased

    private void txtIdEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdEmpActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        alterar();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        String nota = txtIdNota.getText();
        if(!nota.isEmpty()){
            try {
                Map<String, Object> parametros = new HashMap<>();

                parametros.put("nota", Integer.parseInt(nota));

                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\Os.jasper");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexao);
                JasperViewer.viewReport(print,false);
            }
            catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar a Nota. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }}
            else{
                JOptionPane.showMessageDialog(null, "N° da Nota não informado");
            }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        emitir_nota();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        pesquisar_nota();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void cboParcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboParcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboParcActionPerformed

    private void txtIdNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdNotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdNotaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboGar;
    private javax.swing.JComboBox<String> cboPag;
    private javax.swing.JComboBox<String> cboParc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable tblOs;
    private javax.swing.JTextField txtCli;
    private javax.swing.JTextField txtIdCli;
    private javax.swing.JTextField txtIdEmp;
    private javax.swing.JTextField txtIdNota;
    private javax.swing.JTextField txtObs;
    private javax.swing.JTextField txtOs;
    private javax.swing.JTextField txtOsPesq;
    // End of variables declaration//GEN-END:variables
}
