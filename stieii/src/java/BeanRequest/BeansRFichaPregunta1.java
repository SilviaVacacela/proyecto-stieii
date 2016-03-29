/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import BeanSession.BeanSEntrenar;
import Dao.DaoEntrenar;
import Dao.DaoFicha;
import Dao.DaoFichaPregunta;
import Dao.DaoPreguntaEntrenar;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Ficha;
import Pojo.Fichaspregunta;
import Pojo.Preguntaentrenar;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@ViewScoped
public class BeansRFichaPregunta1 {

    private Session session;
    private Transaction transaction;
    private Fichaspregunta fichaPregunta;
    //llamar al bean de ssesion para establecer datos.
    @ManagedProperty("#{beanSEntrenar}")
    private BeanSEntrenar beanSEntrena;
    //Utilizado en el metodo (listaFichasPregunta)
    private List<Fichaspregunta> listFichasPregunta;
    private List<Ficha> listFichas;


    public BeansRFichaPregunta1() {
        fichaPregunta = new Fichaspregunta();

    }

    @PostConstruct
    public void init() {
        //llamamos al metodo para crear fichas con los atrinutos del sesion
        crearFichasPreguntas(beanSEntrena.getIdEntrenamiento(), beanSEntrena.getIdPrenguntaEnt());

    }

    public void crearFichasPreguntas(int idEntrenamiento, int idPrenguntaEnt) {
        System.out.println("id entrenamiento ..........................." + idEntrenamiento);
        System.out.println("id preguntaEntrenar..........................." + idPrenguntaEnt);

        int sizeListaFicha = obtnerListaFichas(idEntrenamiento);
        ArrayList listaAleatorio;
        //si sizeListaFicha(tamaño de la lista de fichas) es diferente de cero(exista fichas)
        System.out.println("tamaño de la lista de fichas ..." + sizeListaFicha + "...............");
        if (sizeListaFicha >= 4) {
            int numAle;
            int numFich;
            //obtiene n(num) numeros aleatorios
            int num = sizeListaFicha - 1;
            listaAleatorio = generarAleatoreo(0, num, 4);
            for (int i = 0; i < 4; i++) {
                numAle = (int) listaAleatorio.get(i);
                numFich = listFichas.get(numAle).getIdFicha();
                System.out.println(i + "creacion de la ficha en la posicion:[" + numAle + "] con el id: " + numFich);
                //aqui  registrar(FichaPregunta) la fija con (idFicha, idPreguntaEntrenar)
                registrarFichaPregunta(numFich, idPrenguntaEnt);
            }

            //metodo para obtnere la lista de fichasPregunta Creadas anteriormente
            listaFichasPregunta(idPrenguntaEnt); //se inicializa---listFichasPregunta---para retornar

            //...................MODELO DEL TEST EN BEANS SESSION....................................
            //aqui se debe obtner un numero aleatorio y guardar beanSEntrenar
            int modelTest = numeroAleatorTEST(1, 6); //obtner el numero del modelo Test1
            this.beanSEntrena.setModelTest(modelTest);
            //metodo para crear un modelo del test1
            elegirModelTEST(modelTest);

        } else {
            System.out.println("(NO HAY SUFUCIENTE O NO EXITE )lista de fichas, para este tema");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "NO HAY SUFUCIENTE O NO EXITE )lista de fichas, para este tema"));
//            //se debe volver a la pagina inicioAprendizaje.xhtml
        }
    }
//

    public int obtnerListaFichas(int idEntrenamiento) {
        int sizeListaFichaPregunta = 0;
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener el entrenamiento mediante el id, para obtner el TEMA
            DaoEntrenar daoEntrenar = new DaoEntrenar();
            Entrenamiento entrenar = daoEntrenar.verPorCodigoEntrenamiento(session, idEntrenamiento);

            //obtener lista de fichas por le Tema segun el entrenamiento
            DaoFicha daoficha = new DaoFicha();
            listFichas = daoficha.verListfichasActivasPorTema(session, entrenar.getTema().getIdTema());
            this.transaction.commit();

            //fijar el numero de fijas que se obtiene de la listFichas
            sizeListaFichaPregunta = listFichas.size();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL OBTENER LISTA DE FICHAS:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return sizeListaFichaPregunta;
    }
//

    public ArrayList generarAleatoreo(int valorInicial, int valorFinal, int numAleatorio) {

        ArrayList listaNumero = new ArrayList();

        for (int i = 0; i < numAleatorio;) {
            int numero = (int) (Math.random() * (valorFinal - valorInicial + 1) + valorInicial);//genero un numero
            if (listaNumero.isEmpty()) {//si la lista esta vacia
                listaNumero.add(numero);
                i++;
            } else {//si no esta vacia
                if (listaNumero.contains(numero)) {//Si el numero que generé esta contenido en la lista
                } else {//Si no esta contenido en la lista
                    listaNumero.add(numero);
                    i++;
                }
            }
        }
        System.out.println(".........................." + listaNumero + ".........................");

        return listaNumero;
    }

    public void registrarFichaPregunta(int idFicha, int idPreguntaEntrena) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener una ficha por su id y fijar en el Fichaspregunta.
            DaoFicha daoficha = new DaoFicha();
            Ficha ficha = daoficha.verPorCodigoFicha(session, idFicha);
            this.fichaPregunta.setFicha(ficha);

            //obtener una pregunta por su id y fijar en el Fichaspregunta.
            DaoPreguntaEntrenar daoPreguntaEntrenar = new DaoPreguntaEntrenar();
            Preguntaentrenar preguntaEntrenar = daoPreguntaEntrenar.verPorCodigoPreguntaEntrenar(session, idPreguntaEntrena);
            this.fichaPregunta.setPreguntaentrenar(preguntaEntrenar);

            //para crear Fichaspregunta (lista de fichas en base a un preguntadeENTRENAMIENTO)
            DaoFichaPregunta daofichaPregunta = new DaoFichaPregunta();
            daofichaPregunta.registrarFichaPregunta(session, this.fichaPregunta);//Devuelve TRUE al crear una fichaPregunta
            this.transaction.commit();

            System.out.println("Correcto: El registro de las fichasPregunta (TEST1)se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL REGISTRA FICHA_PREGUNTA(test1):", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }

    }

    public void listaFichasPregunta(int idPrenguntaEnt) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para crear Fichaspregunta (lista de fichas en base a un preguntadeENTRENAMIENTO)
            DaoFichaPregunta daofichaPregunta = new DaoFichaPregunta();
            listFichasPregunta = daofichaPregunta.verPreguntaEntrenamiento(session, idPrenguntaEnt);
            this.transaction.commit();

            System.out.println("Correcto: El obtner de las fichasPregunta (TEST1)se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL obtner FICHA_PREGUNTA (TEST1):", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    //OBtner los id de las fichas..........
    public int idFicha1() {
        return listFichasPregunta.get(0).getFicha().getIdFicha();
    }

    public int idFicha2() {
        return listFichasPregunta.get(1).getFicha().getIdFicha();
    }

    public int idFicha3() {
        return listFichasPregunta.get(2).getFicha().getIdFicha();
    }

    public int idFicha4() {
        return listFichasPregunta.get(3).getFicha().getIdFicha();
    }

    //OBtner los nombres de las fichas..........
    public String nameFicha1() {
        int id = listFichasPregunta.get(0).getFicha().getIdFicha();
        return obtnerNameficha(id);
    }

    public String nameFicha2() {
        int id = listFichasPregunta.get(1).getFicha().getIdFicha();
        return obtnerNameficha(id);
    }

    public String nameFicha3() {
        int id = listFichasPregunta.get(2).getFicha().getIdFicha();
        return obtnerNameficha(id);
    }

    public String nameFicha4() {
        int id = listFichasPregunta.get(3).getFicha().getIdFicha();
        return obtnerNameficha(id);
    }

    //metodo para obtner NOMBRE de la ficha por su id
    public String obtnerNameficha(int idFich) {
        String nombreFicha = "empty";
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para crear Fichaspregunta (lista de fichas en base columna1 un preguntadeENTRENAMIENTO)
            DaoFicha daofichaPregunta = new DaoFicha();
            Ficha ficha = daofichaPregunta.verPorCodigoFicha(session, idFich);
            this.transaction.commit();
            nombreFicha = ficha.getNombreFicha();

            System.out.println("Correcto: al obtner el nombre (fichaPregunta)se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL obtner FICHA en el Test Emparejamiento:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return nombreFicha;
    }

    public int numeroAleatorTEST(int valorInicial, int valorFinal) {
        ArrayList listaNumero = new ArrayList();
        int numero = (int) (Math.random() * (valorFinal - valorInicial + 1) + valorInicial);//genero un numero
        listaNumero.add(numero);
        return (int) listaNumero.get(0);
    }

    public void elegirModelTEST(int num) {
        System.out.println("modelTest(Dashboard) numero ....................." + num);
        int mt1[] = {1, 2, 3};
        int mt2[] = {2, 3, 1};
        int mt3[] = {3, 1, 2};
        int imgM[]; //elegir el modelo del dashboard(test 1-6) (imagen)
        int sonM[]; //elegir el modelo del dashboard(test 1-6) (sonido)
        switch (num) {
            case 1:
                imgM = mt1;
                sonM = mt2;
                break;
            case 2:
                imgM = mt1;
                sonM = mt3;
                break;
            case 3:
                imgM = mt2;
                sonM = mt1;
                break;
            case 4:
                imgM = mt2;
                sonM = mt3;
                break;
            case 5:
                imgM = mt3;
                sonM = mt1;
                break;
            default:
                imgM = mt3;
                sonM = mt2;
                break;
        }
        //columnas del test: columna 0 y 1
        ArrayList columna0 = new ArrayList();
        ArrayList columna1 = new ArrayList();

        for (int i = 0; i <= 2; i++) {
            columna0.add("imagen" + imgM[i]);
            columna1.add("sonido" + sonM[i]);
        }

        columna1.remove(2);

        //fijo las columna en el beans SESSION
        this.beanSEntrena.setColumna0(columna0);
        this.beanSEntrena.setColumna1(columna1);

    }


    public Fichaspregunta getFichaPregunta() {
        return fichaPregunta;
    }

    public void setFichaPregunta(Fichaspregunta fichaPregunta) {
        this.fichaPregunta = fichaPregunta;
    }

    public BeanSEntrenar getBeanSEntrena() {
        return beanSEntrena;
    }

    public void setBeanSEntrena(BeanSEntrenar beanSEntrena) {
        this.beanSEntrena = beanSEntrena;
    }

    public List<Fichaspregunta> getListFichasPregunta() {
        return listFichasPregunta;
    }

    public void setListFichasPregunta(List<Fichaspregunta> listFichasPregunta) {
        this.listFichasPregunta = listFichasPregunta;
    }

    public List<Ficha> getListFichas() {
        return listFichas;
    }

    public void setListFichas(List<Ficha> listFichas) {
        this.listFichas = listFichas;
    }

  

}
