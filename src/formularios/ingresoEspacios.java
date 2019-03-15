package formularios;

import clases.*;
import java.awt.Color;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ingresoEspacios extends javax.swing.JFrame {

     DefaultTableModel model;
    String insert_update = null;
    String espacio = null;
    ResultSet id_espacio = null;

    public ingresoEspacios() {
        initComponents();
        setTitle("Ingreso de espacios CMD");//esto
        this.setLocationRelativeTo(null);//esto
        limpiar();
        bloquear();
        mostrardatos("");
    }

    void mostrardatos(String valor) {

        String[] titulos = {"Id", "Código", "Descripción", "Ubicación"};
        String[] registros = new String[4];
        String sql = "SELECT id_espacio, cod_espacio, descripcion, ubicacion FROM espacios "
                + "WHERE CONCAT(id_espacio, ' ', cod_espacio, ' ', descripcion, ' ', ubicacion)"
                + " LIKE '%" + valor + "%'"
                + "ORDER BY id_espacio ASC";

        model = new DefaultTableModel(null, titulos);
        conectar cc = new conectar();
        Connection cn = cc.conexion();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registros[0] = rs.getString("id_espacio");
                registros[1] = rs.getString("cod_espacio");
                registros[2] = rs.getString("descripcion");
                registros[3] = rs.getString("ubicacion");
                model.addRow(registros);
            }
            tEspacios.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    void confirmar_eliminar(){
        String botones[] = {"Si", "No"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Desea eliminar "+espacio+"?", "Confirmar eliminar",
                0, 0, null, botones, this);
        if(eleccion == JOptionPane.YES_OPTION){
            deleting();
        }else if(eleccion == JOptionPane.NO_OPTION){
            
        }
    }

    void limpiar() {
        txtId.setText("");
        txtCod.setText("");
        txtDes.setText("");
        txtUbi.setText("");
        txtAuxBuscar.setText("");
    }

    void bloquear() {
        Color azul = new Color (184, 207, 229);
        txtId.setEnabled(false);
        txtCod.setEnabled(false);
        txtDes.setEnabled(false);
        txtUbi.setEnabled(false);
        txtCod.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtDes.setBorder(BorderFactory.createLineBorder(azul, 1));
        txtUbi.setBorder(BorderFactory.createLineBorder(azul, 1));
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    void desbloquear() {
        txtId.setEnabled(true);
        txtCod.setEnabled(true);
        txtDes.setEnabled(true);
        txtUbi.setEnabled(true);
        txtCod.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtDes.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtUbi.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }
    
    void validating() {
        int cont = 0;
        
        if ("".equals(txtUbi.getText())) {
            txtUbi.requestFocus();
            txtUbi.setToolTipText("El nombre no puede quedar vacío");
            txtUbi.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtUbi.setToolTipText(null);
            txtUbi.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
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
        
        if ("".equals(txtCod.getText())) {
            txtCod.requestFocus();
            txtCod.setToolTipText("El código no puede quedar vacío"); 
            txtCod.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            cont = cont + 1;
        } else {
            txtCod.setToolTipText(null);
            txtCod.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
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
            PreparedStatement pst = cn.prepareStatement("INSERT INTO espacios(cod_espacio,descripcion,ubicacion) VALUES (?,?,?)");
            pst.setString(1, txtCod.getText());
            pst.setString(2, txtDes.getText());
            pst.setString(3, txtUbi.getText());

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
            PreparedStatement pst = cn.prepareStatement("UPDATE espacios SET "
                    + "cod_espacio='" + txtCod.getText() + "',"
                    + "descripcion='" + txtDes.getText() + "',"
                    + "ubicacion='" + txtUbi.getText() + "'  "
                    + "WHERE id_espacio='" + txtId.getText() + "'");
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
        
        String sql = "SELECT id_espacio FROM espacios WHERE id_espacio = "+id;
        try {
            Statement st = cn.createStatement();
            id_espacio = st.executeQuery(sql);          
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);            
        } 

        try {
            PreparedStatement pst = cn.prepareStatement("DELETE FROM espacios WHERE  id_espacio='" + id + "'");
            pst.executeUpdate();
            mostrardatos("");
        } catch (Exception ex) {            
             if(id_espacio != null){
            JOptionPane.showMessageDialog(null, "No es posible eliminar este espacio, ya que su ID esta "
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
        txtCod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDes = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtUbi = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
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
        setMinimumSize(null);

        jPanel2.setBackground(new java.awt.Color(153, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Catálogo de Espacios"));
        jPanel2.setForeground(new java.awt.Color(153, 0, 0));

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
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresar o editar Espacio"));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Codigo");

        txtCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Descripcion");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Ubicacion");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUbi)
                            .addComponent(txtId, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(txtCod)
                            .addComponent(txtDes)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLimpiar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUbi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAuxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMostrarDatos)
                .addGap(113, 113, 113))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAuxBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarDatos)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.getAccessibleContext().setAccessibleName("Detalle Espacio");
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        jLabel6.setText("Usuario conectado:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbusu, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbusu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
        txtCod.setText(tEspacios.getValueAt(fila, 1).toString());
        txtDes.setText(tEspacios.getValueAt(fila, 2).toString());
        txtUbi.setText(tEspacios.getValueAt(fila, 3).toString());
        txtId.enable(false);
    } else {
        JOptionPane.showMessageDialog(null, "No selecionó fila");
    }
    insert_update = "update";
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    int fila = tEspacios.getSelectedRow();    
    espacio = tEspacios.getValueAt(fila, 2).toString();
    confirmar_eliminar();
}//GEN-LAST:event_jMenuItem2ActionPerformed

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodActionPerformed

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
        txtCod.requestFocus();
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

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ingresoEspacios().setVisible(true);
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
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    protected static javax.swing.JLabel lbusu;
    private javax.swing.JTable tEspacios;
    private javax.swing.JTextField txtAuxBuscar;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtDes;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtUbi;
    // End of variables declaration//GEN-END:variables
    conectar cc = new conectar();
    Connection cn = cc.conexion();
}
