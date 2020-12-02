/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import ImprimirClases.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logica.conexion;

/**
 *
 * @author boy_d
 */
public final class CorteCaja extends javax.swing.JFrame {

    conexion c = new conexion();
    Connection con = c.getConexion();

    String fecha, ticket;

    String data[] = new String[1];
    String titulo[] = {"Ticket"};
    DefaultTableModel modelo = new DefaultTableModel(null, titulo);
    
    String dataCambios[] = new String[1];    
    String tituloCambios[] = {"Ticket"};
    DefaultTableModel modeloCambios = new DefaultTableModel(null, tituloCambios);
    
    String dataApartados[] = new String[1];
    String tituloApartados[] = {"Ticket"};
    DefaultTableModel modeloApartados = new DefaultTableModel(null, tituloApartados);

    double efectivo;
    double subtotal = 0, apartar, total;
    int ii = 0, pares = 0, apartados = 0;

    /**
     * Creates new form CorteCaja
     */
    public CorteCaja() {
        initComponents();
        this.setResizable(false);
        fecha = fecha();
        consultaventas();
        consultacambios();
        CulsultaApartados();
        this.setResizable(false);
        LblSubTotal.setText(Double.toString(subtotal));
        LblParesVendidos.setText(Integer.toString(pares));
        LblCambiosRealizados.setText(Integer.toString(ii));
        LblEfectivo.setText(Double.toString(efectivo));
        LblApartados.setText(Double.toString(apartar));
        LblNumApartados.setText(Integer.toString(apartados));

    }

    String fecha() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY");
        return formatoFecha.format(fecha);
    }

    public void consultaventas() {
        String sql = "SELECT *  FROM ventas WHERE fecha='" + fecha + "'  ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("corte_caja") != 1) {
                    efectivo = efectivo + rs.getDouble("precio_descuento_producto");
                    subtotal = subtotal + rs.getDouble("precio_descuento_producto");
                    data[0] = rs.getString("num_ticket");
                    modelo.addRow(data);
                    System.out.println("Venta"+efectivo);
                    System.out.println("Venta-"+subtotal);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error de consulta de ventas: " + e.getMessage());
        }

        String sql2 = "SELECT *  FROM ventas WHERE fecha='" + fecha + "' GROUP BY num_ticket HAVING COUNT(*) > 0 ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql2);
            while (rs.next()) {
                if (rs.getInt("corte_caja") != 1) {
                    pares++;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de consulta de Numero de ventas: " + e.getMessage());
        }
    }

    void CulsultaApartados() {

        String sql = "SELECT *  FROM apartados WHERE fecha='" + fecha + "' GROUP BY num_ticket HAVING COUNT(*) > 0 ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("cortecaja") != 1) {
                    apartar = apartar + rs.getDouble("Monto");
                    efectivo = efectivo + rs.getDouble("Monto");
                    dataApartados[0] = rs.getString("num_ticket");
                    modeloApartados.addRow(dataApartados);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de consulta de apartados: " + e.getMessage());
        }

        String sql2 = "SELECT *  FROM apartados WHERE fecha='" + fecha + "' GROUP BY num_ticket HAVING COUNT(*) > 0 ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql2);

            while (rs.next()) {
                if (rs.getInt("cortecaja") != 1) {
                    apartados++;
                }

            }
        } catch (SQLException e) {
            System.out.println("Error de consulta de Numero de apatartados: " + e.getMessage());
        }
    }

    public void consultacambios() {
        String sql = "SELECT *  FROM cambios WHERE FechaTicketAnterior='" + fecha + "'  ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("corte_caja") != 1) {
                    subtotal = subtotal + rs.getDouble("precio_descuento_producto");
                    efectivo = efectivo + rs.getDouble("precio_descuento_producto");
                    dataCambios[0] = rs.getString("num_ticket");
                    modeloCambios.addRow(dataCambios);
                    System.out.println("Cambio"+efectivo);
                    System.out.println("Cambio-"+subtotal);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de consulta de apartados: " + e.getMessage());
        }
        String sql2 = "select * from cambios where fecha='" + fecha + "' GROUP BY num_ticket HAVING COUNT(*) > 0";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql2);
            while (rs.next()) {
                if (rs.getInt("corte_caja") != 1) {
                ii++;
                }
            }

        } catch (Exception e) {
            System.out.println("Error de consulta de cambios: " + e.getMessage());
        }
    }

    public void ticketcortedecaja() {
        Ticket ticket = new Ticket();
        ticket.AddCabecera("CORTE DE CAJA");
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("FECHA: " + fecha());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Ventas: " + subtotal);
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Ingresos: " + TxtIngreso.getText());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Gastos: " + TxtGastos.getText());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Apartados: " + LblApartados.getText());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Efectivo: " + efectivo);
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Nmero Apartados: " + LblNumApartados.getText());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Pares Vendidos: " + LblParesVendidos.getText());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Cambios Realizados: " + LblCambiosRealizados.getText());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Sobrante: " + TxtSobrante.getText());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera("Inventario:");
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera(TxtInventario.getText());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.AddCabecera(ticket.DarEspacio());
        ticket.ImprimirDocumento();

    }
    
    void EjecutarAccion(){
        
        total = efectivo +(Double.parseDouble(TxtIngreso.getText())) - (Double.parseDouble(TxtGastos.getText()));
        LblEfectivo.setText(String.valueOf(total));
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TxtGastos = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        TxtIngreso = new javax.swing.JTextField();
        LblSubTotal = new javax.swing.JLabel();
        LblEfectivo = new javax.swing.JLabel();
        LblParesVendidos = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        LblCambiosRealizados = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TxtSobrante = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TxtInventario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        BtnCorteCaja = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        LblApartados = new javax.swing.JLabel();
        LblNumApartados = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        TxtGastos.setText("0");
        TxtGastos.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                TxtGastosCaretUpdate(evt);
            }
        });
        TxtGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtGastosActionPerformed(evt);
            }
        });
        TxtGastos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtGastosKeyTyped(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Sobrante:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Ingreso:");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Inventario:");

        TxtIngreso.setText("0");
        TxtIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtIngresoActionPerformed(evt);
            }
        });
        TxtIngreso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtIngresoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtIngresoKeyTyped(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Ventas:");

        jLabel2.setText("CORTE DE CAJA");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Gastos:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Efectivo:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/cancelar.png"))); // NOI18N
        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Ventas Realizadas:");

        BtnCorteCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/recepcion.png"))); // NOI18N
        BtnCorteCaja.setText("Imprimir Corte de Caja");
        BtnCorteCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCorteCajaActionPerformed(evt);
            }
        });

        jLabel6.setText("Cambios Realizados:");

        jLabel12.setText("Apartados:");

        jLabel13.setText("Numero Apartados:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(163, 163, 163)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 3, Short.MAX_VALUE)))
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TxtInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(LblNumApartados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(LblSubTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(TxtIngreso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                        .addComponent(TxtGastos, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(LblEfectivo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(LblParesVendidos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(LblCambiosRealizados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(TxtSobrante, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(LblApartados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnCorteCaja)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(LblSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(TxtGastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblApartados, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblEfectivo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblNumApartados, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(LblParesVendidos, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addComponent(LblCambiosRealizados, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(TxtSobrante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(TxtInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(BtnCorteCaja))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Principal pricicpal = new Principal();
        pricicpal.setLocationRelativeTo(null);
        pricicpal.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void BtnCorteCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCorteCajaActionPerformed
        if ("".equals(TxtGastos.getText())) {
            TxtGastos.setText("0");
        }
        if ("".equals(TxtSobrante.getText())) {
            TxtSobrante.setText(" ");
        }
        if ("".equals(TxtInventario.getText())) {
            JOptionPane.showMessageDialog(this, "Ingrese datos en Inventario");
        } else {
            try {
                for (int j = 0; j < modelo.getRowCount(); j++) {
                    PreparedStatement ps = con.prepareStatement("UPDATE ventas SET corte_caja='" + 1
                            + "' WHERE num_ticket='" + modelo.getValueAt(j, 0) + "' ");

                    ps.executeUpdate();
                }
                for (int j = 0; j < modeloApartados.getRowCount(); j++) {
                    PreparedStatement ps = con.prepareStatement("UPDATE apartados SET cortecaja='" + 1
                            + "' WHERE num_ticket='" + modeloApartados.getValueAt(j, 0) + "' ");

                    ps.executeUpdate();
                }
                for (int j = 0; j < modeloCambios.getRowCount(); j++) {
                    PreparedStatement ps = con.prepareStatement("UPDATE cambios SET corte_caja='" + 1
                            + "' WHERE num_ticket='" + modeloCambios.getValueAt(j, 0) + "' ");

                    ps.executeUpdate();
                }
            } catch (Exception e) {
                Logger.getLogger(CorteCaja.class.getName()).log(Level.SEVERE, null, e);
            }
            try {

                PreparedStatement pps = con.prepareStatement("INSERT INTO cortecaja(subtotal,ingresos,gastos,apartados,efectivo,NumApartados,paresVendidos,cambiosRealizados,sobrante,inventario,fecha) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                pps.setDouble(1, Double.parseDouble(LblSubTotal.getText()));
                pps.setDouble(2, Double.parseDouble(TxtIngreso.getText()));
                pps.setDouble(3, Double.parseDouble(TxtGastos.getText()));
                pps.setDouble(4, Double.parseDouble(LblApartados.getText()));
                pps.setDouble(5, Double.parseDouble(LblEfectivo.getText()));
                pps.setInt(6, Integer.parseInt(LblNumApartados.getText()));
                pps.setInt(7, Integer.parseInt(LblParesVendidos.getText()));
                pps.setInt(8, Integer.parseInt(LblCambiosRealizados.getText()));
                pps.setString(9, TxtSobrante.getText());
                pps.setString(10, TxtInventario.getText());
                pps.setString(11, fecha);

                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos Guardados Exitosamente.");

            } catch (SQLException ex) {
                Logger.getLogger(CorteCaja.class.getName()).log(Level.SEVERE, null, ex);
            }

            ticketcortedecaja();

            Principal pricicpal = new Principal();
            pricicpal.setLocationRelativeTo(null);
            pricicpal.setVisible(true);
            dispose();

        }
    }//GEN-LAST:event_BtnCorteCajaActionPerformed

    private void TxtIngresoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtIngresoKeyPressed

    }//GEN-LAST:event_TxtIngresoKeyPressed

    private void TxtGastosCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_TxtGastosCaretUpdate

    }//GEN-LAST:event_TxtGastosCaretUpdate

    private void TxtIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtIngresoActionPerformed
        EjecutarAccion();
    }//GEN-LAST:event_TxtIngresoActionPerformed

    private void TxtGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtGastosActionPerformed
        EjecutarAccion();
    }//GEN-LAST:event_TxtGastosActionPerformed

    private void TxtIngresoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtIngresoKeyTyped
            char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car > '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_TxtIngresoKeyTyped

    private void TxtGastosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtGastosKeyTyped
            char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car > '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_TxtGastosKeyTyped

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
            java.util.logging.Logger.getLogger(CorteCaja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CorteCaja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CorteCaja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CorteCaja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CorteCaja().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCorteCaja;
    private javax.swing.JLabel LblApartados;
    private javax.swing.JLabel LblCambiosRealizados;
    private javax.swing.JLabel LblEfectivo;
    private javax.swing.JLabel LblNumApartados;
    private javax.swing.JLabel LblParesVendidos;
    private javax.swing.JLabel LblSubTotal;
    private javax.swing.JTextField TxtGastos;
    private javax.swing.JTextField TxtIngreso;
    private javax.swing.JTextField TxtInventario;
    private javax.swing.JTextField TxtSobrante;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables
}
