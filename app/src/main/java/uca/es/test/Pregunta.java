package uca.es.test;

public class Pregunta {
    private String texto;
    private int number;
    private String respuesta;
    private int seccion;
    private int tipo;

    public Pregunta(String s,int number,int seccion,int tipo){
        this.texto=s;
        this.number=number;
        this.respuesta="";
        this.seccion=seccion;
        this.tipo=tipo;
    }

    public Pregunta(String s,int number,int seccion,String resp){
        this.texto=s;
        this.number=number;
        this.respuesta=resp;
        this.seccion=seccion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String res) {
        this.respuesta = res;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSeccion() {
        return seccion;
    }

    public int getTipo() {
        return tipo;
    }


}
