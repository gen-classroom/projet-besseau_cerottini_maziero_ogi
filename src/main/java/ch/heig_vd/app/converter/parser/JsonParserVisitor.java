package ch.heig_vd.app.converter.parser;

public interface JsonParserVisitor {
    /**
     * This function is called by a JsonParser for each value encountered
     * @param field the json field
     * @param value the associated value
     */
    public void visit(String field, String value);
}
