package Clases;

public class Alerta {

    public String Titulo;
    public String Descripcion;

    public Alerta(String titulo) {
        Titulo = titulo;
    }

    public Alerta(String titulo, String descripcion) {
        Titulo = titulo;
        Descripcion = descripcion;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
