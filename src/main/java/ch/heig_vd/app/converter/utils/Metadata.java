package ch.heig_vd.app.converter.utils;

import java.util.Objects;

/**
 * @author Marco Maziero
 * @author Besseau Léonard
 */
public class Metadata {
    private final String name;
    private final String content;

    public Metadata(String name, String content) {
        this.name = name;
        this.content = content;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(name, metadata.name) && Objects.equals(content, metadata.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content);
    }
}
