package ch.heig_vd.app.utils;

public class Metadata {
    private final String name;
    private final String content;

    public Metadata(String name, String contenu) {
        this.name = name;
        this.content = contenu;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "name='" + name + '\'' +
                ", contenu='" + content + '\'' +
                '}';
    }
}
