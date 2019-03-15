package formularios;

import clases.*;
import java.awt.Color;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ingresoPersonas extends javax.swing.JFrame {

    DefaultTableModel model;
    String insert_update = null;
    String persona = null;
    ResultSet id_persona = null;

    public ingresoPersonas() {
         initComponents();
        setTitle("Ingreso personas");//esto
        this.setLocationRelativeTo(null);//esto
        limpiar();
        bloquear();
        mostrardatos("");
    }

    void mostrardatos(String valor) {
        String encargado = "";
        String[] titulos = {"Id", "No. Empleado", "Nombre", "Apellido", "Sexo", ""
            + "Teléfono", "Correo", "Dirección", "Encargado", "Tipo"};
        String[] registros = new String[10];
        String sql = "SELECT id_persona, num_empleado, nombre, apellido, sexo,"
                + " telefono, correo, direccion, encargado, tipo FROM personas "
                + "WHERE CONCAT(id_persona, ' ', num_empleado, ' ', nombre, ' ', apellido, ' ', sexo, ' ',"
                + "telefono, ' ', correo, ' ', direccion, ' ', encargado, ' ', tipo)"
                + " LIKE '%" + valor + "%'"
                + "ORDER BY id_persona ASC";

        model = new DefaultTableModel(null, titulos);
        conectar cc = new conectar();
        Connection cn = cc.conexion();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                if ("1".equals(rs.getString("encargado"))) {
                    encargado = "True";
                }
                if ("0".equals(rs.getString("encargado"))) {
                    encargado = "False";
                }
                registros[0] = rs.getString("id_persona");
                registros[1] = rs.getString("num_empleado");
                registros[2] = rs.getString("nombre");
                registros[3] = rs.getString("apellido");
                registros[4] = rs.getString("sexo");
                registros[5] = rs.getString("telefono");
                registros[6] = rs.getString("correo");
                registros[7] = rs.getString("direccion");
                registros[8] = encargado;
                registros[9] = rs.getString("tipo");

                model.addRow(registros);
            }
            tEspacios.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    void confirmar_eliminar(){
        String botones[] = {"Si", "No"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Desea eliminar a "+persona+"?", "Confirmar eliminar",
                0, 0, null, botones, this);
        if(eleccion == JOptionPane.YES_OPTION){
            deleting();
        }else if(eleccion == JOptionPane.NO_OPTION){
            
        }
    }    

    void limpiar() {
        txtId.setText("");
        txtNum.setText("");
        txtNom.setText("");
        txtApe.setText("");
        cbSex.setSelectedIndex(0);
        txtTel.setText("");
        txtCor.setText("");
        txtDir.setText("");
        cbEnc.setSelectedIndex(0);
        cbTipo.setSelectedIndex(0);
        txtAuxBuscar.setText("");
    }

    void bloquear() {
        Color azul = new Color (184, 207, 229);
        Color gris = new Color(238,238,238);
        txtId.setEnabled(false);
        txtNum.setEnabled(false);
        txtNom.setEnabled(false);
        txtApe.setEnabled(false);
        cbSex.setEnabled(false);
        txtTel.setEnabled(false);
        txtCor.setEnabled(false);
        txtDir.setEnabled(false);
        cbEnc.setEnabled(false);
        cbTipo.setEnabled(false);
        txtNum.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtNom.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtApe.setBorder(BorderFactory.createLineBorder(azul, 1));
        cbSex.setBorder(BorderFactory.createLineBorder(azul, 0));
        txtTel.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtCor.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtDir.setBorder(BorderFactory.createLineBorder(azul, 1));
        cbEnc.setBorder(BorderFactory.createLineBorder(azul, 0));
        cbTipo.setBorder(BorderFactory.createLineBorder(azul, 0));
        cbSex.setBackground(gris);        
        cbEnc.setBackground(gris);
        cbTipo.setBackground(gris);
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    void desbloquear() {
        txtId.setEnabled(true);
        txtNum.setEnabled(true);
        txtNom.setEnabled(true);
        txtApe.setEnabled(true);
        cbSex.setEnabled(true);
        txtTel.setEnabled(true);
        txtCor.setEnabled(true);
        txtDir.setEnabled(true);
        cbEnc.setEnabled(true);
        cbTipo.setEnabled(true);
        txtNum.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtNom.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtApe.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cbSex.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
        txtTel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtCor.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtDir.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cbEnc.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
        cbTipo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }
    
     void validating() {
        int cont = 0;
        Color rosita = new Color(255,204,229);
        Color gris = new Color(238,238,238);
        if ("Seleccionar..".equals(cbTipo.getSelectedItem())) {
            cbTipo.requestFocus();
            cbTipo.setToolTipText("Seleccione tipo");
            cbTipo.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cbTipo.setBackground(rosita);
            cont = cont + 1;
        } else {
            cbTipo.setToolTipText(null);
            cbTipo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
            cbTipo.setBackground(gris);
        }           
               
        if ("Seleccionar..".equals(cbEnc.getSelectedItem())) {
            cbEnc.requestFocus();
            cbEnc.setToolTipText("Seleccione encargado");
            cbEnc.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cbEnc.setBackground(rosita);
            cont = cont + 1;
        } else {
            cbEnc.setToolTipText(null);
            cbEnc.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
            cbEnc.setBackground(gris);
        }
        
        if ("".equals(txtDir.getText())) {
            txtDir.requestFocus();
            txtDir.setToolTipText("La dirección no puede quedar vacía"); 
            txtDir.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtDir.setToolTipText(null);
            txtDir.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if ("".equals(txtCor.getText())) {
            txtCor.requestFocus();
            txtCor.setToolTipText("El correo no puede quedar vacío");
            txtCor.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            
            cont = cont + 1;
        } else {
            txtCor.setToolTipText(null);
            txtCor.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }           
               
        if ("".equals(txtTel.getText())) {
            txtTel.requestFocus();
            txtTel.setToolTipText("El teléfono no puede quedar vacío");
            txtTel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtTel.setToolTipText(null);
            txtTel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if ("Seleccionar..".equals(cbSex.getSelectedItem())) {
            cbSex.requestFocus();
            cbSex.setToolTipText("Seleccione sexo"); 
            cbSex.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cbSex.setBackground(rosita);
            cont = cont + 1;
        } else {
            cbSex.setToolTipText(null);
            cbSex.setBorder(BorderFactory.createLineBorder(Color.GRAY, 0));
            cbSex.setBackground(gris);
        }
        
        if ("".equals(txtApe.getText())) {
            txtApe.requestFocus();
            txtApe.setToolTipText("El apellido no puede quedar vacío");
            txtApe.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtApe.setToolTipText(null);
            txtApe.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }           
               
        if ("".equals(txtNom.getText())) {
            txtNom.requestFocus();
            txtNom.setToolTipText("La nombre no puede quedar vacío");
            txtNom.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtNom.setToolTipText(null);
            txtNom.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if ("".equals(txtNum.getText())) {
            txtNum.requestFocus();
            txtNum.setToolTipText("El No. Empleado no puede quedar vacío"); 
            txtNum.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtNum.setToolTipText(null);
            txtNum.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
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
        String encargado = null;

        if ("True".equals(cbEnc.getSelectedItem().toString())) {
            encargado = "1";
        }
        if ("False".equals(cbEnc.getSelectedItem().toString())) {
            encargado = "0";
        }
        if (!"True".equals(cbEnc.getSelectedItem().toString()) && !"False".equals(cbEnc.getSelectedItem().toString())) {
            encargado = cbEnc.getSelectedItem().toString();
        }

        try {
            PreparedStatement pst = cn.prepareStatement("INSERT INTO personas(num_empleado, nombre, apellido, sexo,"
                    + "telefono, correo, direccion, encargado, tipo) VALUES (?,?,?,?,?,?,?,?,?)");

            pst.setString(1, txtNum.getText());
            pst.setString(2, txtNom.getText());
            pst.setString(3, txtApe.getText());
            pst.setString(4, cbSex.getSelectedItem().toString());
            pst.setString(5, txtTel.getText());
            pst.setString(6, txtCor.getText());
            pst.setString(7, txtDir.getText());
            pst.setObject(8, encargado);
            pst.setString(9, cbTipo.getSelectedItem().toString());

            pst.executeUpdate();
            mostrardatos("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        limpiar();
        bloquear();
        insert_update = "NULL";
    }

    void updating() {
        String encargado = null;

        if ("True".equals(cbEnc.getSelectedItem().toString())) {
            encargado = "1";
        }
        if ("False".equals(cbEnc.getSelectedItem().toString())) {
            encargado = "0";
        }        

        try {
            PreparedStatement pst = cn.prepareStatement("UPDATE personas SET "
                    + "num_empleado='" + txtNum.getText() + "',"
                    + "nombre='" + txtNom.getText() + "',"
                    + "apellido='" + txtApe.getText() + "',  "
                    + "sexo='" + cbSex.getSelectedItem().toString() + "',"
                    + "telefono='" + txtTel.getText() + "',"
                    + "correo='" + txtCor.getText() + "',  "
                    + "direccion='" + txtDir.getText() + "',"
                    + "encargado='" + encargado + "',"
                    + "tipo='" + cbTipo.getSelectedItem().toString() + "'  "
                    + "WHERE id_persona='" + txtId.getText() + "'");
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
        
        String sql = "SELECT id_persona FROM personas WHERE id_persona = "+id;
        try {
            Statement st = cn.createStatement();
            id_persona = st.executeQuery(sql);
           
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);            
        } 

        try {
            PreparedStatement pst = cn.prepareStatement("DELETE FROM personas WHERE  id_persona='" + id + "'");
            pst.executeUpdate();
            mostrardatos("");
        } catch (Exception ex) {
             if(id_persona != null){
            JOptionPane.showMessageDialog(null, "No es posible eliminar esta persona, ya que su ID esta "
                   + "siendo usado en uno o más prestamos"); 
            } else {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtAuxBuscar = new javax.swing.JTextField();
        btnMostrarDatos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tEspacios = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNum = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNom = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtApe = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTel = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCor = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDir = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        Tipo = new javax.swing.JLabel();
        cbEnc = new javax.swing.JComboBox();
        cbTipo = new javax.swing.JComboBox();
        cbSex = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
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
        setMaximumSize(null);

        jPanel2.setBackground(new java.awt.Color(153, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Catálogo de Personas"));

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

        tEspacios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tEspacios.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tEspacios);

        jPanel1.setBackground(new java.awt.Color(153, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresar o editar Persona"));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("No. Empleado");

        txtNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nombre");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Apellido");

        txtApe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApeActionPerformed(evt);
            }
        });

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
        jLabel6.setText("Sexo");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Teléfono");

        txtTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelActionPerformed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Correo");

        txtCor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Dirección");

        txtDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDirActionPerformed(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Encargado");

        Tipo.setForeground(new java.awt.Color(255, 255, 255));
        Tipo.setText("Tipo");

        cbEnc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar..", "True", "False" }));

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar..", "Maestro", "Secretaria", "Admin" }));

        cbSex.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar..", "Hombre", "Mujer" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(13, 13, 13)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtApe, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNum, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNom, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSex, javax.swing.GroupLayout.Alignment.LEADING, 0, 147, Short.MAX_VALUE))
                        .addGap(181, 181, 181)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(Tipo)
                                .addGap(13, 13, 13))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addGap(32, 32, 32)
                        .addComponent(btnLimpiar)
                        .addGap(18, 18, 18)
                        .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtTel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCor, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDir)
                    .addComponent(cbEnc, 0, 147, Short.MAX_VALUE)
                    .addComponent(cbTipo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar)
                .addGap(101, 101, 101))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtApe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtCor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(cbEnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Tipo)
                            .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLimpiar)
                        .addComponent(btnVolver))
                    .addComponent(btnNuevo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAuxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnMostrarDatos)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAuxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarDatos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.getAccessibleContext().setAccessibleName("Detalle Espacio");
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        jLabel11.setText("Usuario conectado:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbusu, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbusu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getAccessibleContext().setAccessibleName("Ingreso Personas");

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
    validating();
}//GEN-LAST:event_btnGuardarActionPerformed

private void btnMostrarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarDatosActionPerformed
    mostrardatos("");
    limpiar();
}//GEN-LAST:event_btnMostrarDatosActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    desbloquear();
    int fila = tEspacios.getSelectedRow();
    if (fila >= 0) {

        txtId.setText(tEspacios.getValueAt(fila, 0).toString());
        txtNum.setText(tEspacios.getValueAt(fila, 1).toString());
        txtNom.setText(tEspacios.getValueAt(fila, 2).toString());
        txtApe.setText(tEspacios.getValueAt(fila, 3).toString());
        cbSex.setSelectedItem(tEspacios.getValueAt(fila, 4).toString());
        txtTel.setText(tEspacios.getValueAt(fila, 5).toString());
        txtCor.setText(tEspacios.getValueAt(fila, 6).toString());
        txtDir.setText(tEspacios.getValueAt(fila, 7).toString());
        cbEnc.setSelectedItem(tEspacios.getValueAt(fila, 8).toString());
        cbTipo.setSelectedItem(tEspacios.getValueAt(fila, 9).toString());
        txtId.enable(false);
    } else {
        JOptionPane.showMessageDialog(null, "No selecionó fila");
    }
    insert_update = "update";
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    int fila = tEspacios.getSelectedRow();    
    persona = tEspacios.getValueAt(fila, 2).toString() + " " + tEspacios.getValueAt(fila, 3).toString();
    confirmar_eliminar();    
}//GEN-LAST:event_jMenuItem2ActionPerformed

    private void txtNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtAuxBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAuxBuscarKeyReleased
        mostrardatos(txtAuxBuscar.getText());
    }//GEN-LAST:event_txtAuxBuscarKeyReleased

    private void txtAuxBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuxBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAuxBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        desbloquear();
        txtNum.requestFocus();
        txtId.enable(false);
        insert_update = "insert";
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        bloquear();
        limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void txtApeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApeActionPerformed

    private void txtTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelActionPerformed

    private void txtCorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorActionPerformed

    private void txtDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDirActionPerformed
    
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ingresoPersonas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Tipo;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMostrarDatos;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox cbEnc;
    private javax.swing.JComboBox cbSex;
    private javax.swing.JComboBox cbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    protected static javax.swing.JLabel lbusu;
    private javax.swing.JTable tEspacios;
    private javax.swing.JTextField txtApe;
    private javax.swing.JTextField txtAuxBuscar;
    private javax.swing.JTextField txtCor;
    private javax.swing.JTextField txtDir;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtTel;
    // End of variables declaration//GEN-END:variables
    conectar cc = new conectar();
    Connection cn = cc.conexion();
}
