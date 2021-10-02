package uca.es.test;



public class Paciente {
    private int id;
    private String nombre;
    private String apellidos;
    private String fecha_nacimiento;
    private String telefono;
    private String pass;

    public Paciente(int id,String n,String a,String f,String t){
        this.id=id;
        this.nombre=n;
        this.apellidos=a;
        this.fecha_nacimiento=f;
        this.telefono=t;
    }

    public Paciente(int id,String n,String a,String f,String t,String p){
        this.id=id;
        this.nombre=n;
        this.apellidos=a;
        this.fecha_nacimiento=f;
        this.telefono=t;
        this.pass=p;
    }
    public Paciente(String n,String a,String f,String t){
        this.nombre=n;
        this.apellidos=a;
        this.fecha_nacimiento=f;
        this.telefono=t;
    }

    public int getId(){return id;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
