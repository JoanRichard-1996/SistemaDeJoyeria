/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

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

public final class Principal extends javax.swing.JFrame {

    public static DefaultTableModel modeloClientes;
    public static String tituloClientes[] = {"Codigo", "Cliente", "Descuento"};
    public static String dataClientes[] = new String[3];

    public static DefaultTableModel modeloProductos;
    public static String tituloProductos[] = {"Codigo", "Tipo", "Descripcion", "Medida", "Material", "Precio", "Cantidad", "Fecha"};
    public static String dataProductos[] = new String[8];

    public static DefaultTableModel modeloCompra;
    public static String tituloCompra[] = {"Codigo", "Tipo", "Medida", "Material", "Precio Normal", "Con Descuento"};
    public static String dataCompra[] = new String[5];

    String CodigoProducto, TipoProducto, MedidaProducto, MaterialProducto, PrecioProducto, opcion;
    public static double PrecioProductoDescuento, descuentoDeCliente, Subtotal, Ahorro, Total;
    int cantidad = 0;

    /**
     * Creates new form Principal
     */
    public Principal() {
        initComponents();
        this.setResizable(false);
        FormatearTablas();
        ConultarProductos();
        Desabilitar();
        buttonGroup1.add(OptMaterial);
        buttonGroup1.add(OptMedida);
        buttonGroup1.add(OptTipo);
        buttonGroup1.add(OptDescripcion);
    }
    void FormatearTablas(){
        modeloClientes = new DefaultTableModel(null, tituloClientes) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblCliente.setModel(modeloClientes);
        modeloProductos = new DefaultTableModel(null, tituloProductos) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblProductos.setModel(modeloProductos);
        modeloCompra = new DefaultTableModel(null, tituloCompra) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblCompras.setModel(modeloCompra);
    }

    void Desabilitar() {

        MeuApartar.setEnabled(false);
        BtnAgregarACompras.setEnabled(false);
        BtnEliminarProducto.setEnabled(false);
        BtnComprar.setEnabled(false);
        BtnCancelarCompra.setEnabled(false);
    }

    String fecha() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY");
        return formatoFecha.format(fecha);
    }

    void BuscarCliente() {
        modeloClientes = new DefaultTableModel(null, tituloClientes) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblCliente.setModel(modeloClientes);
        try {
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT * from cliente WHERE nombre like '%" + TxtNombreCliente.getText() + "%'");
            rs = ps.executeQuery();
            while (rs.next()) {
                dataClientes[0] = rs.getString("id");
                dataClientes[1] = rs.getString("nombre");
                dataClientes[2] = rs.getString("descuento");
                modeloClientes.addRow(dataClientes);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BtnCancelarCompra.setEnabled(true);
    }

    /**
     *
     */
     void ConultarProductos() {
         modeloProductos = new DefaultTableModel(null, tituloProductos) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblProductos.setModel(modeloProductos);
        try {
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT * from inventario ORDER BY fechaRegistro  LIMIT 50 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                dataProductos[0] = rs.getString("id");
                dataProductos[1] = rs.getString("producto");
                dataProductos[2] = rs.getString("descripcion");
                dataProductos[3] = rs.getString("medida");
                dataProductos[4] = rs.getString("material");
                dataProductos[5] = rs.getString("precio");
                dataProductos[6] = rs.getString("cantidad");
                dataProductos[7] = rs.getString("fechaRegistro");
                modeloProductos.addRow(dataProductos);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void Descontar() {
        

        for (int i = 0; i < TblCompras.getRowCount(); i++) {
            Subtotal = Subtotal + Double.parseDouble(TblCompras.getValueAt(i, 4).toString());
            Total = Total + Double.parseDouble(TblCompras.getValueAt(i, 5).toString());
        }
        Ahorro = Subtotal - Total;
        LblSubtotal.setText(String.valueOf(Subtotal));
        LblTotal.setText(String.valueOf(Total));
        LblAhorro.setText(String.valueOf(Ahorro));
        Subtotal = 0;
        Total = 0;
        Ahorro = 0;
    }
    void BuscarProducto(String buscar){
        
        modeloProductos = new DefaultTableModel(null, tituloProductos) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TblProductos.setModel(modeloProductos);
        if (OptDescripcion.isSelected() || OptCodigo.isSelected() || OptMaterial.isSelected() || OptMedida.isSelected() || OptTipo.isSelected()) {

            String sql = "SELECT * from inventario WHERE " + opcion + " like '%" + buscar + "%'";
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    
                    dataProductos[0] = rs.getString("id");
                    dataProductos[1] = rs.getString("producto");
                    dataProductos[2] = rs.getString("descripcion");
                    dataProductos[3] = rs.getString("medida");
                    dataProductos[4] = rs.getString("material");
                    dataProductos[5] = rs.getString("precio");
                    dataProductos[6] = rs.getString("cantidad");
                    dataProductos[7] = rs.getString("fechaRegistro");
                    modeloProductos.addRow(dataProductos);
                    

                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error en la consulta de labusqueda del inventario por obcion: " + e.getMessage());
            }
        } else {

            String sql = "SELECT * from inventario WHERE descripcion like '%" + buscar + "%'";
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    
                    dataProductos[0] = rs.getString("id");
                    dataProductos[1] = rs.getString("producto");
                    dataProductos[2] = rs.getString("descripcion");
                    dataProductos[3] = rs.getString("medida");
                    dataProductos[4] = rs.getString("material");
                    dataProductos[5] = rs.getString("precio");
                    dataProductos[6] = rs.getString("cantidad");
                    dataProductos[7] = rs.getString("fechaRegistro");
                    modeloProductos.addRow(dataProductos);
                    

                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error en la consulta de labusqueda del inventario: " + e.getMessage());
            }

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
        jLabel1 = new javax.swing.JLabel();
        TxtNombreCliente = new javax.swing.JTextField();
        BtnSeleccionar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        LblNombreCliente = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        LblDescuentoCliente = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TblCliente = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        TxtCodigoCliente = new javax.swing.JLabel();
        LblPrimeraCompra = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        BtnAgregarACompras = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblProductos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        JpImagen = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        TxtCodigoProducto = new javax.swing.JTextField();
        OptCodigo = new javax.swing.JRadioButton();
        OptMaterial = new javax.swing.JRadioButton();
        OptMedida = new javax.swing.JRadioButton();
        OptTipo = new javax.swing.JRadioButton();
        OptDescripcion = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TblCompras = new javax.swing.JTable();
        BtnCancelarCompra = new javax.swing.JButton();
        BtnEliminarProducto = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        LbFecha = new javax.swing.JLabel();
        BtnComprar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        LblTotal = new javax.swing.JLabel();
        LblAhorro = new javax.swing.JLabel();
        LblSubtotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        MenuAdministrador = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        MenuListaClientes = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        MenuAgregarCliente = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenu4 = new javax.swing.JMenu();
        MenuListaProducto = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        MenuAgregarProducto = new javax.swing.JMenuItem();
        MenuListaCompras = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        MeuApartar = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ventana Principal");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setExtendedState(6);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cliente"));

        jLabel1.setText("Cliente:");

        TxtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNombreClienteActionPerformed(evt);
            }
        });

        BtnSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/lupa-de-busqueda.png"))); // NOI18N
        BtnSeleccionar.setText("Buscar");
        BtnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeleccionarActionPerformed(evt);
            }
        });

        jLabel3.setText("Cliente:");

        jLabel2.setText("Descuento:");

        TblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Cliente", "Descuento"
            }
        ));
        TblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblClienteMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TblCliente);

        jLabel5.setText("Codigo:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TxtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BtnSeleccionar)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtCodigoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(LblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(LblDescuentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LblPrimeraCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(TxtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnSeleccionar))
                        .addGap(18, 26, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LblNombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(TxtCodigoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(LblDescuentoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE)
                            .addComponent(LblPrimeraCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Producto"));

        BtnAgregarACompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/agregar-a-la-cesta-de-la-compra.png"))); // NOI18N
        BtnAgregarACompras.setText("Agregar A Compras");
        BtnAgregarACompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarAComprasActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Lista de Productos"));

        TblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Codigo", "Catalogo", "Marca", "Talla", "Color", "Precio", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblProductosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TblProductos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Vista Previa"));

        JpImagen.setPreferredSize(new java.awt.Dimension(202, 193));

        javax.swing.GroupLayout JpImagenLayout = new javax.swing.GroupLayout(JpImagen);
        JpImagen.setLayout(JpImagenLayout);
        JpImagenLayout.setHorizontalGroup(
            JpImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        JpImagenLayout.setVerticalGroup(
            JpImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 232, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JpImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JpImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(BtnAgregarACompras))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnAgregarACompras)))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Buscar Producto"));

        jLabel6.setText("Descripcion:");

        TxtCodigoProducto.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                TxtCodigoProductoCaretUpdate(evt);
            }
        });
        TxtCodigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtCodigoProductoActionPerformed(evt);
            }
        });

        OptCodigo.setText("Codigo");

        OptMaterial.setText("Material");

        OptMedida.setText("Medida");

        OptTipo.setText("Tipo");

        OptDescripcion.setText("Descripción");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TxtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OptCodigo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OptMaterial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OptMedida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OptTipo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OptDescripcion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(TxtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OptCodigo)
                    .addComponent(OptMaterial)
                    .addComponent(OptMedida)
                    .addComponent(OptTipo)
                    .addComponent(OptDescripcion))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Compras"));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Lista de Compras"));

        TblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TblCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblComprasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TblCompras);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1103, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );

        BtnCancelarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/cancelar.png"))); // NOI18N
        BtnCancelarCompra.setText("CancelarCompra");
        BtnCancelarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarCompraActionPerformed(evt);
            }
        });

        BtnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/boton-eliminar.png"))); // NOI18N
        BtnEliminarProducto.setText("Eliminar Producto");
        BtnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEliminarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(BtnEliminarProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BtnCancelarCompra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(BtnEliminarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnCancelarCompra)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Fecha"));

        LbFecha.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LbFecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LbFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        BtnComprar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/carro-de-la-compra.png"))); // NOI18N
        BtnComprar.setText("Comprar");
        BtnComprar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnComprarActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Total:$");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Ahorro: $");

        LblTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LblTotal.setText("0.0");

        LblAhorro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LblAhorro.setText("0.0");

        LblSubtotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LblSubtotal.setText("0.0");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("SubTotal:$");

        jMenuBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMenuBar2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        MenuAdministrador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/avatar.png"))); // NOI18N
        MenuAdministrador.setText("Administrador");
        MenuAdministrador.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        MenuAdministrador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuAdministradorMouseClicked(evt);
            }
        });
        jMenuBar2.add(MenuAdministrador);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/shopping-list.png"))); // NOI18N
        jMenu2.setText("Clientes");
        jMenu2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenu2.setIconTextGap(10);
        jMenu2.setMargin(new java.awt.Insets(0, 0, 0, 20));

        MenuListaClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/shopping-list.png"))); // NOI18N
        MenuListaClientes.setText("Lista de Clientes");
        MenuListaClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuListaClientesActionPerformed(evt);
            }
        });
        jMenu2.add(MenuListaClientes);
        jMenu2.add(jSeparator4);

        MenuAgregarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/boton-redondo-agregar.png"))); // NOI18N
        MenuAgregarCliente.setText("Agregar Cliente");
        MenuAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAgregarClienteActionPerformed(evt);
            }
        });
        jMenu2.add(MenuAgregarCliente);
        jMenu2.add(jSeparator5);

        jMenuBar2.add(jMenu2);

        jMenu4.setBorder(null);
        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/shopping-list.png"))); // NOI18N
        jMenu4.setText("Productos");
        jMenu4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenu4.setIconTextGap(10);
        jMenu4.setMargin(new java.awt.Insets(0, 0, 0, 20));

        MenuListaProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/shopping-list.png"))); // NOI18N
        MenuListaProducto.setText("Lista de Productos");
        MenuListaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuListaProductoActionPerformed(evt);
            }
        });
        jMenu4.add(MenuListaProducto);
        jMenu4.add(jSeparator2);

        MenuAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/boton-redondo-agregar.png"))); // NOI18N
        MenuAgregarProducto.setText("Agregar Producto");
        MenuAgregarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuAgregarProductoMouseClicked(evt);
            }
        });
        MenuAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAgregarProductoActionPerformed(evt);
            }
        });
        jMenu4.add(MenuAgregarProducto);

        jMenuBar2.add(jMenu4);

        MenuListaCompras.setBorder(null);
        MenuListaCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/shopping-list.png"))); // NOI18N
        MenuListaCompras.setText("Compras");
        MenuListaCompras.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        MenuListaCompras.setIconTextGap(10);
        MenuListaCompras.setMargin(new java.awt.Insets(0, 0, 0, 20));
        MenuListaCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuListaComprasMouseClicked(evt);
            }
        });
        MenuListaCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuListaComprasActionPerformed(evt);
            }
        });
        jMenuBar2.add(MenuListaCompras);

        jMenu1.setBorder(null);
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/shopping-list.png"))); // NOI18N
        jMenu1.setText("Cambios");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenu1.setIconTextGap(10);
        jMenu1.setMargin(new java.awt.Insets(0, 0, 0, 20));
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar2.add(jMenu1);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/cambio-de-dinero (1).png"))); // NOI18N
        jMenu3.setText("Generar Cambio");
        jMenu3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar2.add(jMenu3);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/cajero.png"))); // NOI18N
        jMenu5.setText("Corte de Caja");
        jMenu5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu5MouseClicked(evt);
            }
        });
        jMenuBar2.add(jMenu5);

        MeuApartar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Files/regreso.png"))); // NOI18N
        MeuApartar.setText("Apartar");
        MeuApartar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jMenuItem1.setText("Apartar Articulo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        MeuApartar.add(jMenuItem1);
        MeuApartar.add(jSeparator1);

        jMenuItem2.setText("Agregar monto a un apartado");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        MeuApartar.add(jMenuItem2);

        jMenuBar2.add(MeuApartar);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(LblSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGap(8, 8, 8))
                                        .addComponent(BtnComprar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(LblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(LblAhorro)))
                                .addGap(96, 96, 96))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BtnComprar)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LblSubtotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(LblAhorro))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void MenuListaClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuListaClientesActionPerformed
        // TODO add your handling code here:
        ListaDeClientes ListaClientes = new ListaDeClientes();
        ListaClientes.setLocationRelativeTo(null);
        ListaClientes.setVisible(true);
        dispose();
    }//GEN-LAST:event_MenuListaClientesActionPerformed
    private void TxtCodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtCodigoProductoActionPerformed
        if (TxtCodigoProducto.getText().equals("")) {
            ConultarProductos();
        } else {
            BuscarProducto(TxtCodigoProducto.getText());

        }
    }//GEN-LAST:event_TxtCodigoProductoActionPerformed
    private void MenuAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAgregarProductoActionPerformed

        AgregarProducto ListaVenta = new AgregarProducto();
        ListaVenta.setLocationRelativeTo(null);
        ListaVenta.setVisible(true);
        dispose();

    }//GEN-LAST:event_MenuAgregarProductoActionPerformed
    private void TblProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblProductosMouseClicked
        int posision = TblProductos.rowAtPoint(evt.getPoint());

        PreparedStatement ps;
        ResultSet rs;

        try {

            ps = con.prepareStatement("SELECT * FROM inventario WHERE id=?");
            ps.setInt(1, Integer.parseInt(TblProductos.getValueAt(posision, 0).toString()));
            rs = ps.executeQuery();

            while (rs.next()) {

                InputStream img = rs.getBinaryStream("imagen"); // reading image as InputStream
                try {
                    BufferedImage buffimg = ImageIO.read(img);
                    ImagenMySQL imagen = new ImagenMySQL(JpImagen.getHeight(), JpImagen.getWidth(), buffimg);
                    JpImagen.add(imagen);
                    JpImagen.repaint();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error en la consulta de inventario: " + ex.getMessage());
                }
            }

            rs.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }

    }//GEN-LAST:event_TblProductosMouseClicked
    private void BtnAgregarAComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAgregarAComprasActionPerformed
        int fila = TblProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un Producto");
        } else {
            if ("0".equals(TblProductos.getValueAt(fila, 6).toString())) {
                JOptionPane.showMessageDialog(this, "No Se Encuentra Disponible El Producto");
            } else {
          
                CodigoProducto = TblProductos.getValueAt(fila, 0).toString();
                TipoProducto = TblProductos.getValueAt(fila, 1).toString();
                MedidaProducto = TblProductos.getValueAt(fila, 3).toString();
                MaterialProducto = TblProductos.getValueAt(fila, 4).toString();
                PrecioProducto = TblProductos.getValueAt(fila, 5).toString();
                PrecioProductoDescuento = (Double.parseDouble(TblProductos.getValueAt(fila, 5).toString())) - (descuentoDeCliente * Double.parseDouble(TblProductos.getValueAt(fila, 5).toString()));

                String FilaElemen[] = {CodigoProducto, TipoProducto, MedidaProducto, MaterialProducto, PrecioProducto, String.valueOf(PrecioProductoDescuento)};
                modeloCompra.addRow(FilaElemen);

                BtnComprar.setEnabled(true);
                BtnEliminarProducto.setEnabled(true);

                cantidad = (Integer.parseInt(TblProductos.getValueAt(fila, 6).toString()) - 1);
                modeloProductos.setValueAt(cantidad, fila, 6);

                Descontar();

            }
        }
    }//GEN-LAST:event_BtnAgregarAComprasActionPerformed
    private void BtnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEliminarProductoActionPerformed
        int fila = TblCompras.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un Producto");
        } else {
            modeloCompra.removeRow(fila);
            Descontar();
            if(modeloCompra.getRowCount()== 0){
            BtnComprar.setEnabled(false);
            }
        }
    }//GEN-LAST:event_BtnEliminarProductoActionPerformed
    private void BtnCancelarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelarCompraActionPerformed
        Desabilitar();
        FormatearTablas();
        ConultarProductos();
        LblNombreCliente.setText("");
        LblDescuentoCliente.setText("");
        TxtCodigoCliente.setText("");
        LblSubtotal.setText("0.00");
        LblAhorro.setText("0.00");
        LblTotal.setText("0.00");
        TxtNombreCliente.setText("");
        TxtNombreCliente.setEnabled(true);
        BtnSeleccionar.setEnabled(true);
        TxtCodigoProducto.setText("");
    }//GEN-LAST:event_BtnCancelarCompraActionPerformed
    private void MenuAgregarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuAgregarProductoMouseClicked
    }//GEN-LAST:event_MenuAgregarProductoMouseClicked
    private void MenuListaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuListaProductoActionPerformed
        ListaDeProductos Productos = new ListaDeProductos();
        Productos.setLocationRelativeTo(null);
        Productos.setVisible(true);
        dispose();
    }//GEN-LAST:event_MenuListaProductoActionPerformed
    private void MenuAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAgregarClienteActionPerformed
        // TODO add your handling code here:

        AgregarUnCliente AgregarCliente = new AgregarUnCliente();
        AgregarCliente.setLocationRelativeTo(null);
        AgregarCliente.setVisible(true);
        dispose();
    }//GEN-LAST:event_MenuAgregarClienteActionPerformed
    private void BtnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeleccionarActionPerformed
        
        BuscarCliente();
        
    }//GEN-LAST:event_BtnSeleccionarActionPerformed
    private void TxtCodigoProductoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_TxtCodigoProductoCaretUpdate
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
        
        
    }//GEN-LAST:event_TxtCodigoProductoCaretUpdate
    private void BtnComprarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnComprarActionPerformed

        ImprimirCompra ImprimirCompra = new ImprimirCompra(this, true);
        ImprimirCompra.setLocationRelativeTo(null);
        ImprimirCompra.setVisible(true);
        FormatearTablas();
        ConultarProductos();

    }//GEN-LAST:event_BtnComprarActionPerformed
    private void MenuListaComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuListaComprasMouseClicked
        ListaCompras ListaCompras = new ListaCompras();
        ListaCompras.setLocationRelativeTo(null);
        ListaCompras.setVisible(true);
        dispose();
    }//GEN-LAST:event_MenuListaComprasMouseClicked
    private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseClicked
        CorteCaja CorteCaja = new CorteCaja();
        CorteCaja.setLocationRelativeTo(null);
        CorteCaja.setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenu5MouseClicked
    private void MenuListaComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuListaComprasActionPerformed
    }//GEN-LAST:event_MenuListaComprasActionPerformed
    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        GenerarCambio GenerarCambio = new GenerarCambio();
        GenerarCambio.setLocationRelativeTo(null);
        GenerarCambio.setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenu3MouseClicked
    private void MenuAdministradorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuAdministradorMouseClicked
        LoginAdministrador LoginAdministrador = new LoginAdministrador();
        LoginAdministrador.setLocationRelativeTo(null);
        LoginAdministrador.setVisible(true);
        dispose();
    }//GEN-LAST:event_MenuAdministradorMouseClicked
    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        ListaDeCambios ListaDeCambios = new ListaDeCambios();
        ListaDeCambios.setLocationRelativeTo(null);
        ListaDeCambios.setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenu1MouseClicked
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
                    ImagenMySQL imagen = new ImagenMySQL(JpImagen.getHeight(), JpImagen.getWidth(), buffimg);
                    JpImagen.add(imagen);
                    JpImagen.repaint();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error en la consulta de inventario: " + ex.getMessage());
                }
            }

            rs.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }//GEN-LAST:event_TblComprasMouseClicked
    private void TblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblClienteMouseClicked
        if (BtnSeleccionar.isEnabled() == false) {
            JOptionPane.showMessageDialog(null, "Un cliente seta seleccionado, ");
        } else {
            int posi = TblCliente.rowAtPoint(evt.getPoint());

            String sql = "SELECT * from ventas WHERE id like '%" + TblCliente.getValueAt(posi, 0).toString() + "%'";

            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                System.out.println(rs.next());
                if (rs.next() == true) {
                    LblNombreCliente.setText(TblCliente.getValueAt(posi, 1).toString());
                    TxtCodigoCliente.setText(TblCliente.getValueAt(posi, 0).toString());
                    LblDescuentoCliente.setText(TblCliente.getValueAt(posi, 2).toString());
                    descuentoDeCliente = Double.parseDouble(TblCliente.getValueAt(posi, 2).toString()) / 100;
                    BtnSeleccionar.setEnabled(false);
                    TxtNombreCliente.setEnabled(false);
                    TblCliente.setEnabled(false);
                    BtnAgregarACompras.setEnabled(true);
                    BtnCancelarCompra.setEnabled(true);
                    MeuApartar.setEnabled(true);
                    System.out.println("entro 1");
                } else {
                    LblNombreCliente.setText(TblCliente.getValueAt(posi, 1).toString());
                    TxtCodigoCliente.setText(TblCliente.getValueAt(posi, 0).toString());
                    LblPrimeraCompra.setText("Primera");
                    LblDescuentoCliente.setText("50");
                    descuentoDeCliente = 0.50;
                    BtnSeleccionar.setEnabled(false);
                    TxtNombreCliente.setEnabled(false);
                    TblCliente.setEnabled(false);
                    BtnAgregarACompras.setEnabled(true);
                    BtnCancelarCompra.setEnabled(true);
                    MeuApartar.setEnabled(true);
                    System.out.println("entro 2");
                }
            } catch (Exception e) {
                System.out.println("Error de consulta: " + e.getMessage());

            }

        }


    }//GEN-LAST:event_TblClienteMouseClicked
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ApartarArticulo VentanaApartar = new ApartarArticulo();
        VentanaApartar.setLocationRelativeTo(null);
        VentanaApartar.setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        AgregarMontoApartado AbonarApartado = new AgregarMontoApartado();
        AbonarApartado.setLocationRelativeTo(null);
        AbonarApartado.setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    private void TxtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNombreClienteActionPerformed
        BuscarCliente();
    }//GEN-LAST:event_TxtNombreClienteActionPerformed

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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton BtnAgregarACompras;
    public static javax.swing.JButton BtnCancelarCompra;
    public static javax.swing.JButton BtnComprar;
    public static javax.swing.JButton BtnEliminarProducto;
    public static javax.swing.JButton BtnSeleccionar;
    private javax.swing.JPanel JpImagen;
    private javax.swing.JLabel LbFecha;
    public static javax.swing.JLabel LblAhorro;
    public static javax.swing.JLabel LblDescuentoCliente;
    public static javax.swing.JLabel LblNombreCliente;
    private javax.swing.JLabel LblPrimeraCompra;
    public static javax.swing.JLabel LblSubtotal;
    public static javax.swing.JLabel LblTotal;
    private javax.swing.JMenu MenuAdministrador;
    private javax.swing.JMenuItem MenuAgregarCliente;
    private javax.swing.JMenuItem MenuAgregarProducto;
    private javax.swing.JMenuItem MenuListaClientes;
    private javax.swing.JMenu MenuListaCompras;
    private javax.swing.JMenuItem MenuListaProducto;
    private javax.swing.JMenu MeuApartar;
    private javax.swing.JRadioButton OptCodigo;
    private javax.swing.JRadioButton OptDescripcion;
    private javax.swing.JRadioButton OptMaterial;
    private javax.swing.JRadioButton OptMedida;
    private javax.swing.JRadioButton OptTipo;
    public static javax.swing.JTable TblCliente;
    public static javax.swing.JTable TblCompras;
    public static javax.swing.JTable TblProductos;
    public static javax.swing.JLabel TxtCodigoCliente;
    private javax.swing.JTextField TxtCodigoProducto;
    public static javax.swing.JTextField TxtNombreCliente;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    // End of variables declaration//GEN-END:variables
conexion c = new conexion();
    Connection con = c.getConexion();

}
