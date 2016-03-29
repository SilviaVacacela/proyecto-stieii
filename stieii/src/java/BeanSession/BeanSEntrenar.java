/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanSession;

//import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author silvy
 */
//@Named("beanSEntrenar")
@ManagedBean(name = "beanSEntrenar")
@SessionScoped
public class BeanSEntrenar implements Serializable {

    private Session session;
    private Transaction transaction;

    private int idEntrenamiento;
    private int idPrenguntaEnt;
    private int modelTest;

    private ArrayList columna0;
    private ArrayList columna1;

    public BeanSEntrenar() {
    }

    public void iniciarEntrenamiento(int codigoEntrena) {
        this.idEntrenamiento = codigoEntrena;
        this.session = null;
        this.transaction = null;
        try {
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("idEntrenamiento", this.idEntrenamiento);

        } catch (Exception ex) {
            System.out.println(".........Error al iniciar entrenamiento.........");
        }
    }

    public void finalizar() {
        this.idEntrenamiento = -1;
        this.idPrenguntaEnt = -1;
        this.modelTest = -1;

        this.columna0 = null;
        this.columna1 = null;
//        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//        httpSession.invalidate();
    }

    public int getIdEntrenamiento() {
        return idEntrenamiento;
    }

    public void setIdEntrenamiento(int idEntrenamiento) {
        this.idEntrenamiento = idEntrenamiento;
    }

    public int getIdPrenguntaEnt() {
        return idPrenguntaEnt;
    }

    public void setIdPrenguntaEnt(int idPrenguntaEnt) {
        this.idPrenguntaEnt = idPrenguntaEnt;
    }

    public int getModelTest() {
        return modelTest;
    }

    public void setModelTest(int modelTest) {
        this.modelTest = modelTest;
    }

    public ArrayList getColumna0() {
        return columna0;
    }

    public void setColumna0(ArrayList columna0) {
        this.columna0 = columna0;
    }

    public ArrayList getColumna1() {
        return columna1;
    }

    public void setColumna1(ArrayList columna1) {
        this.columna1 = columna1;
    }

  
}
