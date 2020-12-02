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
import logica.conexion;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author boy_d
 */
public final class ImprimirCompra extends javax.swing.JDialog {
    
    int ii,cantidad_produto;
    String numticket = "",id_producto,id,idcliente;
    conexion c = new conexion();
    Connection con = c.getConexion();
    double precio_descuento_producto;
   
    boolean cond=true,valido;
    PreparedStatement pps;
    String Cabezera1,Cabezera2,Cabezera3,Cabezera4,Cabezera5,Pie1,Pie2,Pie3,Pie4,Pie5;
    /**
     * Creates new form ImprimirCompra
     * @param parent
     * @param modal
     */
    public ImprimirCompra(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        parteticket();
        Compra();
        this.setResizable(false);
    }

    String fecha() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY");
        return formatoFecha.format(fecha);
    }

    String mes() {
        Date mes = new Date();
        SimpleDateFormat fmes = new SimpleDateFormat("MM");
        return fmes.format(mes);
    }

    String dia() {
        Date dia = new Date();
        SimpleDateFormat fdia = new SimpleDateFormat("dd");
        return fdia.format(dia);
    }

    String hora() {
        Date mes = new Date();
        SimpleDateFormat fmes = new SimpleDateFormat("hh");
        return fmes.format(mes);
    }

    void Compra() {
        
        LblFechaIngreso.setText(fecha());
        LblCodigoCliente.setText(Principal.TxtCodigoCliente.getText());
        if(cond==true){
        numticket ="01"+ mes() + "" + hora() + "" + dia()+ "" + LblCodigoCliente.getText();
        cond=false;
        }else{
        numticket ="01"+ mes() + "" + dia() + "" + hora() + "" + LblCodigoCliente.getText();
        cond=true;
        }
        
        LblTicket.setText(numticket);
        LblNombreCliente.setText(Principal.LblNombreCliente.getText());
        LblSubTotal.setText(Principal.LblSubtotal.getText());
        LblTotal.setText(Principal.LblTotal.getText());
        LblAhorro.setText(Principal.LblAhorro.getText());
    }
    
    private void parteticket() {
       
    

        String sql = "select * from partesticket WHERE id=1";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
              Cabezera1=rs.getString("cabezera1"); 
              Cabezera2=rs.getString("cabezera2"); 
              Cabezera3=rs.getString("cabezera3"); 
              Cabezera4=rs.getString("cabezera4"); 
              Cabezera5=rs.getString("cabezera5"); 
              Pie1=rs.getString("pie1");
              Pie2=rs.getString("pie2");
              Pie3=rs.getString("pie3");
              Pie4=rs.getString("pie4");
              Pie5=rs.getString("pie5");
               
            }
          
        } catch (SQLException e) {
            System.out.println("Error de consulta: " + e.getMessage());
        }
        
     }
    void ticket() {
        

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
        Ticket.AddCabecera("Cliente: " + LblCodigoCliente.getText() + " " + LblNombreCliente.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Atiende: ");
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Num. Ticket: " + LblTicket.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Codi| Tipo| Medi| Mate| Precio");
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        if (Principal.TblCompras.getRowCount() > 0) {
            for (ii = 0; ii <= Principal.TblCompras.getRowCount() - 1; ii++) {

                Ticket.AddCabecera("\n" + Principal.TblCompras.getValueAt(ii, 0).toString()
                        + " " + Principal.TblCompras.getValueAt(ii, 1).toString()
                        + " " + Principal.TblCompras.getValueAt(ii, 2).toString()
                        + "  " + Principal.TblCompras.getValueAt(ii, 3).toString()
                        + "  " + Principal.TblCompras.getValueAt(ii, 5).toString());
                        
                try {
            pps = con.prepareStatement("INSERT INTO ventas (codigo_cliente,nombre_cliente,num_ticket,codigo_producto,tipo_producto,medida_producto,material_producto,precio_producto,precio_descuento_producto,subtotal,total,ahorro,fecha,corte_caja) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pps.setString(1, LblCodigoCliente.getText());
            pps.setString(2, LblNombreCliente.getText());
            pps.setString(3, numticket);
            pps.setString(4, Principal.TblCompras.getValueAt(ii, 0).toString());
            pps.setString(5, Principal.TblCompras.getValueAt(ii, 1).toString());
            pps.setString(6, Principal.TblCompras.getValueAt(ii, 2).toString());
            pps.setString(7, Principal.TblCompras.getValueAt(ii, 3).toString());
            pps.setDouble(8, Double.parseDouble(Principal.TblCompras.getValueAt(ii, 4).toString()) );
            pps.setDouble(9, Double.parseDouble(Principal.TblCompras.getValueAt(ii, 5).toString()));
            pps.setDouble(10, Double.parseDouble(LblSubTotal.getText()));
            pps.setDouble(11, Double.parseDouble(LblTotal.getText()));
            pps.setDouble(12, Double.parseDouble(LblAhorro.getText()));
            pps.setString(13, fecha());
            pps.setBoolean(14,false);
            pps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger("No se pudo cargar la base de datos: " + ImprimirCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
        }
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Aticulos:" + ii);
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Subtotal:" + LblSubTotal.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Total:" + LblTotal.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Usted Ahorro:" + LblAhorro.getText());
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
        limpiar();
    }
    
    void actualizarProducto(){
        //System.out.println(Principal.TblCompras.getRowCount());
        
    if (Principal.TblCompras.getRowCount() > 0) {
            for (int i = 0; i < Principal.TblCompras.getRowCount() ; i++) {
                
                id=Principal.TblCompras.getValueAt(i, 0).toString();
            String sql = "SELECT * from inventario WHERE id='" + id + "'";
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    
                    id_producto = rs.getString("id");
                    cantidad_produto = rs.getInt("cantidad");
                    cantidad_produto=cantidad_produto -1;
                    
                PreparedStatement ps = con.prepareStatement("UPDATE inventario SET cantidad ='"+cantidad_produto
                +"' WHERE id='" + id_producto + "' ");
            ps.executeUpdate();
                }

              

            } catch (Exception e) {
                System.out.println("Error de consulta de busqueda: " + e.getMessage());
            }
                
            }
        }
    }
    public void limpiar(){
               
        Principal.LblSubtotal.setText("0.00");
        Principal.LblAhorro.setText("0.00");
        Principal.LblTotal.setText("0.00");
        Principal.LblNombreCliente.setText("");
        Principal.LblDescuentoCliente.setText("");
        Principal.TxtCodigoCliente.setText("");
        Principal.TxtNombreCliente.setText("");
        Principal.BtnAgregarACompras.setEnabled(false);
        Principal.BtnEliminarProducto.setEnabled(false);
        Principal.BtnComprar.setEnabled(false);
        Principal.BtnCancelarCompra.setEnabled(false);
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
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        LblFechaIngreso = new javax.swing.JLabel();
        LblCodigoCliente = new javax.swing.JLabel();
        LblTicket = new javax.swing.JLabel();
        LblSubTotal = new javax.swing.JLabel();
        LblAhorro = new javax.swing.JLabel();
        LblTotal = new javax.swing.JLabel();
        LblNombreCliente = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        TxtCambios = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        LblDescuentoCambio = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TICKET DE COMPRA");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Fecha Ingreso:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Cliente:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Ticket: #");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("SubTotal:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Total:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Usted se Ahorro:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/carro-de-la-compra.png"))); // NOI18N
        jButton1.setText("Comprar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        LblFechaIngreso.setText("jLabel8");

        LblCodigoCliente.setText("jLabel9");

        LblTicket.setText("jLabel10");

        LblSubTotal.setText("jLabel11");

        LblAhorro.setText("jLabel12");

        LblTotal.setText("jLabel13");

        LblNombreCliente.setText("jLabel9");

        jLabel8.setText("Agrecar un Cambio:");

        TxtCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtCambiosActionPerformed(evt);
            }
        });
        TxtCambios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtCambiosKeyTyped(evt);
            }
        });

        jLabel9.setText("Cambio:");

        LblDescuentoCambio.setText("0.00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel9))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(LblCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(LblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(LblFechaIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LblTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LblSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LblAhorro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(TxtCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblDescuentoCambio)
                            .addComponent(LblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(LblFechaIngreso))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(LblCodigoCliente)
                    .addComponent(LblNombreCliente))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(TxtCambios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(LblTicket))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(LblSubTotal))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(LblAhorro))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(LblDescuentoCambio))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(LblTotal))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         
        ticket();
        actualizarProducto();
        limpiar();
       dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TxtCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtCambiosActionPerformed
        double total =Double.parseDouble(LblTotal.getText());
        String sql = "SELECT * FROM cambios WHERE num_ticket = '"+ TxtCambios.getText() +"'";
        
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                   
                idcliente=rs.getString("codigo_cliente");
                precio_descuento_producto = rs.getDouble("monto");
                valido = rs.getBoolean("valido");
                }
            } catch (SQLException e) {
                System.out.println("Error de consulta de busqueda: " + e.getMessage());
            }
            
            
            if (idcliente == null ? LblCodigoCliente.getText() == null : idcliente.equals(LblCodigoCliente.getText())){
                if(valido == false){
                    if (precio_descuento_producto <= total) {
                        total=total-precio_descuento_producto;
                        LblDescuentoCambio.setText(Double.toString((double) Math.round(precio_descuento_producto * 100d) / 100d));
                        LblTotal.setText(Double.toString((double) Math.round(total * 100d) / 100d));
                        try {
                            PreparedStatement ps = con.prepareStatement("UPDATE cambios SET valido ='"+1
                +"' WHERE num_ticket='" + TxtCambios.getText() + "' ");
            ps.executeUpdate();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                    }
                }else{
                 JOptionPane.showMessageDialog(this, "El Ticket de Cambio ya no es valido");
                }
            }else{
                JOptionPane.showMessageDialog(this, "El Codigo del cliente registrado en el ticket de Cambio no es el mismo que el de la compra.");
            }
    }//GEN-LAST:event_TxtCambiosActionPerformed

    private void TxtCambiosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCambiosKeyTyped
    char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car > '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_TxtCambiosKeyTyped

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
            java.util.logging.Logger.getLogger(ImprimirCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImprimirCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImprimirCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImprimirCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImprimirCompra dialog = new ImprimirCompra(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LblAhorro;
    private javax.swing.JLabel LblCodigoCliente;
    private javax.swing.JLabel LblDescuentoCambio;
    private javax.swing.JLabel LblFechaIngreso;
    private javax.swing.JLabel LblNombreCliente;
    private javax.swing.JLabel LblSubTotal;
    private javax.swing.JLabel LblTicket;
    private javax.swing.JLabel LblTotal;
    private javax.swing.JTextField TxtCambios;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
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
