package ch.heig_vd.app.converter.utils;

import java.util.Objects;

/**
 * Represents an html metadata with name and value
 *
 * @author Maziero Marco
 * @author Besseau Léonard
 */
public class Metadata {
    private final String name;
    private final String content;

    /**
     * Constructs the metadata
     * @param name The given name
     * @param content The stored content
     */
    public Metadata(String name, String content) {
        this.name = name;
        this.content = content;
    }

    /**
     * Retrieves the metadata name
     * @return The string name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the metadata content
     * @return The string content
     */
    public String getContent() {
        return content;
    }

    /**
     * Displays the metadata content
     * @return The string output
     */
    @Override
    public String toString() {
        return "Metadata{" +
                "name='" + name + '\'' +
                ", contenu='" + content + '\'' +
                '}';
    }

    /**
     * Compares two metadata objects by their name and content
     * @param o The metadata to compare
     * @return Boolean true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(name, metadata.name) && Objects.equals(content, metadata.content);
    }

    /**
     * Redefines the hashcode for the object
     * @return The calculated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, content);
    }
}
