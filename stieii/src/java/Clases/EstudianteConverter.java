/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Pojo.Estudiante;
import Pojo.Unidadensenianza;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author silvy
 */
@FacesConverter(value = "estudianteConverter")
public class EstudianteConverter implements Converter {

    public EstudianteConverter() {
    }

    @Override
    public Estudiante getAsObject(FacesContext context, UIComponent component, String value) {
        String tipo = value;
        System.out.println("el codigo de la unidad getAsObject es: " + tipo);
//        Estudiante unidadE = new BeanRequest.BeanRUnidadE().consultarUnidadPorNombre(tipo);
//        System.out.println("getAsObject el nombre de la unidad recuperada es:" + unidadE.getNombreUnidad());
//        return unidadE;
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            if (value instanceof Pojo.Estudiante) {

                Unidadensenianza m = (Unidadensenianza) value;
                String cadena = Integer.toString(m.getId());
                System.out.println("EL OBJETO RECUPERADO ES CORRECTO Y EL ID ES:" + cadena);
                System.out.println("Y EL OBJETO ES: " + m.getNombreUnidad());
                return m.getNombreUnidad();
            }
            System.out.println("NO ES UNA INSTANCIA DE UNIDADENSENIANZA");
            return "";
        }
        System.out.println("EL VALOR RECUPERADO ES NULO");
        return "";
    }

}
