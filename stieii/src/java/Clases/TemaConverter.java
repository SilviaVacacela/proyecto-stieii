/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import BeanRequest.BeanRTema;
import Pojo.Tema;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author silvy
 */
@FacesConverter(value = "temaConverter")
public class TemaConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String tipo = value;
        System.out.println("el codigo de la tema getAsObject es: "+tipo);
        Tema tema= new BeanRTema().consultarTemaPorNombre(tipo);
        System.out.println("getAsObject el nombre de la unidad recuperada es:"+tema.getNombre());
        return tema;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            if (value instanceof Pojo.Tema) {
                Tema t=(Tema)value;
                String cadena=Integer.toString(t.getIdTema());
                System.out.println("EL OBJETO RECUPERADO ES CORRECTO Y EL ID ES:"+cadena);
                System.out.println("Y EL OBJETO ES: "+t.getNombre());
                return t.getNombre();
            }
            System.out.println("NO ES UNA INSTANCIA DE TEMA");
            return "";
        }
        System.out.println("EL VALOR RECUPERADO ES NULO");
        return ""; 
    }
    
}
