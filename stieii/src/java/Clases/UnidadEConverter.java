/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Pojo.Unidadensenianza;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author silvy
 */
@FacesConverter(value = "unidadEConverter")
public class UnidadEConverter implements Converter {

     public UnidadEConverter() {
    }

    @Override
    public Unidadensenianza getAsObject(FacesContext context, UIComponent component, String value) {
//        int tipo=Integer.parseInt(value);
        String tipo = value;
        System.out.println("el codigo de la unidad getAsObject es: "+tipo);
        Unidadensenianza unidadE = new BeanRequest.BeanRUnidadE().consultarUnidadPorNombre(tipo);
//        Unidadensenianza unidadE = new BeanRequest.BeanRUnidadE().consultarUnidadPorCodigo(tipo);
        System.out.println("getAsObject el nombre de la unidad recuperada es:"+unidadE.getNombreUnidad());
        return unidadE;
    }

  
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            if (value instanceof Pojo.Unidadensenianza) {
                
                Unidadensenianza m = (Unidadensenianza) value;
                String cadena=Integer.toString(m.getId());
                System.out.println("EL OBJETO RECUPERADO ES CORRECTO Y EL ID ES:"+cadena);
                System.out.println("Y EL OBJETO ES: "+m.getNombreUnidad());
                return m.getNombreUnidad();
            }
            System.out.println("NO ES UNA INSTANCIA DE UNIDADENSENIANZA");
            return "";
        }
        System.out.println("EL VALOR RECUPERADO ES NULO");
        return "";
    }
    
    
}
