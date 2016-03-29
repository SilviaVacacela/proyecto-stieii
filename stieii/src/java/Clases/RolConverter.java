/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Pojo.Rol;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author KathyR
 */
@FacesConverter(value = "rolConverter")
public class RolConverter implements Converter {

//    private List<Rol> listaRoles;
    public RolConverter() {

    }

    @Override
    public Rol getAsObject(FacesContext context, UIComponent component, String value) {
        String tipo = value;
//        String[] cadena= tipo.split(".");
//        System.out.println("Cadena posicion1:"+cadena[0]);
        Rol rol = new BeanRequest.BeanRRol().consultarRolPorTipo(tipo);
        return rol;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            if (value instanceof Pojo.Rol) {
                Rol m = (Rol) value;
                return m.getTipo();
            }
            System.out.println("NO ES UN OBJETO");
            return "";
        }
        System.out.println("esta vacío");
        return "";
    }
}
