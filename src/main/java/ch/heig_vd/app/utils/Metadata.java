package ch.heig_vd.app.utils;

public class Metadata {
    private final String name;
    private final String contenu;

    public Metadata(String name, String contenu) {
        this.name = name;
        this.contenu = contenu;
    }

    public String getName() {
        return name;
    }

    public String getContenu() {
        return contenu;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "name='" + name + '\'' +
                ", contenu='" + contenu + '\'' +
                '}';
    }
}
