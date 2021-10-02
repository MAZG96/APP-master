package uca.es.test;

public class Respuesta {
    private String texto;
    private int id_paciente;
    private int id_pregunta;
    private int number;

    public Respuesta(String s,int number){
        this.texto=s;
        this.number=number;
    }



    public Respuesta(int number, int id_pregunta, int id_paciente, String texto){
        this.number=number;
        this.id_pregunta=id_pregunta;
        this.id_paciente=id_paciente;
        this.texto=texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public int getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(int id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

}
