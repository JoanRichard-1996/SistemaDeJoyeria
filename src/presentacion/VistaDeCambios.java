/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;
 
import ImprimirClases.Ticket;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import logica.ImagenMySQL;
import logica.conexion;

/**
 *
 * @author boy_d
 */
public class VistaDeCambios extends javax.swing.JFrame {
    conexion c = new conexion();
    Connection con = c.getConexion();
    PreparedStatement pps;
    
    byte[] image = null;

    BufferedImage buffimg = null;

    InputStream img[] = new InputStream[1];
        String titulo[] = {"Codigo", "Tipo", "Medida", "Material", "Precio", "Precio Con Descuento"};
    String data[] = new String[6];
    DefaultTableModel modelo;
    int ii;
        String Cabezera1, Cabezera2, Cabezera3, Cabezera4, Cabezera5, Pie1, Pie2, Pie3, Pie4, Pie5, Cabezera11, Cabezera12, Cabezera13, Cabezera14, Cabezera15, Pie11, Pie12, Pie13, Pie14, Pie15;
    /**
     * Creates new form VistaDeCambios
     */
    public VistaDeCambios() {
        initComponents();
        consultararticulo();
        this.setResizable(false);
        parteticketCambio();
    }
    private void parteticketCambio() {

        String sql = "select * from partesticket WHERE id=2";
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

        } catch (Exception e) {
            System.out.println("Error de consulta: " + e.getMessage());
        }

    }
     void consultararticulo(){
          modelo = new DefaultTableModel(null, titulo) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }};
         int fila= ListaDeCambios.TblListaDeCambios.getSelectedRow();
         
        String sql = "SELECT * FROM cambios WHERE num_ticket='" + ListaDeCambios.TblListaDeCambios.getValueAt(fila, 2) + "'";
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    
                LblCodigoCliente.setText(rs.getString("codigo_cliente"));
                LblNombreCliente.setText(rs.getString("nombre_cliente"));
                LblTicket.setText(rs.getString("num_ticket")) ;
                LblFecha.setText(rs.getString("fecha"));
                LblMonto.setText(rs.getString("monto"));
                if(rs.getBoolean("valido") == false){
                LblValido.setText("No Cobrado");
                }else{
                 LblValido.setText("Cobrado");
                }
                data[0] = rs.getString("codigo_producto");
                data[1] = rs.getString("tipo_producto");
                data[2] = rs.getString("medida_producto");
                data[3] = rs.getString("material_producto");
                data[4] = rs.getString("precio_producto");
                data[5] = rs.getString("precio_descuento_producto");
                    
                modelo.addRow(data);
                TblVistaCambios.setModel(modelo);
                    
                    }
            } catch (SQLException e) {
                System.out.println("Error de consulta de busqueda: " + e.getMessage());
            }
            
    }
    void Imagen(){
        
        PreparedStatement ps;
        ResultSet rs;

        try {
            int fila2 = TblVistaCambios.getSelectedRow();
            ps = con.prepareStatement("SELECT * FROM inventario WHERE id=?");
            ps.setInt(1, Integer.parseInt(TblVistaCambios.getValueAt(fila2, 0).toString()));
            rs = ps.executeQuery();

            while (rs.next()) {

                InputStream img = rs.getBinaryStream("imagen"); // reading image as InputStream
                try { 
                    buffimg = ImageIO.read(img);
                    ImagenMySQL imagen = new ImagenMySQL(JPImagen.getHeight(), JPImagen.getWidth(), buffimg);
                    JPImagen.add(imagen);
                    JPImagen.repaint();
                } catch (IOException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            rs.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

 void ReimprimirTicket(){
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
        Ticket.AddCabecera("FECHA: " + LblFecha.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Ticket: " + LblTicket.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Cliente: " );
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Codigo: " + LblCodigoCliente.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Nombre: " + LblNombreCliente.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Monto: " + LblMonto.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Codi| Tipo| Medi| Mate| Precio| Descu");
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));

        if (TblVistaCambios.getRowCount() > 0) {
            for (ii = 0; ii <= TblVistaCambios.getRowCount() - 1; ii++) {
                Ticket.AddCabecera("\n" + TblVistaCambios.getValueAt(ii, 0).toString()
                        + " " + TblVistaCambios.getValueAt(ii, 1).toString()
                        + " " + TblVistaCambios.getValueAt(ii, 2).toString()
                        + "  " + TblVistaCambios.getValueAt(ii, 3).toString()
                        + "   " + TblVistaCambios.getValueAt(ii, 4).toString()
                        + "  " + TblVistaCambios.getValueAt(ii, 5).toString());
               
            }
        }
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        Ticket.AddCabecera(Ticket.DarEspacio());
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

        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblVistaCambios = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        LblCodigoCliente = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        JPImagen = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        LblNombreCliente = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        LblFecha = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        LblTicket = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        LblValido = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        LblMonto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Nombre Cliente:");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Articulos de Cambio"));

        TblVistaCambios.setModel(new javax.swing.table.DefaultTableModel(
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
        TblVistaCambios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblVistaCambiosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TblVistaCambios);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Codigo Cliente:");

        LblCodigoCliente.setText("jLabel9");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/recepcion.png"))); // NOI18N
        jButton2.setText("Reimprimir Ticket");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPImagenLayout = new javax.swing.GroupLayout(JPImagen);
        JPImagen.setLayout(JPImagenLayout);
        JPImagenLayout.setHorizontalGroup(
            JPImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
        );
        JPImagenLayout.setVerticalGroup(
            JPImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 188, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(JPImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/regresar-flecha-curva.png"))); // NOI18N
        jButton1.setText("Regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        LblNombreCliente.setText("jLabel11");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Fecha Registro:");

        LblFecha.setText("jLabel8");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Cambios");

        jLabel8.setText("Imagen:");

        LblTicket.setText("jLabel11");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Ticket: #");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Estado: ");

        LblValido.setText("jLabel3");

        jLabel3.setText("Monto:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LblCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LblTicket, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(LblValido, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(LblMonto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LblFecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(LblNombreCliente))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(LblCodigoCliente))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(LblTicket))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(LblFecha))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(LblMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(LblValido))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TblVistaCambiosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblVistaCambiosMouseClicked
        Imagen();
    }//GEN-LAST:event_TblVistaCambiosMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       ReimprimirTicket();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(VistaDeCambios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaDeCambios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaDeCambios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaDeCambios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaDeCambios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPImagen;
    private javax.swing.JLabel LblCodigoCliente;
    private javax.swing.JLabel LblFecha;
    private javax.swing.JLabel LblMonto;
    private javax.swing.JLabel LblNombreCliente;
    private javax.swing.JLabel LblTicket;
    private javax.swing.JLabel LblValido;
    private javax.swing.JTable TblVistaCambios;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
