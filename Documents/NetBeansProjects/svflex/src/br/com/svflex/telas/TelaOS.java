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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Manipulação de OS
 * @author Adriano Barbosa
 * @version 1.1
 */
public class TelaOS extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    private String tipo;

    public TelaOS() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
/**Método responsável pela validação dos dados
 * 
 * @return validacao
 */
    private boolean validacao() {
        return !(txtTecNome.getText().isEmpty()
                || txtTecId.getText().isEmpty()
                || txtOsProb.getText().isEmpty()
                || txtOsValTot.getText().isEmpty()
                //|| txtTecPesquisar.getText().isEmpty()
                || txtCliId.getText().isEmpty());
    }
/**
 * Método para limpar os dados
 */
    private void LimparDados() {
        txtOsProb.setText(null);
        txtCliId.setText(null);
        txtOsDesc.setText(null);
        txtOsValTot.setText(null);
        txtOsValMat.setText(null);
        txtCliPesquisar.setText(null);
        txtCliId.setText(null);
        txtTecPesquisar.setText(null);
        txtTecId.setText(null);
        txtTecNome.setText(null);
        txtOsId.setText(null);
        txtOsData.setText(null);
        txtOsCli.setText(null);
        ((DefaultTableModel) tblClientes.getModel()).setRowCount(0);
        ((DefaultTableModel) tblTec.getModel()).setRowCount(0);
    }
/**
 * Método para pesquisar cliente
 */
    private void pesquisar() {
        String sql = "select id_cliente as Cod_Cliente, nome as Nome, telefone as Telefone, bairro as Bairro from tbclientes where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao pesquisar cliente. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
/**
 * Método para pesquisar técnico
 */
    private void pesquisar_tec() {
        String sql = "select id_tecnico as Cod_Tecnico, nome as Nome, especialidade as Especialidade from tbtecnico where especialidade like ?";
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
 * Método para setar campos técnico
 */
    private void setar_campos_tec() {
        int setar_tec = tblTec.getSelectedRow();
        txtTecId.setText(tblTec.getModel().getValueAt(setar_tec, 0).toString());
        txtTecNome.setText(tblTec.getModel().getValueAt(setar_tec, 1).toString());
    }
/**
 * Método para setar campos cliente
 */
    private void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtOsCli.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
    }
/**
 * Método para emitir OS
 */
    private void emitir_os() {
        String sql = "insert into tbos (tipo, status_os, problema, id_tecnico, id_cliente, descricao, valor, material, valor_material, nome_tecnico, nome_cli) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // boolean chkMat = chkOsMat.isSelected();
        try {
            boolean check = chkOsMat.isSelected();
            double val = 0.00;
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboStt.getSelectedItem().toString());
            pst.setString(3, txtOsProb.getText());
            pst.setString(4, txtTecId.getText());
            pst.setString(5, txtCliId.getText());
            pst.setString(6, txtOsDesc.getText());
            if (txtOsValTot.getText().isEmpty()) {
                pst.setDouble(7, val);
            } else {
                pst.setString(7, txtOsValTot.getText().replace(",", "."));
            }
            if (check == false) {
                pst.setBoolean(8, check);
            } else {
                pst.setBoolean(8, check);
            }
            if (txtOsValMat.getText().isEmpty()) {
                pst.setDouble(9, val);
            } else {
                pst.setString(9, txtOsValMat.getText().replace(",", "."));
            }
            pst.setString(10, txtTecNome.getText());
            pst.setString(11, txtOsCli.getText());
            if (validacao()) {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "OS Emitida com sucesso!");
                    //TelaCliente.LimparDados limpar = new TelaCliente.LimparDados();
                    LimparDados();
                    btnAdicionar.setEnabled(true);
                    txtCliPesquisar.setEnabled(true);
                    txtTecPesquisar.setEnabled(true);
                    tblClientes.setVisible(true);
                    tblTec.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao emitir OS. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
/**
 * Método para pesquisar OS
 */
    private void pesquisar_os() {
            
            String num_os = JOptionPane.showInputDialog("Número da Os:");
            String sql = "select * from tbos where id_os = " + num_os;
            if (num_os != null && !num_os.isEmpty()) {
            try {
                
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();

                if (rs.next()) {
                    txtOsId.setText(rs.getString(1));
                    txtOsData.setText(rs.getString(2));
                    String rbTipo = rs.getString(3);

                    if (rbTipo.equals("Ordem de Serviço")) {
                        rbtOs.setSelected(true);
                        tipo = "Ordem de Serviço";
                    } else {
                        rbtOrc.setSelected(true);
                        tipo = "Orçamento";
                    }
                    cboStt.setSelectedItem(rs.getString(4));
                    txtOsProb.setText(rs.getString(5));
                    txtTecId.setText(rs.getString(6));
                    txtCliId.setText(rs.getString(7));
                    txtOsDesc.setText(rs.getString(8));
                    txtOsValTot.setText(rs.getString(9));
                    boolean check = rs.getBoolean(10);
                    if (check == false) {
                        chkOsMat.setSelected(check);
                    } else {
                        chkOsMat.setSelected(check);
                    }
                    txtOsValMat.setText(rs.getString(11));
                    txtTecNome.setText(rs.getString(12));
                    txtOsCli.setText(rs.getString(13));
                    btnAdicionar.setEnabled(false);
                    txtCliPesquisar.setEnabled(false);
                    txtTecPesquisar.setEnabled(false);
                    tblClientes.setVisible(false);
                    tblTec.setVisible(false);
                } //if (txtOsId != null) {}
                else {
                    JOptionPane.showMessageDialog(null, "OS não cadastrada");
                }
                

            } catch (java.sql.SQLSyntaxErrorException e) {
                JOptionPane.showMessageDialog(null, "OS Inválida");
                //System.out.println(e);
            } catch (HeadlessException | SQLException e2) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao pesquisar OS. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
            
        
    }
/**
 * Método para alterar OS
 */
    private void alterar() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja alterar esta OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
        String sql = "update tbos set tipo = ?, status_os = ?, problema = ?, descricao = ?, valor = ?, material = ?, valor_material = ?, nome_tecnico = ? where id_os = ?";
        try {
            boolean check = chkOsMat.isSelected();
            double val = 0.00;
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboStt.getSelectedItem().toString());
            pst.setString(3, txtOsProb.getText());
            pst.setString(4, txtOsDesc.getText());
            if (txtOsValTot.getText().isEmpty()) {
                pst.setDouble(5, val);
            } else {
                pst.setString(5, txtOsValTot.getText().replace(",", "."));
            }
            if (check == false) {
                pst.setBoolean(6, check);
            } else {
                pst.setBoolean(6, check);
            }
            if (txtOsValMat.getText().isEmpty()) {
                pst.setDouble(7, val);
            } else {
                pst.setString(7, txtOsValMat.getText().replace(",", "."));
            }
            pst.setString(8, txtTecNome.getText());
            pst.setString(9, txtOsId.getText());
            if (validacao()) {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "OS Alterada com sucesso!");
                    LimparDados();
                    btnAdicionar.setEnabled(true);
                    txtCliPesquisar.setEnabled(true);
                    txtTecPesquisar.setEnabled(true);
                    tblClientes.setVisible(true);
                    tblTec.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao atualizar OS. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }}
/**
 * Método para excluir OS
 */
    private void excluir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esta OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbos where id_os = ?";
            try {
                pst = conexao.prepareStatement(sql);
                 pst.setString(1, txtOsId.getText());
                String os = txtOsId.getText();
                int apagado = pst.executeUpdate();
                if (apagado > 0 && !os.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Os Excluída com sucesso!");
                    LimparDados();
                    btnAdicionar.setEnabled(true);
                    txtCliPesquisar.setEnabled(true);
                    txtTecPesquisar.setEnabled(true);
                    tblClientes.setVisible(true);
                    tblTec.setVisible(true);
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao excluir OS. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel10 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtOsId = new javax.swing.JTextField();
        txtOsData = new javax.swing.JTextField();
        rbtOrc = new javax.swing.JRadioButton();
        rbtOs = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        cboStt = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtCliPesquisar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        txtOsCli = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtOsProb = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtOsDesc = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtOsValMat = new javax.swing.JTextField();
        chkOsMat = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        txtOsValTot = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblTec = new javax.swing.JTable();
        txtTecPesquisar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTecId = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTecNome = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnAdicionar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        jLabel10.setText("Valor Material");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("OS");
        setPreferredSize(new java.awt.Dimension(912, 623));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("N°OS");

        jLabel2.setText("Data");

        txtOsId.setEditable(false);

        txtOsData.setEditable(false);
        txtOsData.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtOsData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOsDataActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtOrc);
        rbtOrc.setText("Orçamento");
        rbtOrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOrcActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtOs);
        rbtOs.setText("Ordem de Serviço");
        rbtOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtOrc)
                        .addGap(18, 18, 18)
                        .addComponent(rbtOs))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtOsId, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(122, 122, 122))
                            .addComponent(txtOsData))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOsData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtOrc)
                    .addComponent(rbtOs))
                .addGap(17, 17, 17))
        );

        jLabel3.setText("Status");

        cboStt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pendente", "Aguardando Aprovação", "Aguardando Pagamento", "Em Andamento", "Concluído", "Cancelado" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        txtCliPesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtCliPesquisarMouseReleased(evt);
            }
        });
        txtCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliPesquisarActionPerformed(evt);
            }
        });
        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/pesquisar.png"))); // NOI18N

        jLabel5.setText("*Cód_Cli");

        txtCliId.setEditable(false);

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cód_Cliente", "Nome", "Telefone", "Bairro"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        tblClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblClientesKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jLabel16.setText("*Nome do Cliente:");

        txtOsCli.setEditable(false);
        txtOsCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOsCliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtOsCli, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel16)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtOsCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setText("*Problema");

        jLabel7.setText("Descrição");

        jLabel8.setText("Valor Material");

        chkOsMat.setText("Com Material");
        chkOsMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOsMatActionPerformed(evt);
            }
        });

        jLabel13.setText("*Valor Total");

        txtOsValTot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOsValTotActionPerformed(evt);
            }
        });

        tblTec.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Cód_Técnico", "Nome", "Especialidade"
            }
        ));
        tblTec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTecMouseClicked(evt);
            }
        });
        tblTec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblTecKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tblTec);

        txtTecPesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTecPesquisarMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtTecPesquisarMouseReleased(evt);
            }
        });
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

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/pesquisar.png"))); // NOI18N

        jLabel12.setText("*Cód_Téc");

        txtTecId.setEditable(false);

        jLabel14.setText("Especialidade:");

        txtTecNome.setEditable(false);

        jLabel15.setText("*Técnico");

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

        jLabel17.setText("*Empresa");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cboStt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(60, 60, 60))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtOsProb, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtTecPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel9)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(167, 167, 167)
                                    .addComponent(jLabel8))
                                .addGroup(layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel12)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtTecId, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15)))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtOsValMat)
                                .addComponent(chkOsMat)
                                .addComponent(txtTecNome, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel14)
                                .addComponent(jLabel7))
                            .addGap(18, 18, 18)
                            .addComponent(txtOsDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtOsValTot, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(93, 93, 93))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cboStt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtOsProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtOsDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTecPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtTecId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTecNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtOsValMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkOsMat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtOsValTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(170, 170, 170))
        );

        getAccessibleContext().setAccessibleName("Cliente");

        setBounds(0, 0, 912, 623);
    }// </editor-fold>//GEN-END:initComponents

    private void txtOsDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsDataActionPerformed

    private void rbtOrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOrcActionPerformed
        tipo = "Orçamento";
    }//GEN-LAST:event_rbtOrcActionPerformed

    private void txtOsValTotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsValTotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsValTotActionPerformed

    private void txtCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliPesquisarActionPerformed
        //pesquisar();
    }//GEN-LAST:event_txtCliPesquisarActionPerformed

    private void tblClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblClientesKeyReleased
        //pesquisar();
    }//GEN-LAST:event_tblClientesKeyReleased

    private void txtCliPesquisarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCliPesquisarMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliPesquisarMouseReleased

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        pesquisar();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void rbtOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOsActionPerformed
        tipo = "Ordem de Serviço";
    }//GEN-LAST:event_rbtOsActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        rbtOrc.setSelected(true);
        tipo = "Orçamento";
    }//GEN-LAST:event_formInternalFrameOpened

    private void chkOsMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOsMatActionPerformed

    }//GEN-LAST:event_chkOsMatActionPerformed

    private void tblTecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTecMouseClicked
        setar_campos_tec();
    }//GEN-LAST:event_tblTecMouseClicked

    private void tblTecKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTecKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTecKeyReleased

    private void txtTecPesquisarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTecPesquisarMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecPesquisarMouseReleased

    private void txtTecPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecPesquisarActionPerformed

    private void txtTecPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTecPesquisarKeyReleased
        pesquisar_tec();
    }//GEN-LAST:event_txtTecPesquisarKeyReleased

    private void txtTecPesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTecPesquisarMouseClicked

    }//GEN-LAST:event_txtTecPesquisarMouseClicked

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        emitir_os();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        pesquisar_os();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        alterar();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
       String os = txtOsId.getText();
       if(!os.isEmpty()){
            try {
                Map<String, Object> parametros = new HashMap<>();
                
                parametros.put("os", Integer.parseInt(os));
           
                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\Os.jasper");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexao);
                JasperViewer.viewReport(print,false);
           }
             catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar a OS. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }}
            else{
               JOptionPane.showMessageDialog(null, "N° da OS não informado");
           }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void txtOsCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsCliActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboStt;
    private javax.swing.JCheckBox chkOsMat;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JRadioButton rbtOrc;
    private javax.swing.JRadioButton rbtOs;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblTec;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtOsCli;
    private javax.swing.JTextField txtOsData;
    private javax.swing.JTextField txtOsDesc;
    private javax.swing.JTextField txtOsId;
    private javax.swing.JTextField txtOsProb;
    private javax.swing.JTextField txtOsValMat;
    private javax.swing.JTextField txtOsValTot;
    private javax.swing.JTextField txtTecId;
    private javax.swing.JTextField txtTecNome;
    private javax.swing.JTextField txtTecPesquisar;
    // End of variables declaration//GEN-END:variables
}
