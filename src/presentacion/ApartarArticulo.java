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
public class ApartarArticulo extends javax.swing.JFrame {

    String CodigoProductoApartado, TipoProductoApartado, MaterialProductoApartado, MedidaProductoApartado, opcion;
    String Cabezera1, Cabezera2, Cabezera3, Cabezera4, Cabezera5, Pie1, Pie2, Pie3, Pie4, Pie5;

    double PrecioProductoApartado, SubsumaApartados, TotalApartados, Monto;

    int numeroApartadosTotales;

    public static DefaultTableModel modeloProducto;
    public static String tituloProducto[] = {"Codigo", "Tipo", "Descripcion", "Medida", "Material", "Precio", "Cantidad", "Fecha"};
    public static String dataProducto[] = new String[8];

    

    public static DefaultTableModel modeloApartado;
    public static String tituloApartado[] = {"Codigo", "Tipo", "Material", "Medida", "Precio"};
    public static String dataApartado[] = new String[5];

    byte[] image = null;

    BufferedImage buffimg = null;

    InputStream img[] = new InputStream[1];

    /**
     * Creates new form ApartarArticulo
     */
    public ApartarArticulo() {
        initComponents();
        modeloApartado = new DefaultTableModel(null, tituloApartado) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblApartados.setModel(modeloApartado);
        LblCodigoCliente.setText(Principal.TxtCodigoCliente.getText());
        LblNombreCliente.setText(Principal.LblNombreCliente.getText());
        LblDescuentoCliente.setText(Principal.LblDescuentoCliente.getText());
        buttonGroup1.add(OptMaterial);
        buttonGroup1.add(OptMedida);
        buttonGroup1.add(OptTipo);
        buttonGroup1.add(OptDescripcion);

    }

    void consutaProducto() {
        

        modeloProducto = new DefaultTableModel(null, tituloProducto) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblProductos.setModel(modeloProducto);
        //---------------
        PreparedStatement ps;
        ResultSet rs;
        try {

            ps = con.prepareStatement("select * from inventario ORDER BY fechaRegistro ASC LIMIT 50");
            rs = ps.executeQuery();

            while (rs.next()) {

                
                dataProducto[0] = rs.getString("id");
                dataProducto[1] = rs.getString("producto");
                dataProducto[2] = rs.getString("descripcion");
                dataProducto[3] = rs.getString("medida");
                dataProducto[4] = rs.getString("material");
                dataProducto[5] = rs.getString("precio");
                dataProducto[6] = rs.getString("cantidad");
                dataProducto[7] = rs.getString("fechaRegistro");

                modeloProducto.addRow(dataProducto);
                

            }

            rs.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en la consulta del inventario: " + ex.getMessage());
        }

    }

    void buscarproducto(String buscar) {
        modeloProducto = new DefaultTableModel(null, tituloProducto) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblProductos.setModel(modeloProducto);
        if (OptDescripcion.isSelected() || OptCodigo.isSelected() || OptMaterial.isSelected() || OptMedida.isSelected() || OptTipo.isSelected()) {

            String sql = "SELECT * from inventario WHERE " + opcion + " like '%" + buscar + "%'";
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    
                    dataProducto[0] = rs.getString("id");
                    dataProducto[1] = rs.getString("producto");
                    dataProducto[2] = rs.getString("descripcion");
                    dataProducto[3] = rs.getString("medida");
                    dataProducto[4] = rs.getString("material");
                    dataProducto[5] = rs.getString("precio");
                    dataProducto[6] = rs.getString("cantidad");
                    dataProducto[7] = rs.getString("fechaRegistro");
                    modeloProducto.addRow(dataProducto);
                    // modeloImagen.addRow(dataImagen);

                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error en la consulta de labusqueda del inventario por obcion: " + e.getMessage());
            }
        } else {

            String sql = "SELECT * from inventario WHERE descripcion like '%" + TxtCodigoProductoApartado.getText() + "%'";
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    
                    dataProducto[0] = rs.getString("id");
                    dataProducto[1] = rs.getString("producto");
                    dataProducto[2] = rs.getString("descripcion");
                    dataProducto[3] = rs.getString("medida");
                    dataProducto[4] = rs.getString("material");
                    dataProducto[5] = rs.getString("precio");
                    dataProducto[6] = rs.getString("cantidad");
                    dataProducto[7] = rs.getString("fechaRegistro");
                    modeloProducto.addRow(dataProducto);
                    //modeloImagen.addRow(dataImagen);

                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error en la consulta de labusqueda del inventario: " + e.getMessage());
            }

        }

    }

    void EjecutarDescuento() {
        if ("".equals(TxtMonto.getText())) {
            Monto = 0;
        } else {
            Monto = Double.parseDouble(TxtMonto.getText());
        }

        for (int i = 0; i < TblApartados.getRowCount(); i++) {
            SubsumaApartados = SubsumaApartados + Double.parseDouble(TblApartados.getValueAt(i, 4).toString());
        }
        LblTotal.setText(String.valueOf(SubsumaApartados));
        TotalApartados = SubsumaApartados - Monto;
        LblResta.setText(String.valueOf(TotalApartados));

        SubsumaApartados = 0;

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
            JOptionPane.showMessageDialog(this, "Error en la consulta de partesticket: " + e.getMessage());
        }

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
        Ticket.AddCabecera("Cliente: " + LblCodigoCliente.getText() + " " + LblNombreCliente.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());

        Ticket.AddCabecera("Atiende: ");
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Num. Ticket: " + LblCodigoCliente.getText() + "" + numeroApartadosTotales);

        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Codi| Tipo| Medi| Mate| Precio");
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera(Ticket.DibujarLinea(39));

        for (int i = 0; i <= TblApartados.getRowCount() - 1; i++) {

            Ticket.AddCabecera("\n" + TblApartados.getValueAt(i, 0).toString()
                    + " " + TblApartados.getValueAt(i, 1).toString()
                    + " " + TblApartados.getValueAt(i, 2).toString()
                    + "  " + TblApartados.getValueAt(i, 3).toString()
                    + "  " + TblApartados.getValueAt(i, 4).toString());

            try {
                PreparedStatement pps;
                pps = con.prepareStatement("INSERT INTO apartados (codigo_cliente,nombre_cliente,num_ticket,primer_ticket,codigo_producto,tipo_producto,material_producto,medida_producto,precio_producto,Total,Monto,Resta,fecha,cortecaja) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pps.setString(1, LblCodigoCliente.getText());
                pps.setString(2, LblNombreCliente.getText());
                pps.setString(3, LblCodigoCliente.getText() + "" + numeroApartadosTotales);
                pps.setString(4, LblCodigoCliente.getText() + "" + numeroApartadosTotales);
                pps.setString(5, TblApartados.getValueAt(i, 0).toString());
                pps.setString(6, TblApartados.getValueAt(i, 1).toString());
                pps.setString(7, TblApartados.getValueAt(i, 2).toString());
                pps.setString(8, TblApartados.getValueAt(i, 3).toString());
                pps.setString(9, TblApartados.getValueAt(i, 4).toString());
                pps.setString(10, LblTotal.getText());
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
        Ticket.AddCabecera("Aticulos:" + TblApartados.getRowCount());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Subtotal:" + LblTotal.getText());
        Ticket.AddCabecera(Ticket.DarEspacio());
        Ticket.AddCabecera("Total:" + LblResta.getText());
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

    String fecha() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY");
        return formatoFecha.format(fecha);
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
            JOptionPane.showMessageDialog(this, "Error en la consulta del numero de los apartados: " + ex.getMessage());
        }
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
        TblProductos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        TxtCodigoProductoApartado = new javax.swing.JTextField();
        OptCodigo = new javax.swing.JRadioButton();
        OptMaterial = new javax.swing.JRadioButton();
        OptMedida = new javax.swing.JRadioButton();
        OptTipo = new javax.swing.JRadioButton();
        OptDescripcion = new javax.swing.JRadioButton();
        BtnAgregar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        JpImag = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TblApartados = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        LblTotal = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        LblCodigoCliente = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        LblNombreCliente = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        LblDescuentoCliente = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        LblResta = new javax.swing.JLabel();
        TxtMonto = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Apartar Articulo");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Productos"));

        TblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Catalogo", "Marca", "Talla", "Color", "Precio", "Fecha"
            }
        ));
        TblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblProductosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TblProductos);

        jLabel1.setText("Buscar:");

        TxtCodigoProductoApartado.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                TxtCodigoProductoApartadoCaretUpdate(evt);
            }
        });
        TxtCodigoProductoApartado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtCodigoProductoApartadoActionPerformed(evt);
            }
        });

        OptCodigo.setText("Codigo");

        OptMaterial.setText("Material");

        OptMedida.setText("Medida");

        OptTipo.setText("Tipo");

        OptDescripcion.setText("Descripción");

        BtnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/agregar-a-la-cesta-de-la-compra.png"))); // NOI18N
        BtnAgregar.setText("Agregar");
        BtnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Vista Previa"));

        javax.swing.GroupLayout JpImagLayout = new javax.swing.GroupLayout(JpImag);
        JpImag.setLayout(JpImagLayout);
        JpImagLayout.setHorizontalGroup(
            JpImagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );
        JpImagLayout.setVerticalGroup(
            JpImagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JpImag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JpImag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TxtCodigoProductoApartado, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(OptCodigo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OptMaterial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OptMedida)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OptTipo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OptDescripcion)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnAgregar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(OptCodigo)
                                .addComponent(OptMaterial)
                                .addComponent(OptMedida)
                                .addComponent(OptTipo)
                                .addComponent(OptDescripcion))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(TxtCodigoProductoApartado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BtnAgregar))))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Apartados"));

        TblApartados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Tipo", "Material", "Medida", "Precio"
            }
        ));
        jScrollPane2.setViewportView(TblApartados);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(257, 257, 257))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/carro-de-la-compra.png"))); // NOI18N
        jButton1.setText("Apartar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Total:");

        jLabel3.setText("Monto:");

        LblTotal.setText("0.00");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        jLabel5.setText("Codigo:");

        LblCodigoCliente.setText("jLabel6");

        jLabel7.setText("Nombre:");

        LblNombreCliente.setText("jLabel8");

        jLabel11.setText("Descuento:");

        LblDescuentoCliente.setText("jLabel12");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LblCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LblDescuentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(LblCodigoCliente)
                    .addComponent(jLabel7)
                    .addComponent(LblNombreCliente)
                    .addComponent(jLabel11)
                    .addComponent(LblDescuentoCliente))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/cancelar.png"))); // NOI18N
        jButton3.setText("Cancelar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/boton-eliminar.png"))); // NOI18N
        jButton4.setText("Eliminar Articulo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel13.setText("Resta:");

        LblResta.setText("0.00");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton4)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel13))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(LblResta)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(TxtMonto))))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jButton4)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(LblTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(TxtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(LblResta))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton3))
                        .addGap(22, 22, 22))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtCodigoProductoApartadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtCodigoProductoApartadoActionPerformed
        if (TxtCodigoProductoApartado.getText().equals("")) {
            consutaProducto();
        } else {
            buscarproducto(TxtCodigoProductoApartado.getText());

        }
    }//GEN-LAST:event_TxtCodigoProductoApartadoActionPerformed

    private void TblProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblProductosMouseClicked
        int posision = TblProductos.rowAtPoint(evt.getPoint());

        PreparedStatement ps;
        ResultSet rs;

        try {

            ps = con.prepareStatement("SELECT * FROM inventario WHERE id=?");
            ps.setInt(1, Integer.parseInt(TblProductos.getValueAt(posision, 0).toString()));
            rs = ps.executeQuery();

            while (rs.next()) {

                InputStream img = rs.getBinaryStream("imagen"); 
                try {
                    buffimg = ImageIO.read(img);
                    ImagenMySQL imagen = new ImagenMySQL(JpImag.getHeight(), JpImag.getWidth(), buffimg);
                    JpImag.add(imagen);
                    JpImag.repaint();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error en la consulta de inventario: " + ex.getMessage());
                }
            }

            rs.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }

    }//GEN-LAST:event_TblProductosMouseClicked

    private void TxtCodigoProductoApartadoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_TxtCodigoProductoApartadoCaretUpdate
        if (OptCodigo.isSelected()) {
            opcion = "id";
        } else {
            if (OptMaterial.isSelected()) {
                opcion = "material";
            } else {
                if (OptMedida.isSelected()) {
                    opcion = "medida";
                } else {
                    if (OptTipo.isSelected()) {
                        opcion = "producto";
                    } else {
                        if (OptDescripcion.isSelected()) {
                            opcion = "descripcion";
                        }
                    }
                }
            }
            
        }
        

    }//GEN-LAST:event_TxtCodigoProductoApartadoCaretUpdate

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        int fila = TblApartados.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un Producto");
        } else {
            modeloApartado.removeRow(fila);
            EjecutarDescuento();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void BtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAgregarActionPerformed
        int fila = TblProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un Producto");
        } else {
            if ("0".equals(TblProductos.getValueAt(fila, 6).toString())) {
                JOptionPane.showMessageDialog(this, "No Se Encuentra Disponible El Producto");
            } else {

                CodigoProductoApartado = TblProductos.getValueAt(fila, 0).toString();
                TipoProductoApartado = TblProductos.getValueAt(fila, 1).toString();
                MedidaProductoApartado = TblProductos.getValueAt(fila, 3).toString();
                MaterialProductoApartado = TblProductos.getValueAt(fila, 4).toString();
                PrecioProductoApartado = (Double.parseDouble(TblProductos.getValueAt(fila, 5).toString())) - (Principal.descuentoDeCliente * Double.parseDouble(TblProductos.getValueAt(fila, 5).toString()));
                String FilaElemen[] = {CodigoProductoApartado, TipoProductoApartado, MedidaProductoApartado, MaterialProductoApartado, String.valueOf(PrecioProductoApartado)};
                modeloApartado.addRow(FilaElemen);

                EjecutarDescuento();

            }
        }
    }//GEN-LAST:event_BtnAgregarActionPerformed

    private void TxtMontoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtMontoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car > '.')) {
            evt.consume();
        }

    }//GEN-LAST:event_TxtMontoKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if ("".equals(TxtMonto.getText())) {
           JOptionPane.showMessageDialog(this, "Favor de Ingresar un Monto para Apartar");
        } else {
            ticket();
        Principal ventaPrincipal = new Principal();
        ventaPrincipal.setLocationRelativeTo(null);
        ventaPrincipal.setVisible(true);
        dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TxtMontoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_TxtMontoCaretUpdate
        EjecutarDescuento();
    }//GEN-LAST:event_TxtMontoCaretUpdate

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Principal ventaPrincipal = new Principal();
        ventaPrincipal.setLocationRelativeTo(null);
        ventaPrincipal.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(ApartarArticulo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ApartarArticulo().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAgregar;
    private javax.swing.JPanel JpImag;
    private javax.swing.JLabel LblCodigoCliente;
    private javax.swing.JLabel LblDescuentoCliente;
    private javax.swing.JLabel LblNombreCliente;
    private javax.swing.JLabel LblResta;
    private javax.swing.JLabel LblTotal;
    private javax.swing.JRadioButton OptCodigo;
    private javax.swing.JRadioButton OptDescripcion;
    private javax.swing.JRadioButton OptMaterial;
    private javax.swing.JRadioButton OptMedida;
    private javax.swing.JRadioButton OptTipo;
    private javax.swing.JTable TblApartados;
    private javax.swing.JTable TblProductos;
    private javax.swing.JTextField TxtCodigoProductoApartado;
    private javax.swing.JTextField TxtMonto;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
conexion c = new conexion();
    Connection con = c.getConexion();
}
