/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import ImprimirClases.Ticket;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logica.ImagenMySQL;
import logica.conexion;

/**
 *
 * @author boy_d
 */
public class GenerarCambio extends javax.swing.JFrame {

    conexion c = new conexion();
    Connection con = c.getConexion();

    DefaultTableModel modeloVenta;
    String tituloVenta[] = {"Codigo", "Tipo", "Medida", "Material","Precio Normal", "Con Descuento"};
    String dataVenta[] = new String[6];
    
    DefaultTableModel modeloDevolucion;
    String tituloDevolucion[] = {"Codigo", "Tipo", "Medida", "Material","Precio Normal", "Con Descuento"};
    String dataDevolucion[] = new String[6];

    double TotalDevolucion;
    String CodigoCliente,CodigoProducto,TipoProducto,MedidaProducto,MaterialProducto,PrecioProducto,PrecioProductoDescuento;
    String Cabezera1,Cabezera2,Cabezera3,Cabezera4,Cabezera5,Pie1,Pie2,Pie3,Pie4,Pie5;
    /**
     * Creates new form GenerarCambio
     */
    public GenerarCambio() {
        initComponents();
        BtnDevolverArticulo.setEnabled(false);
        BtnGenerarCambio.setEnabled(false);
        BtnRegresarCompra.setEnabled(false);
        this.setResizable(false);
        
        modeloDevolucion = new DefaultTableModel(null, tituloDevolucion) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblDevueltos.setModel(modeloDevolucion);
    }

    String fecha() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY");
        return formatoFecha.format(fecha);
    }

    void ConsultasTicketVenta() {
        modeloVenta = new DefaultTableModel(null, tituloVenta) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblCompras.setModel(modeloVenta);
        try {
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT * from ventas WHERE num_ticket ='" + TxtBuscarTicket.getText() + "'");
            rs = ps.executeQuery();

            
                while (rs.next()) {
                    dataVenta[0] = rs.getString("codigo_producto");
                    dataVenta[1] = rs.getString("tipo_producto");
                    dataVenta[2] = rs.getString("medida_producto");
                    dataVenta[3] = rs.getString("material_producto");
                    dataVenta[4] = rs.getString("precio_producto");
                    dataVenta[5] = rs.getString("precio_descuento_producto");
                    modeloVenta.addRow(dataVenta);
                    CodigoCliente = rs.getString("codigo_cliente");
                    LblSubTotal.setText(rs.getString("subtotal"));
                    LblAhorro.setText(rs.getString("ahorro"));
                    LblDescuentoCliente.setText("");
                    LblTotal.setText(rs.getString("total"));
                    LblFecha.setText(rs.getString("fecha"));
                }
                TxtBuscarTicket.setEnabled(false);
                BtnDevolverArticulo.setEnabled(true);

            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en la consulta de ventas: " + ex);
        }
        ConsultaClientes();
    }

    void ConsultaClientes() {
        try {
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT * from cliente WHERE id ='" + CodigoCliente + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                LblCodigoCliente.setText(rs.getString("id"));
                LblNombreCliente.setText(rs.getString("nombre"));
                LblDescuentoCliente.setText(rs.getString("descuento") + "%");

            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void TotalDevolucion(){
        for (int i = 0; i < TblDevueltos.getRowCount(); i++) {
            TotalDevolucion= TotalDevolucion + Double.parseDouble(TblDevueltos.getValueAt(i, 5).toString());
        }
        LblTotalDevolucion.setText(String.valueOf(TotalDevolucion));
        TotalDevolucion= 0;
    }
    void EliminarVenta(){
         String sql = "DELETE FROM ventas WHERE num_ticket='" + TxtBuscarTicket.getText() + "'";
            try {
                Statement st = con.createStatement();
                st.executeUpdate(sql);
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la Compra: " + e);
            }
        CrearVenta();
    }
    void CrearVenta(){
        PreparedStatement pps;
        for (int i = 0; i < TblCompras.getRowCount(); i++) {
            try {
            pps = con.prepareStatement("INSERT INTO ventas (codigo_cliente,nombre_cliente,num_ticket,codigo_producto,tipo_producto,medida_producto,material_producto,precio_producto,precio_descuento_producto,subtotal,total,ahorro,fecha,corte_caja) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pps.setString(1, LblCodigoCliente.getText());
            pps.setString(2, LblNombreCliente.getText());
            pps.setString(3, TxtBuscarTicket.getText());
            pps.setString(4, TblCompras.getValueAt(i, 0).toString());
            pps.setString(5, TblCompras.getValueAt(i, 1).toString());
            pps.setString(6, TblCompras.getValueAt(i, 2).toString());
            pps.setString(7, TblCompras.getValueAt(i, 3).toString());
            pps.setDouble(8, Double.parseDouble(TblCompras.getValueAt(i, 4).toString()) );
            pps.setDouble(9, Double.parseDouble(TblCompras.getValueAt(i, 5).toString()));
            pps.setDouble(10, Double.parseDouble(LblSubTotal.getText()));
            pps.setDouble(11, Double.parseDouble(LblTotal.getText()));
            pps.setDouble(12, Double.parseDouble(LblAhorro.getText()));
            pps.setString(13, LblFecha.getText());
            pps.setBoolean(14,false);
            pps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger("No se pudo cargar la base de datos: " + ImprimirCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    void AgregarCambio(){
        PreparedStatement pps;
        for (int i = 0; i < TblDevueltos.getRowCount(); i++) {
            try {
            pps = con.prepareStatement("INSERT INTO cambios (codigo_cliente,nombre_cliente,num_ticket,codigo_producto,tipo_producto,medida_producto,material_producto,precio_producto,precio_descuento_producto,monto,fecha,FechaTicketAnterior,corte_caja,valido) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pps.setString(1, LblCodigoCliente.getText());
            pps.setString(2, LblNombreCliente.getText());
            pps.setString(3, TxtBuscarTicket.getText());
            pps.setString(4, TblDevueltos.getValueAt(i, 0).toString());
            pps.setString(5, TblDevueltos.getValueAt(i, 1).toString());
            pps.setString(6, TblDevueltos.getValueAt(i, 2).toString());
            pps.setString(7, TblDevueltos.getValueAt(i, 3).toString());
            pps.setDouble(8, Double.parseDouble(TblDevueltos.getValueAt(i, 4).toString()) );
            pps.setDouble(9, Double.parseDouble(TblDevueltos.getValueAt(i, 5).toString()));
            pps.setDouble(10, Double.parseDouble(LblTotalDevolucion.getText()));
            pps.setString(11, fecha());
            pps.setString(12, LblFecha.getText());
            pps.setBoolean(13,false);
            pps.setBoolean(14,false);
            pps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger("No se pudo cargar la base de datos: " + ImprimirCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    void parteticket() {
       
    

        String sql = "select * from partesticket WHERE id=2";
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
    void TicketCambio(){
        parteticket();
        
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
        Ticket.AddCabecera("Ticket: " + "02"+TxtBuscarTicket.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Cliente: " );
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Codigo: " + LblCodigoCliente.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Nombre: " + LblNombreCliente.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Monto: " + LblTotalDevolucion.getText());
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblCompras = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        TxtBuscarTicket = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        LblCodigoCliente = new javax.swing.JLabel();
        LblNombreCliente = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        LblSubTotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        LblTotal = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        LblAhorro = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        LblFecha = new javax.swing.JLabel();
        BtnDevolverArticulo = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        LblDescuentoCliente = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TblDevueltos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        LblTotalDevolucion = new javax.swing.JLabel();
        BtnRegresarCompra = new javax.swing.JButton();
        BtnGenerarCambio = new javax.swing.JButton();
        JPImagenCompras = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Articulos de Ticket de Compra"));

        TblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TblCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblComprasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TblCompras);

        jLabel1.setText("Ticket: #");

        TxtBuscarTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarTicketActionPerformed(evt);
            }
        });
        TxtBuscarTicket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TxtBuscarTicketKeyTyped(evt);
            }
        });

        jLabel2.setText("Cliente:");

        jLabel5.setText("SubTotal:");

        jLabel7.setText("Total:");

        jLabel9.setText("Ahorro:");

        jLabel6.setText("Fecha:");

        BtnDevolverArticulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/regreso.png"))); // NOI18N
        BtnDevolverArticulo.setText("Devolver Articulo");
        BtnDevolverArticulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDevolverArticuloActionPerformed(evt);
            }
        });

        jLabel10.setText("Descuento:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LblSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LblAhorro, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(LblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(LblDescuentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnDevolverArticulo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TxtBuscarTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(LblCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblCodigoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(TxtBuscarTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(LblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LblAhorro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel10)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LblFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LblDescuentoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(BtnDevolverArticulo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Articulos devueltos"));

        TblDevueltos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TblDevueltos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblDevueltosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TblDevueltos);

        jLabel3.setText("Total de Devolucion:");

        BtnRegresarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/atras.png"))); // NOI18N
        BtnRegresarCompra.setText("Regresar");
        BtnRegresarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRegresarCompraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LblTotalDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnRegresarCompra)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(BtnRegresarCompra)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(LblTotalDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        BtnGenerarCambio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/cambio-de-dinero (1).png"))); // NOI18N
        BtnGenerarCambio.setText("Generar Cambio");
        BtnGenerarCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGenerarCambioActionPerformed(evt);
            }
        });

        JPImagenCompras.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout JPImagenComprasLayout = new javax.swing.GroupLayout(JPImagenCompras);
        JPImagenCompras.setLayout(JPImagenComprasLayout);
        JPImagenComprasLayout.setHorizontalGroup(
            JPImagenComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 176, Short.MAX_VALUE)
        );
        JPImagenComprasLayout.setVerticalGroup(
            JPImagenComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 176, Short.MAX_VALUE)
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/cancelar.png"))); // NOI18N
        jButton1.setText("Cancelar Cambio");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(31, 31, 31)
                            .addComponent(JPImagenCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(BtnGenerarCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(JPImagenCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BtnGenerarCambio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtBuscarTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarTicketActionPerformed
        ConsultasTicketVenta();
    }//GEN-LAST:event_TxtBuscarTicketActionPerformed

    private void TblComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblComprasMouseClicked
        int posision = TblCompras.rowAtPoint(evt.getPoint());

        PreparedStatement ps;
        ResultSet rs;

        try {

            ps = con.prepareStatement("SELECT * FROM inventario WHERE id=?");
            ps.setInt(1, Integer.parseInt(TblCompras.getValueAt(posision, 0).toString()));
            rs = ps.executeQuery();

            while (rs.next()) {

                InputStream img = rs.getBinaryStream("imagen");
                try {
                    BufferedImage buffimg = ImageIO.read(img);
                    ImagenMySQL imagen = new ImagenMySQL(JPImagenCompras.getHeight(), JPImagenCompras.getWidth(), buffimg);
                    JPImagenCompras.add(imagen);
                    JPImagenCompras.repaint();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error en la consulta de inventario: " + ex.getMessage());
                }
            }

            rs.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }//GEN-LAST:event_TblComprasMouseClicked

    private void BtnDevolverArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDevolverArticuloActionPerformed
        int fila = TblCompras.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un Producto");
        } else {
            CodigoProducto = TblCompras.getValueAt(fila, 0).toString();
            TipoProducto = TblCompras.getValueAt(fila, 1).toString();
            MedidaProducto = TblCompras.getValueAt(fila, 2).toString();
            MaterialProducto = TblCompras.getValueAt(fila, 3).toString();
            PrecioProducto = TblCompras.getValueAt(fila, 4).toString();
            PrecioProductoDescuento = TblCompras.getValueAt(fila, 5).toString();

            String FilaElemen[] = {CodigoProducto, TipoProducto, MedidaProducto, MaterialProducto,PrecioProducto, PrecioProductoDescuento};
            modeloDevolucion.addRow(FilaElemen);
            modeloVenta.removeRow(fila);
            BtnRegresarCompra.setEnabled(true);
            BtnGenerarCambio.setEnabled(true);
            TotalDevolucion();
        }
    }//GEN-LAST:event_BtnDevolverArticuloActionPerformed

    private void BtnRegresarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRegresarCompraActionPerformed
        int fila = TblDevueltos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un Producto");
        } else {
            CodigoProducto = TblDevueltos.getValueAt(fila, 0).toString();
            TipoProducto = TblDevueltos.getValueAt(fila, 1).toString();
            MedidaProducto = TblDevueltos.getValueAt(fila, 2).toString();
            MaterialProducto = TblDevueltos.getValueAt(fila, 3).toString();
            PrecioProducto=  TblDevueltos.getValueAt(fila, 4).toString();
            PrecioProductoDescuento = TblDevueltos.getValueAt(fila, 5).toString();

            String FilaElemen[] = {CodigoProducto, TipoProducto, MedidaProducto, MaterialProducto,PrecioProducto, PrecioProductoDescuento};
            modeloVenta.addRow(FilaElemen);
            modeloDevolucion.removeRow(fila);
            TotalDevolucion();
        }
    }//GEN-LAST:event_BtnRegresarCompraActionPerformed

    private void BtnGenerarCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGenerarCambioActionPerformed
        TicketCambio();
        EliminarVenta();
        AgregarCambio();
        Principal pricicpal = new Principal();
        pricicpal.setLocationRelativeTo(null);
        pricicpal.setVisible(true);
        dispose();
    }//GEN-LAST:event_BtnGenerarCambioActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Principal ventaPrincipal = new Principal();
        ventaPrincipal.setLocationRelativeTo(null);
        ventaPrincipal.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TblDevueltosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblDevueltosMouseClicked
       int posision = TblDevueltos.rowAtPoint(evt.getPoint());

        PreparedStatement ps;
        ResultSet rs;

        try {

            ps = con.prepareStatement("SELECT * FROM inventario WHERE id=?");
            ps.setInt(1, Integer.parseInt(TblDevueltos.getValueAt(posision, 0).toString()));
            rs = ps.executeQuery();

            while (rs.next()) {

                InputStream img = rs.getBinaryStream("imagen");
                try {
                    BufferedImage buffimg = ImageIO.read(img);
                    ImagenMySQL imagen = new ImagenMySQL(JPImagenCompras.getHeight(), JPImagenCompras.getWidth(), buffimg);
                    JPImagenCompras.add(imagen);
                    JPImagenCompras.repaint();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error en la consulta de inventario: " + ex.getMessage());
                }
            }

            rs.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }//GEN-LAST:event_TblDevueltosMouseClicked

    private void TxtBuscarTicketKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtBuscarTicketKeyTyped
           char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car > '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_TxtBuscarTicketKeyTyped

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GenerarCambio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new GenerarCambio().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDevolverArticulo;
    private javax.swing.JButton BtnGenerarCambio;
    private javax.swing.JButton BtnRegresarCompra;
    private javax.swing.JPanel JPImagenCompras;
    private javax.swing.JLabel LblAhorro;
    private javax.swing.JLabel LblCodigoCliente;
    private javax.swing.JLabel LblDescuentoCliente;
    private javax.swing.JLabel LblFecha;
    private javax.swing.JLabel LblNombreCliente;
    private javax.swing.JLabel LblSubTotal;
    private javax.swing.JLabel LblTotal;
    private javax.swing.JLabel LblTotalDevolucion;
    private javax.swing.JTable TblCompras;
    private javax.swing.JTable TblDevueltos;
    private javax.swing.JTextField TxtBuscarTicket;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
