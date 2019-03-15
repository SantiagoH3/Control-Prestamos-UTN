
package formularios;

import clases.*;
import java.awt.Color;
import java.sql.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ingresoEquipos extends javax.swing.JFrame {

    DefaultTableModel model;
    String insert_update = null;
    String equipo = null;
    ResultSet id_equipo = null;
    

    public ingresoEquipos() {
        initComponents();
        setTitle("Ingreso de equipos y/o material");//esto
        this.setLocationRelativeTo(null);//esto
        limpiar();
        bloquear();
        mostrardatos("");
    }

    void mostrardatos(String valor) {

        String[] titulos = {"Id", "Nombre", "Descripción", "Cantidad", "Marca"};
        String[] registros = new String[5];
        String sql = "SELECT id_equipo, nombre, descripcion, cantidad, marca FROM equipos "
                + "WHERE CONCAT(id_equipo, ' ', nombre, ' ', descripcion, "
                + "' ', cantidad, ' ', marca)"
                + " LIKE '%" + valor + "%'"
                + "ORDER BY id_equipo ASC";

        model = new DefaultTableModel(null, titulos);
        conectar cc = new conectar();
        Connection cn = cc.conexion();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registros[0] = rs.getString("id_equipo");
                registros[1] = rs.getString("nombre");
                registros[2] = rs.getString("descripcion");
                registros[3] = rs.getString("cantidad");
                registros[4] = rs.getString("marca");
                model.addRow(registros);
            }
            tEspacios.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    void confirmar_eliminar(){
        String botones[] = {"Si", "No"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Desea eliminar "+equipo+"?", "Confirmar eliminar",
                0, 0, null, botones, this);
        if(eleccion == JOptionPane.YES_OPTION){
            deleting();
        }else if(eleccion == JOptionPane.NO_OPTION){
            
        }
    }

    void limpiar() {
        txtId.setText("");
        txtNom.setText("");
        txtDes.setText("");
        txtCant.setText("");
        txtMar.setText("");
        txtAuxBuscar.setText("");
    }

    void bloquear() {
        Color azul = new Color (184, 207, 229);
        txtId.setEnabled(false);
        txtNom.setEnabled(false);        
        txtDes.setEnabled(false);        
        txtCant.setEnabled(false);        
        txtMar.setEnabled(false);
        txtNom.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtDes.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtCant.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtMar.setBorder(BorderFactory.createLineBorder(azul, 1));
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    void desbloquear() {
        txtId.setEnabled(true);
        txtNom.setEnabled(true);
        txtDes.setEnabled(true);         
        txtCant.setEnabled(true);
        txtMar.setEnabled(true);
        txtNom.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtDes.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtCant.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtMar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }
    
    void validating() {
        int cont = 0;               
        
        if ("".equals(txtMar.getText())) {
            txtMar.requestFocus();
            txtMar.setToolTipText("La marca no puede quedar vacía"); 
            txtMar.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtMar.setToolTipText(null);
            txtMar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if ("".equals(txtCant.getText())) {
            txtCant.requestFocus();
            txtCant.setToolTipText("La cantidad no puede quedar vacía");
            txtCant.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        } else {
            txtCant.setToolTipText(null);
            txtCant.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if ("".equals(txtDes.getText())) {
            txtDes.requestFocus();
            txtDes.setToolTipText("La descripcion no puede quedar vacía");
            txtDes.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtDes.setToolTipText(null);
            txtDes.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
        
        if ("".equals(txtNom.getText())) {
            txtNom.requestFocus();
            txtNom.setToolTipText("El nombre no puede quedar vacío");
            txtNom.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtNom.setToolTipText(null);
            txtNom.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
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
        try {
            PreparedStatement pst = cn.prepareStatement("INSERT INTO equipos(nombre,descripcion,cantidad, marca) VALUES (?,?,?,?)");
            pst.setString(1, txtNom.getText());
            pst.setString(2, txtDes.getText());
            pst.setString(3, txtCant.getText());
            pst.setString(4, txtMar.getText());

            pst.executeUpdate();
            mostrardatos("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        limpiar();
        insert_update = "NULL";
    }

    void updating() {
        try {
            PreparedStatement pst = cn.prepareStatement("UPDATE equipos SET "
                    + "nombre='" + txtNom.getText() + "',"
                    + "descripcion='" + txtDes.getText() + "',"
                    + "cantidad='" + txtCant.getText() + "',  "
                    + "marca='" + txtMar.getText() + "'  "
                    + "WHERE id_equipo='" + txtId.getText() + "'");
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
        
        String sql = "SELECT id_equipo FROM equipos WHERE id_equipo = "+id;
        try {
            Statement st = cn.createStatement();
            id_equipo  = st.executeQuery(sql);           
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);            
        }                

        try {
            PreparedStatement pst = cn.prepareStatement("DELETE FROM equipos WHERE  id_equipo='" + id + "'");
            pst.executeUpdate();
            mostrardatos("");
        } catch (Exception ex) {
            if(id_equipo != null){
            JOptionPane.showMessageDialog(null, "No es posible eliminar este equipo, ya que su ID esta "
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tEspacios = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtAuxBuscar = new javax.swing.JTextField();
        btnMostrarDatos = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNom = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDes = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCant = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtMar = new javax.swing.JTextField();
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
        setForeground(new java.awt.Color(153, 0, 0));

        jPanel2.setBackground(new java.awt.Color(153, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Catálogo de Equipos"));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setAutoscrolls(true);

        jScrollPane1.setBackground(new java.awt.Color(153, 0, 0));

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

        btnMostrarDatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Buscar16x16.png"))); // NOI18N
        btnMostrarDatos.setText("Mostrar datos");
        btnMostrarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarDatosActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresar o editar Equipo"));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nombre");

        txtNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Descripcion");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cantidad");

        txtCant.setName(""); // NOI18N
        txtCant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantKeyTyped(evt);
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
        jLabel6.setText("Marca");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLimpiar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
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
                                .addGap(29, 29, 29)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtMar, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(txtCant, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNom, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDes, javax.swing.GroupLayout.Alignment.LEADING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtCant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtMar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnLimpiar)
                    .addComponent(btnVolver))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAuxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMostrarDatos))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbusu, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lbusu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

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
        txtNom.setText(tEspacios.getValueAt(fila, 1).toString());
        txtDes.setText(tEspacios.getValueAt(fila, 2).toString());
        txtCant.setText(tEspacios.getValueAt(fila, 3).toString());
        txtMar.setText(tEspacios.getValueAt(fila, 4).toString());
        txtId.enable(false);
    } else {
        JOptionPane.showMessageDialog(null, "No selecionó fila");
    }
    insert_update = "update";
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    int fila = tEspacios.getSelectedRow();    
    equipo = tEspacios.getValueAt(fila, 1).toString();
    confirmar_eliminar();    
}//GEN-LAST:event_jMenuItem2ActionPerformed

    private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomActionPerformed

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
        txtNom.requestFocus();
        txtId.enable(false);
        insert_update = "insert";
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        bloquear();
        limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCantKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantKeyTyped
       char caracter = evt.getKeyChar();

      // Verificar si la tecla pulsada no es un digito
      if(((caracter < '0') ||
         (caracter > '9')) &&
         (caracter != '\b' /*corresponde a BACK_SPACE*/))
      {
         evt.consume();  // ignorar el evento de teclado
      }
      if(txtCant.getText().length()>=3){
         evt.consume();  // ignorar el evento de teclado
      }
    }//GEN-LAST:event_txtCantKeyTyped

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ingresoEquipos().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMostrarDatos;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnVolver;
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
    private javax.swing.JTextField txtCant;
    private javax.swing.JTextField txtDes;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtMar;
    private javax.swing.JTextField txtNom;
    // End of variables declaration//GEN-END:variables
    conectar cc = new conectar();
    Connection cn = cc.conexion();
}
