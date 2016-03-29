/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Clases;

import java.io.Serializable;

/**
 *
 * @author KathyR
 */
public class Conocimiento implements Serializable{
    
    //Objetos escuela
    private String mesa;
    private String pizarron;
    private String clase;
    private String profesor;
    private String silla;
    //Colores
    private String verde;
    private String morado;
    private String naranja;
    private String azul;
    private String rojo;
    private String amarillo;
    //Números
    private String uno;
    private String dos;
    private String tres;
    private String cuatro;
    private String cinco;
    //Familia
    private String padre;
    private String hermana;
    private String hermano;
    //Partes del cuerpo
    private String pie;
    private String mano;
    private String brazo;
    private String cabeza;
    private String hombro;
    private String pierna;

    public Conocimiento() {
    }

    public Conocimiento(String mesa, String pizarron, String clase, String profesor, String silla, String verde, String morado, String naranja, String azul, String rojo, String amarillo, String uno, String dos, String tres, String cuatro, String cinco, String padre, String hermana, String hermano, String pie, String mano, String brazo, String cabeza, String hombro, String pierna) {
        this.mesa = mesa;
        this.pizarron = pizarron;
        this.clase = clase;
        this.profesor = profesor;
        this.silla = silla;
        this.verde = verde;
        this.morado = morado;
        this.naranja = naranja;
        this.azul = azul;
        this.rojo = rojo;
        this.amarillo = amarillo;
        this.uno = uno;
        this.dos = dos;
        this.tres = tres;
        this.cuatro = cuatro;
        this.cinco = cinco;
        this.padre = padre;
        this.hermana = hermana;
        this.hermano = hermano;
        this.pie = pie;
        this.mano = mano;
        this.brazo = brazo;
        this.cabeza = cabeza;
        this.hombro = hombro;
        this.pierna = pierna;
    }

    public Conocimiento(String padre, String hermana, String hermano) {
        this.padre = padre;
        this.hermana = hermana;
        this.hermano = hermano;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getPizarron() {
        return pizarron;
    }

    public void setPizarron(String pizarron) {
        this.pizarron = pizarron;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getVerde() {
        return verde;
    }

    public void setVerde(String verde) {
        this.verde = verde;
    }

    public String getMorado() {
        return morado;
    }

    public void setMorado(String morado) {
        this.morado = morado;
    }

    public String getNaranja() {
        return naranja;
    }

    public void setNaranja(String naranja) {
        this.naranja = naranja;
    }

    public String getAzul() {
        return azul;
    }

    public void setAzul(String azul) {
        this.azul = azul;
    }

    public String getRojo() {
        return rojo;
    }

    public void setRojo(String rojo) {
        this.rojo = rojo;
    }

    public String getAmarillo() {
        return amarillo;
    }

    public void setAmarillo(String amarillo) {
        this.amarillo = amarillo;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getTres() {
        return tres;
    }

    public void setTres(String tres) {
        this.tres = tres;
    }

    public String getCuatro() {
        return cuatro;
    }

    public void setCuatro(String cuatro) {
        this.cuatro = cuatro;
    }

    public String getCinco() {
        return cinco;
    }

    public void setCinco(String cinco) {
        this.cinco = cinco;
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public String getHermana() {
        return hermana;
    }

    public void setHermana(String hermana) {
        this.hermana = hermana;
    }

    public String getHermano() {
        return hermano;
    }

    public void setHermano(String hermano) {
        this.hermano = hermano;
    }

    public String getPie() {
        return pie;
    }

    public void setPie(String pie) {
        this.pie = pie;
    }

    public String getMano() {
        return mano;
    }

    public void setMano(String mano) {
        this.mano = mano;
    }

    public String getBrazo() {
        return brazo;
    }

    public void setBrazo(String brazo) {
        this.brazo = brazo;
    }

    public String getCabeza() {
        return cabeza;
    }

    public void setCabeza(String cabeza) {
        this.cabeza = cabeza;
    }

    public String getHombro() {
        return hombro;
    }

    public void setHombro(String hombro) {
        this.hombro = hombro;
    }

    public String getPierna() {
        return pierna;
    }

    public void setPierna(String pierna) {
        this.pierna = pierna;
    }

    public String getSilla() {
        return silla;
    }

    public void setSilla(String silla) {
        this.silla = silla;
    }
    
    
}
