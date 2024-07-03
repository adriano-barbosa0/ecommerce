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
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.Timer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Manipulação da tela principal
 * @author Adriano Barbosa
 * @version 1.1
 */
public class TelaPrincipal extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    //private JLabel lblHora;
    public TelaPrincipal() {
        initComponents();
        conexao = ModuloConexao.conector();
       
    }
    void atualizarHorario(){
        Timer timer = new Timer(1000, (ActionEvent e) -> {
           Date time = new Date();
           DateFormat formatador1 = DateFormat.getTimeInstance(DateFormat.DEFAULT);
           lblHora.setText(formatador1.format(time));
        }); timer.start();
    }
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Desktop = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        Menu = new javax.swing.JMenuBar();
        menCad = new javax.swing.JMenu();
        menCadCli = new javax.swing.JMenuItem();
        menCadOS = new javax.swing.JMenuItem();
        menCadUser = new javax.swing.JMenuItem();
        menCadCli1 = new javax.swing.JMenuItem();
        menCadEmp = new javax.swing.JMenuItem();
        menCadOS1 = new javax.swing.JMenuItem();
        menRel = new javax.swing.JMenu();
        menRelCli = new javax.swing.JMenuItem();
        menRelServ = new javax.swing.JMenuItem();
        menRelMen = new javax.swing.JMenuItem();
        menRelAn = new javax.swing.JMenuItem();
        menAg = new javax.swing.JMenu();
        menAgHorA = new javax.swing.JMenuItem();
        menAgHorD = new javax.swing.JMenuItem();
        menAgHorD1 = new javax.swing.JMenuItem();
        menAgHorD2 = new javax.swing.JMenuItem();
        menAjud = new javax.swing.JMenu();
        menAjudSob = new javax.swing.JMenuItem();
        menOp = new javax.swing.JMenu();
        menOpSair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SVFLEX - Sistema de Gestão");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        javax.swing.GroupLayout DesktopLayout = new javax.swing.GroupLayout(Desktop);
        Desktop.setLayout(DesktopLayout);
        DesktopLayout.setHorizontalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 913, Short.MAX_VALUE)
        );
        DesktopLayout.setVerticalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 623, Short.MAX_VALUE)
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/svflex/icones/icon_sv-removebg-preview__2_-removebg-preview.png"))); // NOI18N

        lblData.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        lblData.setText("Data");

        lblUsuario.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        lblUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsuario.setText("Usuário");

        lblHora.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        lblHora.setText("Hora");

        menCad.setText("Cadastro");

        menCadCli.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        menCadCli.setText("Clientes");
        menCadCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadCliActionPerformed(evt);
            }
        });
        menCad.add(menCadCli);

        menCadOS.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        menCadOS.setText("OS");
        menCadOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadOSActionPerformed(evt);
            }
        });
        menCad.add(menCadOS);

        menCadUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK));
        menCadUser.setText("Usuários");
        menCadUser.setEnabled(false);
        menCadUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadUserActionPerformed(evt);
            }
        });
        menCad.add(menCadUser);

        menCadCli1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        menCadCli1.setText("Técnicos");
        menCadCli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadCli1ActionPerformed(evt);
            }
        });
        menCad.add(menCadCli1);

        menCadEmp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        menCadEmp.setText("Empresa");
        menCadEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadEmpActionPerformed(evt);
            }
        });
        menCad.add(menCadEmp);

        menCadOS1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        menCadOS1.setText("Nota");
        menCadOS1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadOS1ActionPerformed(evt);
            }
        });
        menCad.add(menCadOS1);

        Menu.add(menCad);

        menRel.setText("Relatório");
        menRel.setEnabled(false);

        menRelCli.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        menRelCli.setText("Clientes");
        menRelCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelCliActionPerformed(evt);
            }
        });
        menRel.add(menRelCli);

        menRelServ.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        menRelServ.setText("Serviços");
        menRelServ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelServActionPerformed(evt);
            }
        });
        menRel.add(menRelServ);

        menRelMen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        menRelMen.setText("Relatório Mensal");
        menRelMen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelMenActionPerformed(evt);
            }
        });
        menRel.add(menRelMen);

        menRelAn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.ALT_MASK));
        menRelAn.setText("Relatório Anual");
        menRelAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelAnActionPerformed(evt);
            }
        });
        menRel.add(menRelAn);

        Menu.add(menRel);

        menAg.setText("Agenda");

        menAgHorA.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        menAgHorA.setText("Horário Agendado");
        menAgHorA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menAgHorAActionPerformed(evt);
            }
        });
        menAg.add(menAgHorA);

        menAgHorD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        menAgHorD.setText("Agenda Mensal");
        menAgHorD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menAgHorDActionPerformed(evt);
            }
        });
        menAg.add(menAgHorD);

        menAgHorD1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.InputEvent.ALT_MASK));
        menAgHorD1.setText("Agenda Semanal");
        menAgHorD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menAgHorD1ActionPerformed(evt);
            }
        });
        menAg.add(menAgHorD1);

        menAgHorD2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.InputEvent.ALT_MASK));
        menAgHorD2.setText("Agenda Anual");
        menAgHorD2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menAgHorD2ActionPerformed(evt);
            }
        });
        menAg.add(menAgHorD2);

        Menu.add(menAg);

        menAjud.setText("Ajuda");

        menAjudSob.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK));
        menAjudSob.setText("Sobre");
        menAjudSob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menAjudSobActionPerformed(evt);
            }
        });
        menAjud.add(menAjudSob);

        Menu.add(menAjud);

        menOp.setText("Opções");

        menOpSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        menOpSair.setText("Sair");
        menOpSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOpSairActionPerformed(evt);
            }
        });
        menOp.add(menOpSair);

        Menu.add(menOp);

        setJMenuBar(Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Desktop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblData)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblHora)
                        .addGap(34, 34, 34))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Desktop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHora)
                    .addComponent(lblData))
                .addGap(27, 27, 27))
        );

        setSize(new java.awt.Dimension(1214, 680));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menCadCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadCliActionPerformed
        TelaCliente cliente = new TelaCliente();
        cliente.setVisible(true);
        Desktop.add(cliente);
    }//GEN-LAST:event_menCadCliActionPerformed

    private void menCadOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadOSActionPerformed
        TelaOS os = new TelaOS();
        os.setVisible(true);
        Desktop.add(os);
    }//GEN-LAST:event_menCadOSActionPerformed

    private void menCadUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadUserActionPerformed
        TelaUsuario usuario = new TelaUsuario();
        usuario.setVisible(true);
        Desktop.add(usuario);
    }//GEN-LAST:event_menCadUserActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblData.setText(formatador.format(data));
        
        /*Date time = new Date();
        DateFormat formatadorr = DateFormat.getTimeInstance(DateFormat.DEFAULT);
        lblHora.setText(formatadorr.format(time));
        */
        //atualizarHorario();

    }//GEN-LAST:event_formWindowActivated

    private void menOpSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOpSairActionPerformed
        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(sair == JOptionPane.YES_OPTION){
            System.exit(0);
        } else{
            int cancel = JOptionPane.OK_CANCEL_OPTION;
        }
      
    }//GEN-LAST:event_menOpSairActionPerformed

    private void menAjudSobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menAjudSobActionPerformed
        TelaSobre sobre = new TelaSobre();
        sobre.setVisible(true);
      
    }//GEN-LAST:event_menAjudSobActionPerformed

    private void menCadCli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadCli1ActionPerformed
        TelaTecnico tec = new TelaTecnico();
        tec.setVisible(true);
        Desktop.add(tec);
    }//GEN-LAST:event_menCadCli1ActionPerformed

    private void menRelCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelCliActionPerformed
        int confirma = JOptionPane.showConfirmDialog(null, "Emitir relatório de clientes?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            try {
                JasperPrint print = JasperFillManager.fillReport("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\clientes.jasper", null, conexao);
                JasperViewer.viewReport(print,false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar relatório de clientes. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_menRelCliActionPerformed

    private void menRelServActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelServActionPerformed
        int confirma = JOptionPane.showConfirmDialog(null, "Emitir relatório de serviços?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            try {
                JasperPrint print = JasperFillManager.fillReport("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\servicos.jasper", null, conexao);
                JasperViewer.viewReport(print,false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar relatório de serviços. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_menRelServActionPerformed

    private void menRelMenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelMenActionPerformed
       // mes();
       Map<String, Object> parametros = new HashMap<>();
       String anoStr = JOptionPane.showInputDialog(null, "Digite o ano (YYYY):");
       int ano = Integer.parseInt(anoStr);
       String mesStr = JOptionPane.showInputDialog(null, "Digite o mês (1-12):");
       int mes = Integer.parseInt(mesStr);
       parametros.put("ano", ano);
       parametros.put("mes", mes);
       try {
                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\servicos_mensal.jasper");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexao);
                JasperViewer.viewReport(print,false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar relatório mensal. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_menRelMenActionPerformed

    private void menRelAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelAnActionPerformed
         Map<String, Object> parametros = new HashMap<>();
       String anoStr = JOptionPane.showInputDialog(null, "Digite o ano (YYYY):");
       int ano = Integer.parseInt(anoStr);
       parametros.put("ano", ano);
       try {
                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\servico_anual.jasper");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexao);
                JasperViewer.viewReport(print,false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar relatório anual. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_menRelAnActionPerformed

    private void menAgHorAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menAgHorAActionPerformed
        TelaAgendamento agendamento = new TelaAgendamento();
        agendamento.setVisible(true);
        Desktop.add(agendamento);
        agendamento.preencher();
        agendamento.lista_padrao();
       // agendamento.horPreencher();
    }//GEN-LAST:event_menAgHorAActionPerformed

    private void menAgHorDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menAgHorDActionPerformed
         // mes();
       Map<String, Object> parametros = new HashMap<>();
       String anoStr = JOptionPane.showInputDialog(null, "Digite o ano (YYYY):");
       int ano = Integer.parseInt(anoStr);
       String mesStr = JOptionPane.showInputDialog(null, "Digite o mês (1-12):");
       int mes = Integer.parseInt(mesStr);
       parametros.put("ano", ano);
       parametros.put("mes", mes);
       try {
                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\agendamento_mensal.jasper");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexao);
                JasperViewer.viewReport(print,false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar relatório de agendamento mensal. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
    
    }//GEN-LAST:event_menAgHorDActionPerformed

    private void menAgHorD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menAgHorD1ActionPerformed
Map<String, Object> parametros = new HashMap<>();
String dataStr = JOptionPane.showInputDialog(null, "Digite a data (dd/MM/yyyy):");
if (!dataStr.contains("/")) {
    dataStr = dataStr.substring(0, 2) + "/" + dataStr.substring(2, 4) + "/" + dataStr.substring(4);
}
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
LocalDate data = LocalDate.parse(dataStr, formatter);

DateTimeFormatter formatterBanco = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String dataBanco = data.atStartOfDay().format(formatterBanco);

parametros.put("data", dataBanco);

try {
    JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\agendamento_semanal.jasper");
    JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexao);
    JasperViewer.viewReport(print,false);
} catch (JRException e) {
    JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar relatório anual. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
}
    }//GEN-LAST:event_menAgHorD1ActionPerformed

    private void menAgHorD2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menAgHorD2ActionPerformed
         Map<String, Object> parametros = new HashMap<>();
       String anoStr = JOptionPane.showInputDialog(null, "Digite o ano (YYYY):");
       int ano = Integer.parseInt(anoStr);
       parametros.put("ano", ano);
       try {
                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Nova\\Downloads\\Developer\\dbsvflex\\reports\\agendamento_anual.jasper");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, conexao);
                JasperViewer.viewReport(print,false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gerar relatório anual de agendamento. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_menAgHorD2ActionPerformed

    private void menCadEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadEmpActionPerformed
        TelaEmpresa empresa = new TelaEmpresa();
        empresa.setVisible(true);
        Desktop.add(empresa);
    }//GEN-LAST:event_menCadEmpActionPerformed

    private void menCadOS1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadOS1ActionPerformed
        TelaNota nota = new TelaNota();
        nota.setVisible(true);
        Desktop.add(nota);
    }//GEN-LAST:event_menCadOS1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane Desktop;
    private javax.swing.JMenuBar Menu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblHora;
    public static javax.swing.JLabel lblUsuario;
    private javax.swing.JMenu menAg;
    private javax.swing.JMenuItem menAgHorA;
    private javax.swing.JMenuItem menAgHorD;
    private javax.swing.JMenuItem menAgHorD1;
    private javax.swing.JMenuItem menAgHorD2;
    private javax.swing.JMenu menAjud;
    private javax.swing.JMenuItem menAjudSob;
    private javax.swing.JMenu menCad;
    private javax.swing.JMenuItem menCadCli;
    private javax.swing.JMenuItem menCadCli1;
    private javax.swing.JMenuItem menCadEmp;
    public static javax.swing.JMenuItem menCadOS;
    public static javax.swing.JMenuItem menCadOS1;
    public static javax.swing.JMenuItem menCadUser;
    private javax.swing.JMenu menOp;
    private javax.swing.JMenuItem menOpSair;
    public static javax.swing.JMenu menRel;
    private javax.swing.JMenuItem menRelAn;
    private javax.swing.JMenuItem menRelCli;
    private javax.swing.JMenuItem menRelMen;
    private javax.swing.JMenuItem menRelServ;
    // End of variables declaration//GEN-END:variables
}
