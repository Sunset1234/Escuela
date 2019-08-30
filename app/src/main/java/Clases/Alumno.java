package Clases;

public class Alumno {

    public String token;
    public String Idsalon;

    public String getIdsalon() {
        return Idsalon;
    }

    public void setIdsalon(String idsalon) {
        Idsalon = idsalon;
    }

    public Alumno() {
    }

    public Alumno(String token, String idsalon) {
        this.token = token;
        Idsalon = idsalon;
    }

    public Alumno(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
