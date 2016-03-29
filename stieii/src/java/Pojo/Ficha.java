package Pojo;
// Generated 28/03/2016 11:00:41 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Ficha generated by hbm2java
 */
public class Ficha  implements java.io.Serializable {


     private int idFicha;
     private Tema tema;
     private String nombreFicha;
     private String descripcion;
     private String estadoAprendizaje;
     private boolean estado;
     private Set fichaspreguntas = new HashSet(0);

    public Ficha() {
    }

	
    public Ficha(int idFicha, Tema tema, String nombreFicha, String estadoAprendizaje, boolean estado) {
        this.idFicha = idFicha;
        this.tema = tema;
        this.nombreFicha = nombreFicha;
        this.estadoAprendizaje = estadoAprendizaje;
        this.estado = estado;
    }
    public Ficha(int idFicha, Tema tema, String nombreFicha, String descripcion, String estadoAprendizaje, boolean estado, Set fichaspreguntas) {
       this.idFicha = idFicha;
       this.tema = tema;
       this.nombreFicha = nombreFicha;
       this.descripcion = descripcion;
       this.estadoAprendizaje = estadoAprendizaje;
       this.estado = estado;
       this.fichaspreguntas = fichaspreguntas;
    }
   
    public int getIdFicha() {
        return this.idFicha;
    }
    
    public void setIdFicha(int idFicha) {
        this.idFicha = idFicha;
    }
    public Tema getTema() {
        return this.tema;
    }
    
    public void setTema(Tema tema) {
        this.tema = tema;
    }
    public String getNombreFicha() {
        return this.nombreFicha;
    }
    
    public void setNombreFicha(String nombreFicha) {
        this.nombreFicha = nombreFicha;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getEstadoAprendizaje() {
        return this.estadoAprendizaje;
    }
    
    public void setEstadoAprendizaje(String estadoAprendizaje) {
        this.estadoAprendizaje = estadoAprendizaje;
    }
    public boolean isEstado() {
        return this.estado;
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    public Set getFichaspreguntas() {
        return this.fichaspreguntas;
    }
    
    public void setFichaspreguntas(Set fichaspreguntas) {
        this.fichaspreguntas = fichaspreguntas;
    }



}

