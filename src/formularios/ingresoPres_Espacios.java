package formularios;

import clases.*;
import java.awt.Color;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ingresoPres_Espacios extends javax.swing.JFrame {

    Funciones funciones = new Funciones();
    DefaultTableModel model;
    String insert_update = null;
    String espacio = null;

    public ingresoPres_Espacios() {
        initComponents();
        setTitle("Prestamos de espacios CMD");//esto
        this.setLocationRelativeTo(null);//esto
        limpiar();
        bloquear();
        loadcomboPer();
        loadcomboEsp();
        mostrardatos("");
    }

    void mostrardatos(String valor) {
        String autorizado = "";
        String[] titulos = {"Id", "Fecha", "Autorizado", "Persona", "Espacio"};
        String[] registros = new String[5];
        String sql = "SELECT pes.id_presesp\n"
                + "          ,pes.fecha\n"
                + "	   ,pes.autorizado	   \n"
                + "	   ,CONCAT(p.nombre,' ',p.apellido) AS persona	   \n"
                + "	   , esp.descripcion AS espacio\n"
                + "FROM pres_espacios pes\n"
                + "INNER JOIN personas p ON p.id_persona=pes.id_persona\n"
                + "INNER JOIN espacios esp ON esp.id_espacio=pes.id_espacio\n"
                + "WHERE CONCAT(pes.id_presesp,' ',pes.fecha,' ',pes.autorizado,' ',p.nombre,' ',p.apellido,' ',esp.descripcion)"
                + " LIKE '%" + valor + "%'"
                + "ORDER BY pes.id_presesp ASC";

        model = new DefaultTableModel(null, titulos);
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                if ("1".equals(rs.getString("pes.autorizado"))) {
                    autorizado = "True";
                }
                if ("0".equals(rs.getString("pes.autorizado"))) {
                    autorizado = "False";
                }
                registros[0] = rs.getString("pes.id_presesp");
                registros[1] = rs.getString("pes.fecha");
                registros[2] = autorizado;
                registros[3] = rs.getString("persona");
                registros[4] = rs.getString("espacio");
                model.addRow(registros);
            }
            tEspacios.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    void loadcomboPer() {
        String sql = "SELECT CONCAT(p.nombre,' ', p.apellido) FROM personas p ORDER BY p.apellido ASC, p.nombre ASC ";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                cbPer.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    void loadcomboEsp() {
        String sql = "SELECT descripcion FROM espacios ORDER BY descripcion ASC";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                cbEsp.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    void confirmar_eliminar(){
        String botones[] = {"Si", "No"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Desea eliminar el prestamo de "+espacio+"?", "Confirmar eliminar",
                0, 0, null, botones, this);
        if(eleccion == JOptionPane.YES_OPTION){
            deleting();
        }else if(eleccion == JOptionPane.NO_OPTION){
            
        }
    }

    void limpiar() {
        txtId.setText("");
        dcCalendario.setDate(funciones.StringADate(""));
        cbAut.setSelectedIndex(0);
        cbPer.setSelectedIndex(0);
        cbEsp.setSelectedIndex(0);
        txtAuxBuscar.setText("");
    }

    void bloquear() {
        Color azul = new Color (184, 207, 229);
        Color gris = new Color(238,238,238);
        txtId.setEnabled(false);        
        dcCalendario.setEnabled(false);
        cbAut.setEnabled(false);
        cbPer.setEnabled(false);
        cbEsp.setEnabled(false);
        dcCalendario.setBorder(BorderFactory.createLineBorder(azul, 0));
        cbAut.setBorder(BorderFactory.createLineBorder(azul, 0));
        cbPer.setBorder(BorderFactory.createLineBorder(azul, 0));
        cbEsp.setBorder(BorderFactory.createLineBorder(azul, 0));
        dcCalendario.getDateEditor().getUiComponent().setBackground(Color.WHITE);
        cbAut.setBackground(gris);        
        cbPer.setBackground(gris);
        cbEsp.setBackground(gris);
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    void desbloquear() {
        Date ahora = new java.util.Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        txtId.setEnabled(true);
        dcCalendario.setDate(funciones.StringADate(formato.format(ahora)));
        dcCalendario.setEnabled(true);
        cbAut.setEnabled(true);
        cbPer.setEnabled(true);
        cbEsp.setEnabled(true);
        dcCalendario.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
        cbAut.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
        cbPer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
        cbEsp.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }
    
    void validating(){
       
       int cont = 0;
        Color rosita = new Color(255,204,229);
        Color gris = new Color(238,238,238);
        if ("Seleccionar..".equals(cbEsp.getSelectedItem())) {
            cbEsp.requestFocus();
            cbEsp.setToolTipText("Seleccione espacio");
            cbEsp.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cbEsp.setBackground(rosita);
            cont = cont + 1;
        } else {
            cbEsp.setToolTipText(null);
            cbEsp.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
            cbEsp.setBackground(gris);
        }           
               
        if ("Seleccionar..".equals(cbPer.getSelectedItem())) {
            cbPer.requestFocus();
            cbPer.setToolTipText("Seleccione persona");
            cbPer.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cbPer.setBackground(rosita);
            cont = cont + 1;
        } else {
            cbPer.setToolTipText(null);
            cbPer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
            cbPer.setBackground(gris);
        }
        
        if ("Seleccionar..".equals(cbAut.getSelectedItem())) {
            cbAut.requestFocus();
            cbAut.setToolTipText("Seleccione autorizado");
            cbAut.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cbAut.setBackground(rosita);
            cont = cont + 1;
        } else {
            cbAut.setToolTipText(null);
            cbAut.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
            cbAut.setBackground(gris);
        }
        
        if (null==(funciones.getFecha(dcCalendario))) {            
            dcCalendario.getDateEditor().getUiComponent().requestFocusInWindow();            
            dcCalendario.getDateEditor().getUiComponent().setToolTipText("La fecha no puede quedar vacía");
            dcCalendario.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            dcCalendario.getDateEditor().getUiComponent().setBackground(rosita);
            cont = cont + 1;
        } else {
            dcCalendario.getDateEditor().getUiComponent().setToolTipText(null);
            dcCalendario.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
            dcCalendario.getDateEditor().getUiComponent().setBackground(Color.WHITE);
        }          
        
        if(cont > 0){
            JOptionPane.showMessageDialog(null, "Es necesario llenar todos los campos", "Validar campos",
                    JOptionPane.INFORMATION_MESSAGE);
        } 

        if (cont == 0) {        
            if ("insert".equals(insert_update)) {
                inserting();
            }
            if ("update".equals(insert_update)) {
                updating();
            }
        }        
    }

    

    void inserting() {
        String autorizado = null;
        String per = null;
        String esp = null;

        if ("True".equals(cbAut.getSelectedItem().toString())) {
            autorizado = "1";
        }
        if ("False".equals(cbAut.getSelectedItem().toString())) {
            autorizado = "0";
        }

        if (!"True".equals(cbAut.getSelectedItem().toString()) && !"False".equals(cbAut.getSelectedItem().toString())) {
            autorizado = cbAut.getSelectedItem().toString();
        }

        try {
            String sql = "SELECT p.id_persona FROM personas p WHERE CONCAT(p.nombre,' ', p.apellido) LIKE '" + cbPer.getSelectedItem().toString() + "'";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                per = rs.getString("p.id_persona");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        try {
            String sql = "SELECT id_espacio FROM espacios WHERE descripcion LIKE '" + cbEsp.getSelectedItem().toString() + "'";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                esp = rs.getString("id_espacio");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        try {
            PreparedStatement pst = cn.prepareStatement("INSERT INTO pres_espacios(fecha,autorizado, "
                    + "                                  id_persona, id_espacio) VALUES (?,?,?,?)");

            pst.setString(1, funciones.getFecha(dcCalendario));
            pst.setObject(2, autorizado);
            pst.setObject(3, per);
            pst.setObject(4, esp);

            pst.executeUpdate();
            mostrardatos("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        limpiar();
        insert_update = "NULL";
    }

    void updating() {
        String autorizado = null;
        String per = null;
        String esp = null;

        if ("True".equals(cbAut.getSelectedItem().toString())) {
            autorizado = "1";
        }
        if ("False".equals(cbAut.getSelectedItem().toString())) {
            autorizado = "0";
        }

        if (!"True".equals(cbAut.getSelectedItem().toString()) && !"False".equals(cbAut.getSelectedItem().toString())) {
            autorizado = cbAut.getSelectedItem().toString();
        }

        try {
            String sql = "SELECT p.id_persona FROM personas p WHERE CONCAT(p.nombre,' ', p.apellido) LIKE '" + cbPer.getSelectedItem().toString() + "'";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                per = rs.getString("p.id_persona");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        try {
            String sql = "SELECT id_espacio FROM espacios WHERE descripcion LIKE '" + cbEsp.getSelectedItem().toString() + "'";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                esp = rs.getString("id_espacio");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        try {
            PreparedStatement pst = cn.prepareStatement("UPDATE pres_espacios SET "
                    + "fecha='" + funciones.getFecha(dcCalendario) + "',"
                    + "autorizado='" + autorizado + "',"
                    + "id_persona='" + per + "',  "
                    + "id_espacio='" + esp + "'  "
                    + "WHERE id_presesp='" + txtId.getText() + "'");
            pst.executeUpdate();
            mostrardatos("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        insert_update = "NULL";
    }
    
    void deleting() {
        int fila = tEspacios.getSelectedRow();
        String id = "";
        id = tEspacios.getValueAt(fila, 0).toString();

        try {
            PreparedStatement pst = cn.prepareStatement("DELETE FROM pres_espacios WHERE  id_presesp='" + id + "'");
            pst.executeUpdate();
            mostrardatos("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tEspacios = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtAuxBuscar = new javax.swing.JTextField();
        btnMostrarDatos = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbAut = new javax.swing.JComboBox();
        cbPer = new javax.swing.JComboBox();
        cbEsp = new javax.swing.JComboBox();
        btnReporte = new javax.swing.JButton();
        dcCalendario = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        lbusu = new javax.swing.JLabel();

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/editar3.png"))); // NOI18N
        jMenuItem1.setText("modificar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/eliminar3.png"))); // NOI18N
        jMenuItem2.setText("eliminar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 0, 0));

        jPanel2.setBackground(new java.awt.Color(153, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Prestamo de espacios"));

        tEspacios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tEspacios.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tEspacios);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Buscar");

        txtAuxBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuxBuscarActionPerformed(evt);
            }
        });
        txtAuxBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAuxBuscarKeyReleased(evt);
            }
        });

        btnMostrarDatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Buscar_16x16.png"))); // NOI18N
        btnMostrarDatos.setText("Mostrar datos");
        btnMostrarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarDatosActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresar o editar prestamo"));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Fecha");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Autorizado");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Persona");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar33.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Id");

        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/limpiar3.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevo.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setToolTipText("");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/TWalsh__Return-Reply1-16.png"))); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/cancelar3.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Espacio");

        cbAut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar..", "True", "False" }));

        cbPer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar.." }));

        cbEsp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar.." }));

        btnReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/MensajeNuevo.png"))); // NOI18N
        btnReporte.setText("Reporte");
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });

        dcCalendario.setDateFormatString("yyyy-MM-dd hh:mm:ss");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbPer, 0, 171, Short.MAX_VALUE)
                            .addComponent(cbEsp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbAut, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtId)
                            .addComponent(dcCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar)
                        .addGap(19, 19, 19)
                        .addComponent(btnVolver)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnReporte))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar)
                        .addGap(5, 5, 5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(dcCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbAut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(cbPer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbEsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnLimpiar)
                    .addComponent(btnVolver)
                    .addComponent(btnReporte))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAuxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnMostrarDatos)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAuxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarDatos)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.getAccessibleContext().setAccessibleName("Detalle Espacio");
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        jLabel7.setText("Usuario conectado:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbusu, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbusu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnMostrarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarDatosActionPerformed
    mostrardatos("");
    limpiar();
}//GEN-LAST:event_btnMostrarDatosActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
     desbloquear();
    int fila = tEspacios.getSelectedRow();
    if (fila >= 0) {

        txtId.setText(tEspacios.getValueAt(fila, 0).toString());
        dcCalendario.setDate(funciones.StringADate(tEspacios.getValueAt(fila, 1).toString()));
        cbAut.setSelectedItem(tEspacios.getValueAt(fila, 2).toString());
        cbPer.setSelectedItem(tEspacios.getValueAt(fila, 3).toString());
        cbEsp.setSelectedItem(tEspacios.getValueAt(fila, 4).toString());
        txtId.enable(false);
    } else {
        JOptionPane.showMessageDialog(null, "No selecionó fila");
    }
    insert_update = "update";
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    int fila = tEspacios.getSelectedRow();    
    espacio = tEspacios.getValueAt(fila, 4).toString();
    confirmar_eliminar();
    
}//GEN-LAST:event_jMenuItem2ActionPerformed

    private void txtAuxBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAuxBuscarKeyReleased
        mostrardatos(txtAuxBuscar.getText());
    }//GEN-LAST:event_txtAuxBuscarKeyReleased

    private void txtAuxBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuxBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAuxBuscarActionPerformed

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed

         String path = "C:\\Users\\Santiago\\Desktop\\Bases de datos pruebas\\BD prestamos ut\\control_prestamosV4\\src\\formularios\\Reportes\\report2.jasper";
//        String path = "D:\\Respaldo General\\Documentos\\Ingenieria\\8vo cuatri\\Bases de datos para aplicaciones\\"
//                + "2do Parcial\\control_prestamos con reportes\\control_prestamosV4"
//                + "\\src\\formularios\\Reportes\\report2.jasper";
        JasperReport jr = null;

        try {
            jr = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, cc.conexion());
            JasperViewer jv = new JasperViewer(jp);
            jv.setVisible(true);
            jv.setTitle(path);

            // TODO add your handling code here:
        } catch (JRException ex) {
            Logger.getLogger(ingresoPres_Equipos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReporteActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        bloquear();
        limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        desbloquear();
        dcCalendario.getDateEditor().getUiComponent().requestFocusInWindow();
        txtId.enable(false);
        insert_update = "insert";
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        validating();
    }//GEN-LAST:event_btnGuardarActionPerformed
    
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ingresoPres_Espacios().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMostrarDatos;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnReporte;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox cbAut;
    private javax.swing.JComboBox cbEsp;
    private javax.swing.JComboBox cbPer;
    private com.toedter.calendar.JDateChooser dcCalendario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    protected static javax.swing.JLabel lbusu;
    private javax.swing.JTable tEspacios;
    private javax.swing.JTextField txtAuxBuscar;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
    conectar cc = new conectar();
    Connection cn = cc.conexion();
}
