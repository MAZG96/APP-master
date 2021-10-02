package uca.es.test;

public class Cita {
    private String fecha;
    private int number;
    private int id_paciente;
    private String nombre_paciente;
    private String valoracion;


    public Cita(String f, int number, int id_p){
        this.id_paciente=id_p;
        this.fecha=f;
        this.number=number;

    }

    public Cita(int number,int id_p,String f,String v){
        this.id_paciente=id_p;
        this.fecha=f;
        this.number=number;
        this.valoracion=v;
    }

    public Cita(String f, int number, int id_p,String p,String v){
        this.id_paciente=id_p;
        this.fecha=f;
        this.number=number;
        this.nombre_paciente=p;
        this.valoracion=v;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String f) {
        this.fecha = f;
    }

    public String getNombre_paciente() {
        return nombre_paciente;
    }

    public void setNombre_paciente(String n) {
        this.nombre_paciente = n;
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

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String v) {
        this.valoracion = v;
    }
}
