package ch.heig_vd.app.converter.parser;

/**
 * @author Besseau Léonard
 */
public interface JsonParserVisitor {
    /**
     * This function is called by a JsonParser for each value encountered
     * @param field the json field
     * @param value the associated value
     */
    void visit(String field, String value);
}
