
package clases;

import com.toedter.calendar.JDateChooser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Funciones {
   //Date ahora = new java.util.Date();
      SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public String getFecha(JDateChooser dc){
        if(dc.getDate()!=null){
            return formato.format(dc.getDate());
        } else {
            return null;
        }
    }
    
    public java.util.Date StringADate(String fecha) {
        SimpleDateFormat formato_del_Texto = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date fechaE = null;
        try {
            fechaE = formato_del_Texto.parse(fecha);
            return fechaE;
        } catch (ParseException ex) {
            return null;
        }
    } 
}
