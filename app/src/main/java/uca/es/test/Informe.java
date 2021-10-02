package uca.es.test;

public class Informe {
    private String url;
    private String nombre_archivo;
    private int id;
    private int id_paciente;

    public Informe(int id,int id_paciente,String url,String nombre_archivo){
        this.id=id;
        this.id_paciente=id_paciente;
        this.url=url;
        this.nombre_archivo=nombre_archivo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String texto) {
        this.url = texto;
    }

    public String getNombre_archivo() {
        return nombre_archivo;
    }

    public void setNombre_archivo(String texto) { this.url = texto; }

    public void setId(int number) {
        this.id = number;
    }

    public int getId() {
        return id;
    }

    public void setId_paciente(int number) {
        this.id_paciente = number;
    }

    public int getId_paciente() {
        return id_paciente;
    }
}
