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
import java.util.Calendar;
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
public class AgregarMontoApartado extends javax.swing.JFrame {

    conexion c = new conexion();
    Connection con = c.getConexion();

    String PrimerTicket, FechadelTicket;
    String Cabezera1, Cabezera2, Cabezera3, Cabezera4, Cabezera5, Pie1, Pie2, Pie3, Pie4, Pie5;

    double Monto, Restante, RestanteAnterior, Total, SumaAbonos;

    int numeroApartadosTotales;

    public static DefaultTableModel modeloArticulos;
    public static String tituloArticulos[] = {"Codigo", "Tipo", "Material", "Medida", "Precio"};
    public static String dataArticulos[] = new String[5];

    public static DefaultTableModel modeloMontos;
    public static String tituloMontos[] = {"Ticket", "Fecha", "Total", "Abono", "Resta"};
    public static String dataMontos[] = new String[5];

    /**
     * Creates new form AgregarMontoApartado
     */
    public AgregarMontoApartado() {
        initComponents();
        modeloMontos = new DefaultTableModel(null, tituloMontos) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        modeloArticulos = new DefaultTableModel(null, tituloArticulos) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        LblCliente.setText(Principal.LblNombreCliente.getText());
        LblCodigo.setText(Principal.TxtCodigoCliente.getText());

    }

    void ConultarApartado() {
        TblArticulos.setModel(modeloArticulos);
        TblMontos.setModel(modeloMontos);
        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = con.prepareStatement("SELECT * from apartados WHERE num_ticket like '%" + TxtNumTicket.getText() + "%'");
            rs = ps.executeQuery();
            while (rs.next()) {

                PrimerTicket = rs.getString("primer_ticket");
                dataArticulos[0] = rs.getString("codigo_producto");
                dataArticulos[1] = rs.getString("tipo_producto");
                dataArticulos[2] = rs.getString("material_producto");
                dataArticulos[3] = rs.getString("medida_producto");
                dataArticulos[4] = rs.getString("precio_producto");
                FechadelTicket = rs.getString("fecha");
                modeloArticulos.addRow(dataArticulos);
            }

        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, "Error en la consulta Numero de Ticket: "+ex);
        }
        try {
            ps = con.prepareStatement("SELECT * from apartados  WHERE primer_ticket like '%" + PrimerTicket + "%'  GROUP BY num_ticket  HAVING COUNT(*) > 0 ORDER BY fecha DESC");
            rs = ps.executeQuery();
            while (rs.next()) {

                dataMontos[0] = rs.getString("num_ticket");
                dataMontos[1] = rs.getString("fecha");
                dataMontos[2] = rs.getString("Total");
                dataMontos[3] = rs.getString("Monto");
                dataMontos[4] = rs.getString("Resta");
                modeloMontos.addRow(dataMontos);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en la consulta Numero de Ticket Principal: "+ex);
        }
        AgregarAbono();
    }

    void AgregarAbono() {
        if ("".equals(TxtMonto.getText())) {
            Monto = 0;
        } else {
            Monto = Double.parseDouble(TxtMonto.getText());
        }

        for (int i = 0; i < TblMontos.getRowCount(); i++) {
            SumaAbonos = SumaAbonos + Double.parseDouble(TblMontos.getValueAt(i, 3).toString());
        }
        for (int i = 0; i < TblArticulos.getRowCount(); i++) {
            Total = Total + Double.parseDouble(TblArticulos.getValueAt(i, 4).toString());
        }
        LblTotal.setText(String.valueOf(Total));
        RestanteAnterior = Total - SumaAbonos;
        lblRestanteAnterior.setText(String.valueOf(RestanteAnterior));
        Restante = RestanteAnterior - Monto;
        if (Restante <= 0) {
            LblResta.setText("0.00");
        } else {
            LblResta.setText(String.valueOf(Restante));
        }
        LblFecha.setText(FechadelTicket);
        Restante = 0;
        Total = 0;
        SumaAbonos = 0;
    }

    void ConsultaCantidadApartados() {
        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = con.prepareStatement("SELECT count(*) AS id FROM apartados");
            rs = ps.executeQuery();
            if (rs.next()) {
                numeroApartadosTotales = Integer.parseInt(rs.getString("id"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ApartarArticulo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parteticket() {

        String sql = "select * from partesticket WHERE id=3";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Cabezera1 = rs.getString("cabezera1");
                Cabezera2 = rs.getString("cabezera2");
                Cabezera3 = rs.getString("cabezera3");
                Cabezera4 = rs.getString("cabezera4");
                Cabezera5 = rs.getString("cabezera5");
                Pie1 = rs.getString("pie1");
                Pie2 = rs.getString("pie2");
                Pie3 = rs.getString("pie3");
                Pie4 = rs.getString("pie4");
                Pie5 = rs.getString("pie5");

            }

        } catch (SQLException e) {
            System.out.println("Error de consulta: " + e.getMessage());
        }

    }

    String fecha() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY");
        return formatoFecha.format(fecha);
    }

    String mes() {
        Date mes = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("MM");
        return formatoFecha.format(mes);
    }

    void ticket() {
        parteticket();
        ConsultaCantidadApartados();

        Ticket.AddCabecera(Cabezera1);

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Cabezera2);

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Cabezera3);

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Cabezera4);

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Cabezera5);

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("FECHA: " + fecha());

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Cliente: " + LblCliente.getText() + " " + LblCodigo.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());

        Ticket.AddCabecera("Atiende: ");
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Num. Ticket: " + LblCodigo.getText() + "" + numeroApartadosTotales);

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Codi| Tipo| Medi| Mate| Precio");
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));

        for (int i = 0; i <= TblArticulos.getRowCount() - 1; i++) {

            Ticket.AddCabecera("\n" + TblArticulos.getValueAt(i, 0).toString()
                    + " " + TblArticulos.getValueAt(i, 1).toString()
                    + " " + TblArticulos.getValueAt(i, 2).toString()
                    + "  " + TblArticulos.getValueAt(i, 3).toString()
                    + "  " + TblArticulos.getValueAt(i, 4).toString());

            try {
                PreparedStatement pps;
                pps = con.prepareStatement("INSERT INTO apartados (codigo_cliente,nombre_cliente,num_ticket,primer_ticket,codigo_producto,tipo_producto,material_producto,medida_producto,precio_producto,Total,Monto,Resta,fecha,cortecaja) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pps.setString(1, LblCodigo.getText());
                pps.setString(2, LblCliente.getText());
                pps.setString(3, LblCodigo.getText() + "" + numeroApartadosTotales);
                pps.setString(4, PrimerTicket);
                pps.setString(5, TblArticulos.getValueAt(i, 0).toString());
                pps.setString(6, TblArticulos.getValueAt(i, 1).toString());
                pps.setString(7, TblArticulos.getValueAt(i, 2).toString());
                pps.setString(8, TblArticulos.getValueAt(i, 3).toString());
                pps.setString(9, TblArticulos.getValueAt(i, 4).toString());
                pps.setString(10, lblRestanteAnterior.getText());
                pps.setString(11, TxtMonto.getText());
                pps.setString(12, LblResta.getText());
                pps.setString(13, fecha());
                pps.setBoolean(14, false);
                pps.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger("No se pudo cargar la base de datos: " + ImprimirCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Aticulos:" + TblArticulos.getRowCount());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Restante Aterior: $" + lblRestanteAnterior.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Abono: $" + TxtMonto.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Resta: $" + LblResta.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Pie1);
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Pie2);
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Pie3);
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Pie4);
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Pie5);

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.ImprimirDocumento();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TxtNumTicket = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        LblCliente = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        LblCodigo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblMontos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        TxtMonto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        LblResta = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TblArticulos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        LblTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblRestanteAnterior = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        LblFecha = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Agregar Abono a un Articulo");

        TxtNumTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNumTicketActionPerformed(evt);
            }
        });
        TxtNumTicket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtNumTicketKeyTyped(evt);
            }
        });

        jLabel1.setText("Numero de Ticket:");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos dle Cliente"));

        jLabel2.setText("Cliente:");

        LblCliente.setText("jLabel3");

        jLabel4.setText("Codigo:");

        LblCodigo.setText("jLabel5");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LblCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(LblCliente)
                    .addComponent(jLabel4)
                    .addComponent(LblCodigo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Montos Agregados"));

        TblMontos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ticket", "Fecha", "Total", "Monto", "Resta"
            }
        ));
        jScrollPane1.setViewportView(TblMontos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setText("Abono:");

        TxtMonto.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                TxtMontoCaretUpdate(evt);
            }
        });
        TxtMonto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtMontoKeyTyped(evt);
            }
        });

        jLabel7.setText("Restante:");

        LblResta.setText("0.00");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/boton-redondo-agregar.png"))); // NOI18N
        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Articulos"));

        TblArticulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Tipo", "Material", "Medida", "Precio"
            }
        ));
        jScrollPane2.setViewportView(TblArticulos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel3.setText("Total:");

        LblTotal.setText("0.00");

        jLabel8.setText("Restante Anterior:");

        lblRestanteAnterior.setText("0.00");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/cancelar.png"))); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText("Fecha:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TxtNumTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addComponent(LblFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(TxtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblRestanteAnterior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(LblResta, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(TxtNumTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(LblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(2, 2, 2)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(LblResta)
                    .addComponent(jButton1)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3)
                    .addComponent(LblTotal)
                    .addComponent(jLabel8)
                    .addComponent(lblRestanteAnterior))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtNumTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNumTicketActionPerformed
        ConultarApartado();
    }//GEN-LAST:event_TxtNumTicketActionPerformed

    private void TxtMontoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtMontoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car > '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_TxtMontoKeyTyped

    private void TxtMontoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_TxtMontoCaretUpdate
        AgregarAbono();
    }//GEN-LAST:event_TxtMontoCaretUpdate

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SimpleDateFormat FechaDato = new SimpleDateFormat("dd/MM/yyyy");
        Date FechaTicketADate = null;
        Date FechaActualEnDate = new Date();
        String FechaActual = FechaDato.format(FechaActualEnDate);
        String[] PartesFechaActual = FechaActual.split("/");
        int DiaActual = Integer.parseInt(PartesFechaActual[0]);
        int MesActual = Integer.parseInt(PartesFechaActual[1]);
        try {
            FechaTicketADate = FechaDato.parse(FechadelTicket);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la Funcion Fecha: "+e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(FechaTicketADate);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        String FechaLimite = FechaDato.format(calendar.getTime());
        String[] PartesFechaLimite = FechaLimite.split("/");
        int DiaLimite = Integer.parseInt(PartesFechaLimite[0]);
        int MesLimite = Integer.parseInt(PartesFechaLimite[1]);
        if (MesActual <= MesLimite) {
            if (DiaActual <= DiaLimite) {
                ticket();
                Principal ventaPrincipal = new Principal();
                ventaPrincipal.setLocationRelativeTo(null);
                ventaPrincipal.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "La fecha del ticket ya no es valido");
            }
        } else {
            JOptionPane.showMessageDialog(this, "La fecha del ticket ya no es valido");
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Principal ventaPrincipal = new Principal();
        ventaPrincipal.setLocationRelativeTo(null);
        ventaPrincipal.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void TxtNumTicketKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtNumTicketKeyTyped
            char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car > '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_TxtNumTicketKeyTyped

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
            java.util.logging.Logger.getLogger(AgregarMontoApartado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgregarMontoApartado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgregarMontoApartado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgregarMontoApartado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AgregarMontoApartado().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LblCliente;
    private javax.swing.JLabel LblCodigo;
    private javax.swing.JLabel LblFecha;
    private javax.swing.JLabel LblResta;
    private javax.swing.JLabel LblTotal;
    private javax.swing.JTable TblArticulos;
    private javax.swing.JTable TblMontos;
    private javax.swing.JTextField TxtMonto;
    private javax.swing.JTextField TxtNumTicket;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblRestanteAnterior;
    // End of variables declaration//GEN-END:variables

}
