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
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TelaAgendamento extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaAgendamento
     */
    public TelaAgendamento() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void LimparDados() {

        //public LimparDados() {
            txtAgCli.setText(null);
            txtAgTec.setText(null);
            txtAgOs.setText(null);
            txtAgIdTec.setText(null);
            txtAgIdCli.setText(null);
            txtAgData.setText(null);
            txtAgHour.setText(null);
            txtAgId.setText(null);
            txtAgDataAgendado.setText(null);
            txtAgHourAgendado.setText(null);
            txtAgPesq.setText(null);
            txtOsPesq.setText(null);
    }
    
     private boolean validacao() {
        return !(txtAgIdCli.getText().isEmpty()
                || txtAgData.getText().isEmpty()
                || txtAgIdTec.getText().isEmpty()
                || txtAgHour.getText().isEmpty());
    }
    
     private void agendar() throws SQLException {
    String sql = "insert into tbagendamento (id_cli, id_tecnico, data_agendamento, horario) values(?,?,STR_TO_DATE(?, '%d/%m/%Y'),?)";
    try {
        pst = conexao.prepareStatement(sql);
        pst.setString(1, txtAgIdCli.getText());
        pst.setString(2, txtAgIdTec.getText());
        pst.setString(3, txtAgData.getText());
        pst.setString(4, txtAgHour.getText());
        if (validacao()) {
            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Agendamento criado com sucesso!");
                LimparDados();
                btnAgendar.setEnabled(true);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        }
    } catch (HeadlessException | SQLException e) {
        JOptionPane.showMessageDialog(null, "Ocorreu um erro ao agendar. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
        System.err.println(e);
    }
}
     
    private void pesquisar_os() {
        String sql = "select id_os as OS, nome_cli as Cliente, nome_tecnico as Tecnico, id_tecnico as id_tec, id_cliente from tbos where id_os like ? and tipo = 'Ordem de Serviço' ";
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
    
     private void pesquisar_ag() {
        String sql = "select id_agendamento as Cod_Agendamento, DATE_FORMAT (data_agendamento,'%d/%m/%Y') as Data, horario as Horario from tbagendamento where DATE_FORMAT (data_agendamento,'%d/%m/%Y') like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtAgPesq.getText() + "%");
            rs = pst.executeQuery();
            tblAg.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao pesquisar cliente. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
       }
    }
   private void setar_agendamento() {
        int setar = tblAg.getSelectedRow();
        txtAgId.setText(tblAg.getModel().getValueAt(setar, 0).toString());
        txtAgDataAgendado.setText(tblAg.getModel().getValueAt(setar, 1).toString());
        txtAgHourAgendado.setText(tblAg.getModel().getValueAt(setar, 2).toString());
    }
 
      private void setar_campos(){
         String setar = cboData.getSelectedItem().toString();
         txtAgData.setText(setar);
     }
     private List<String> getHorariosAgendados() {
    List<String> horariosAgendados = new ArrayList<>();
    String sql = "SELECT DATE_FORMAT(horario, '%H:%i') AS horario FROM tbagendamento WHERE id_tecnico =? AND data_agendamento = STR_TO_DATE(?, '%d/%m/%Y')";
    try {
        pst = conexao.prepareStatement(sql);
        pst.setString(1, txtAgIdTec.getText());
        pst.setString(2, txtAgData.getText());
        rs = pst.executeQuery();
        while (rs.next()) {
            horariosAgendados.add(rs.getString("horario"));
        }
    } catch (SQLException e) {
        // tratar erro
    }
    return horariosAgendados;
}
     private List<String> getHorariosDisponiveis() {
    List<String> horariosDisponiveis = new ArrayList<>();
    for (int i = 9; i < 18; i++) {
        horariosDisponiveis.add(String.format("%02d:00", i));
    }
    List<String> horariosAgendados = getHorariosAgendados();
    horariosDisponiveis.removeAll(horariosAgendados);
    return horariosDisponiveis;
}
     
     void preencherHorariosDisponiveis() {
    List<String> horariosDisponiveis = getHorariosDisponiveis();
    DefaultListModel<String> model = new DefaultListModel<>();
    horariosDisponiveis.forEach((horario) -> {
        model.addElement(horario);
        });
    listHor.setModel(model);
}

    void lista_padrao() {
      
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("09:00");
        model.addElement("10:00");
        model.addElement("11:00");
        model.addElement("12:00");
        model.addElement("13:00");
        model.addElement("14:00");
        model.addElement("15:00");
        model.addElement("16:00");
        model.addElement("17:00");
        listHor.setModel(model);
    }

    private void setar() {
        int setar = tblOs.getSelectedRow();
        txtAgOs.setText(tblOs.getModel().getValueAt(setar, 0).toString());
        txtAgCli.setText(tblOs.getModel().getValueAt(setar, 1).toString());
        txtAgTec.setText(tblOs.getModel().getValueAt(setar, 2).toString());
        txtAgIdTec.setText(tblOs.getModel().getValueAt(setar, 3).toString());
        txtAgIdCli.setText(tblOs.getModel().getValueAt(setar, 4).toString());

    }
    
    private void setar_ag(){
        int setar = listHor.getSelectedIndex();
        txtAgHour.setText(listHor.getModel().getElementAt(setar));
    }


    private List<String> dataAtual() {
        List<String> datas = new ArrayList<>();
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 0; i < 14; i++) {
            datas.add(dataAtual.plusDays(i).format(formatter));
        }
        return datas;
    }

    void preencher() {
        List<String> datas = dataAtual();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(datas.toArray(new String[0]));
        cboData.setModel(model);
    }
     private void excluir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este agendamento?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbagendamento where id_agendamento = ?";
            try {
                pst = conexao.prepareStatement(sql);
                 pst.setString(1, txtAgId.getText());
                String idAg = txtAgId.getText();
                int apagado = pst.executeUpdate();
                if (apagado > 0 && !idAg.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Os Excluída com sucesso!");
                    LimparDados();
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao excluir OS. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    //public List<LocalDateTime> buscaDisponibilidade()
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtAgData = new javax.swing.JTextField();
        cboData = new javax.swing.JComboBox<>();
        txtAgHour = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        listHor = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        btnAgendar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtAgCli = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblOs = new javax.swing.JTable();
        txtOsPesq = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtAgOs = new javax.swing.JTextField();
        txtAgTec = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAgIdCli = new javax.swing.JTextField();
        txtAgIdTec = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblAg = new javax.swing.JTable();
        txtAgPesq = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtAgId = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtAgDataAgendado = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtAgHourAgendado = new javax.swing.JTextField();
        btnExcluir = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Agendamentos");

        jPanel1.setToolTipText("");

        txtAgData.setEditable(false);

        cboData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboDataMouseClicked(evt);
            }
        });
        cboData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDataActionPerformed(evt);
            }
        });
        cboData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboDataKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboDataKeyReleased(evt);
            }
        });

        txtAgHour.setEditable(false);

        jLabel20.setText("*Hora:");

        listHor.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listHor.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listHor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listHorMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(listHor);

        jButton1.setText("Pesquisar");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel22.setText("*Data");

        btnAgendar.setText("Agendar");
        btnAgendar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(cboData, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtAgHour, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtAgData)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAgendar))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jButton1)))
                .addGap(44, 44, 44))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(cboData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtAgData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAgHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgendar)
                        .addGap(43, 43, 43))))
        );

        txtAgCli.setEditable(false);

        tblOs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "OS", "Cliente", "Técnico", "id_cli", "id_tec"
            }
        ));
        tblOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOsMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblOs);
        if (tblOs.getColumnModel().getColumnCount() > 0) {
            tblOs.getColumnModel().getColumn(2).setResizable(false);
            tblOs.getColumnModel().getColumn(3).setHeaderValue("id_cli");
            tblOs.getColumnModel().getColumn(4).setHeaderValue("id_tec");
        }

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

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/pesquisar.png"))); // NOI18N

        jLabel12.setText("Cliente");

        jLabel25.setText("Pesquisar OS:");

        jLabel2.setText("OS");

        txtAgOs.setEditable(false);
        txtAgOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgOsActionPerformed(evt);
            }
        });

        txtAgTec.setEditable(false);

        jLabel13.setText("Técnico");

        jLabel1.setText("*Cód_Cli");

        jLabel3.setText("*Cód_Téc");

        txtAgIdCli.setEditable(false);
        txtAgIdCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgIdCliActionPerformed(evt);
            }
        });

        txtAgIdTec.setEditable(false);
        txtAgIdTec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgIdTecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAgCli, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addGap(13, 13, 13)
                        .addComponent(txtAgTec, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtOsPesq, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAgOs, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAgIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAgIdTec, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(txtOsPesq, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtAgOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtAgIdTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtAgIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAgTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtAgCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/pesquisar.png"))); // NOI18N

        tblAg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Cód_Agendamento", "Data", "Horário"
            }
        ));
        tblAg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAgMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblAg);
        if (tblAg.getColumnModel().getColumnCount() > 0) {
            tblAg.getColumnModel().getColumn(2).setResizable(false);
        }

        txtAgPesq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtAgPesqMouseReleased(evt);
            }
        });
        txtAgPesq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgPesqActionPerformed(evt);
            }
        });
        txtAgPesq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAgPesqKeyReleased(evt);
            }
        });

        jLabel26.setText("Pesquisar Agendamento:");

        txtAgId.setEditable(false);
        txtAgId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgIdActionPerformed(evt);
            }
        });

        jLabel21.setText("*Cód_Ag");

        txtAgDataAgendado.setEditable(false);

        jLabel23.setText("Data");

        jLabel24.setText("Hora:");

        txtAgHourAgendado.setEditable(false);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtAgPesq, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15)
                                .addGap(61, 61, 61)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAgId, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAgDataAgendado, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAgHourAgendado, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel26)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15)
                            .addComponent(txtAgPesq, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAgHourAgendado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(txtAgDataAgendado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(txtAgId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImprimir))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        setBounds(0, 0, 912, 623);
    }// </editor-fold>//GEN-END:initComponents

    private void txtOsPesqMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOsPesqMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsPesqMouseReleased

    private void txtOsPesqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsPesqActionPerformed
        //pesquisar();
    }//GEN-LAST:event_txtOsPesqActionPerformed

    private void txtOsPesqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsPesqKeyReleased
        pesquisar_os();
    }//GEN-LAST:event_txtOsPesqKeyReleased

    private void txtAgOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgOsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgOsActionPerformed

    private void txtAgIdCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgIdCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgIdCliActionPerformed

    private void cboDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboDataActionPerformed

    private void tblOsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOsMouseClicked
        setar();
        preencher();
    }//GEN-LAST:event_tblOsMouseClicked

    private void listHorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listHorMouseClicked
        setar_ag();
    }//GEN-LAST:event_listHorMouseClicked

    private void cboDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboDataMouseClicked
        
    }//GEN-LAST:event_cboDataMouseClicked

    private void cboDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboDataKeyPressed
       
    }//GEN-LAST:event_cboDataKeyPressed

    private void cboDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboDataKeyReleased
         
    }//GEN-LAST:event_cboDataKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setar_campos();
        preencherHorariosDisponiveis();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblAgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgMouseClicked
        setar_agendamento();
    }//GEN-LAST:event_tblAgMouseClicked

    private void txtAgPesqMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAgPesqMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgPesqMouseReleased

    private void txtAgPesqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgPesqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgPesqActionPerformed

    private void txtAgPesqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAgPesqKeyReleased
        pesquisar_ag();
    }//GEN-LAST:event_txtAgPesqKeyReleased

    private void txtAgIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgIdActionPerformed

    private void txtAgIdTecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgIdTecActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgIdTecActionPerformed

    private void btnAgendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendarActionPerformed
        try {
            agendar();
        } catch (SQLException ex) {
            Logger.getLogger(TelaAgendamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgendarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
       String idAg = txtAgId.getText();
       if(!idAg.isEmpty()){
            try {
                Map<String, Object> parametros = new HashMap<>();
                
                parametros.put("idAg", Integer.parseInt(idAg));
           
                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\agendamento.jasper");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexao);
                JasperViewer.viewReport(print,false);
           }
             catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar o Agendamento. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
                 System.err.println(e);
            }}
            else{
               JOptionPane.showMessageDialog(null, "Id do agendamento não informado");
           }
    }//GEN-LAST:event_btnImprimirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgendar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JComboBox<String> cboData;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JList<String> listHor;
    private javax.swing.JTable tblAg;
    private javax.swing.JTable tblOs;
    private javax.swing.JTextField txtAgCli;
    private javax.swing.JTextField txtAgData;
    private javax.swing.JTextField txtAgDataAgendado;
    private javax.swing.JTextField txtAgHour;
    private javax.swing.JTextField txtAgHourAgendado;
    private javax.swing.JTextField txtAgId;
    private javax.swing.JTextField txtAgIdCli;
    private javax.swing.JTextField txtAgIdTec;
    private javax.swing.JTextField txtAgOs;
    private javax.swing.JTextField txtAgPesq;
    private javax.swing.JTextField txtAgTec;
    private javax.swing.JTextField txtOsPesq;
    // End of variables declaration//GEN-END:variables

    
}
