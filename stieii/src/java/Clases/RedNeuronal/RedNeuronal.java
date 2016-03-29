/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases.RedNeuronal;

import Pojo.Entrenamiento;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author silvy
 */
public class RedNeuronal {

    private String resultado;
    private String imgResultado;
    private String imgREDneuronal;

    private String nivelSigEntrena;

    public void redNeuronal(int puntaje, int tiempo, int error, String nivel) throws Exception {
        //si puntaje >= 200 entonces aprendido
        //si tiempo <= 240 (4 minutos) entonces aprendido
        //si errores <= 3 entonces aprendido
        String[] dato = {obtnerPuntaje(puntaje, nivel), obtenerTiempo(tiempo), obtenerErrores(error, nivel)};

        ConverterUtils.DataSource con = new ConverterUtils.DataSource("C:\\Users\\USUARIO\\Documents\\SILVIIS\\10 Modulo\\2.ANTEPROYECTOS DE TESIS\\Proyecto\\Aplicacion\\redeAprendizaje.arff");
//        ConverterUtils.DataSource con = new ConverterUtils.DataSource("E:\\Unl\\10 Modulo\\2.ANTEPROYECTOS DE TESIS\\Proyecto\\Aplicacion\\redeAprendizaje.arff");

        Instances instances = con.getDataSet();
        System.out.println("...........................................");
        System.out.println(instances);
        System.out.println("...........................................");
        instances.setClassIndex(instances.numAttributes() - 1);

        MultilayerPerceptron mp = new MultilayerPerceptron();
        mp.buildClassifier(instances);

        Evaluation evalucion = new Evaluation(instances);
        evalucion.evaluateModel(mp, instances);
        System.out.println(evalucion.toSummaryString());
        System.out.println(evalucion.toMatrixString());

        String datosEntrada = null;
        String datosSalida = "no se puede predecir";
        for (int i = 0; i < instances.numInstances(); i++) {
            double predecido = mp.classifyInstance(instances.instance(i));
            System.out.println(" ...................."+ predecido+ "........... "+i);
            datosEntrada = dato[0] + " " + dato[1] + " " + dato[2];
            if ((int) instances.instance(i).value(0) == Integer.parseInt(dato[0])
                    && (int) instances.instance(i).value(1) == Integer.parseInt(dato[1])
                    && (int) instances.instance(i).value(2) == Integer.parseInt(dato[2])) {
                datosSalida = instances.classAttribute().value((int) predecido);
                System.out.println(" salida...................."+ datosSalida+ "........... "+i);
            }
        }
        System.out.println("DATOS DE ENTRADA: " + datosEntrada);
        System.out.println("SALIDA PREDECIDA: " + datosSalida);

        switch (datosSalida) {
            case "0":
                resultado = "Excelente ha aprendido";
                imgResultado = "Excelente.jpg";
                imgREDneuronal = "0.png";
                System.out.println("Excelente ha aprendido");
                break;
            case "1":
                resultado = "Disminuir Errores";
                imgResultado = "Bueno.jpg";
                imgREDneuronal = "1.png";
                System.out.println("Disminuir Errores");
                break;
            case "2":
                resultado = "Disminuir Tiempo";
                imgResultado = "Bueno.jpg";
                imgREDneuronal = "2.png";
                System.out.println("Disminuir Tiempo");
                break;
            case "3":
                resultado = "Disminuir Errores y tiempo";
                imgResultado = "Bueno.jpg";
                imgREDneuronal = "3.png";
                System.out.println("Disminuir Errores y tiempo");
                break;
            case "4":
                resultado = "Subir Puntaje";
                imgResultado = "pensando.jpg";
                imgREDneuronal = "4.png";
                System.out.println("Subir Puntaje");
                break;
            case "5":
                resultado = "Subir Puntaje y disminuir Errores";
                imgResultado = "pensando.jpg";
                imgREDneuronal = "5.png";
                System.out.println("Subir Puntaje y disminuir Errores");
                break;
            case "6":
                resultado = "Subir Puntaje y disminuir Tiempo";
                imgResultado = "pensando.jpg";
                imgREDneuronal = "6.png";
                System.out.println("Subir Puntaje y disminuir Tiempo");
                break;
            case "7":
                resultado = "Ponle mas Empeño";
                imgResultado = "pensando.jpg";
                imgREDneuronal = "7.png";
                System.out.println("Ponle mas Empeño");
                break;
            default:
                resultado = "Verifique entradas, no se puede predecir";
                imgResultado = "Error.jpg";
                System.out.println("Verifique entradas, no se puede predecir");
                break;
        }

        //switch se utiliza para obtener el nivel del siguiente entrenamiento
        switch (nivel) {
            case "facil":
                if (resultado.equals("Excelente ha aprendido")||resultado.equals("Disminuir Tiempo")) {
                    nivelSigEntrena = "medio";
                } else {
                    nivelSigEntrena = "facil";
                }
                break;
            case "medio":
                if (resultado.equals("Excelente ha aprendido")||resultado.equals("Disminuir Tiempo")) {
                    nivelSigEntrena = "dificil";
                } else {
                    if (resultado.equals("Disminuir Errores")||resultado.equals("Disminuir Errores y tiempo")) {
                        nivelSigEntrena = "medio";
                    } else {
                        nivelSigEntrena = "facil";
                    }
                }
                break;
            case "dificil":
                 if (resultado.equals("Excelente ha aprendido")||resultado.equals("Disminuir Tiempo")) {
                    nivelSigEntrena = "dificil";
                } else {
                    nivelSigEntrena = "medio";
                }
                break;
            default: //por defecto esta el caso facil
                if (resultado.equals("Excelente ha aprendido")) {
                    nivelSigEntrena = "medio";
                } else {
                    nivelSigEntrena = "facil";
                }
                break;
        }
    }

    public String obtnerPuntaje(int punto, String nivel) {
        int valorPuntos;
        switch (nivel) {
            case "facil":
                if (punto >= 850) {
                    valorPuntos = 1;
                } else {
                    valorPuntos = 0;
                }
                break;
            case "medio":
                if (punto >= 1150) {
                    valorPuntos = 1;
                } else {
                    valorPuntos = 0;
                }
                break;
            case "dificil":
                if (punto >= 1260) {
                    valorPuntos = 1;
                } else {
                    valorPuntos = 0;
                }
                break;
            default:
                if (punto >= 875) {
                    valorPuntos = 1;
                } else {
                    valorPuntos = 0;
                }
                break;
        }

        String puntaje = "" + valorPuntos;
        return puntaje;
    }

    public String obtenerTiempo(int tiempo) {
        int valorTiempo;
        if (tiempo >= 240) { //mayor a 120(4 minutos)
            valorTiempo = 0;
        } else {
            valorTiempo = 1;
        }
        String tiemposEntrenar = "" + valorTiempo;
        return tiemposEntrenar;
    }

    public String obtenerErrores(int errors, String nivel) {
        int valorError;
        switch (nivel) {
            case "facil":
                if (errors > 4) {
                    valorError = 0;
                } else {
                    valorError = 1;
                }
                break;
            case "medio":
                if (errors >= 4) {
                    valorError = 0;
                } else {
                    valorError = 1;
                }
                break;
            case "dificil":
                if (errors >= 4) {
                    valorError = 0;
                } else {
                    valorError = 1;
                }
                break;
            default:
                if (errors > 4) {
                    valorError = 0;
                } else {
                    valorError = 1;
                }
                break;
        }

        String erroresEntrenar = "" + valorError;
        return erroresEntrenar;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getImgResultado() {
        return imgResultado;
    }

    public void setImgResultado(String imgResultado) {
        this.imgResultado = imgResultado;
    }

    public String getImgREDneuronal() {
        return imgREDneuronal;
    }

    public void setImgREDneuronal(String ImgREDneuronal) {
        this.imgREDneuronal = ImgREDneuronal;
    }

    public String getNivelSigEntrena() {
        return nivelSigEntrena;
    }

    public void setNivelSigEntrena(String nivelSigEntrena) {
        this.nivelSigEntrena = nivelSigEntrena;
    }

}
