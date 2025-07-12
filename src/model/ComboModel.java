
package model;

public class ComboModel {
    private int id;
    private String campo1;
    private String campo2;

    public ComboModel(int id, String campo1, String campo2) {
        this.id = id;
        this.campo1 = campo1;
        this.campo2 = campo2;
    }

    public int getId() {
        return id;
    }

    public String getCampo1() {
        return campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    @Override
    public String toString() {
        return campo1 + " " + campo2;
    }
}
