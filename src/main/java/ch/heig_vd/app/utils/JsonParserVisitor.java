package ch.heig_vd.app.utils;

public interface JsonParserVisitor {
    public void visit(String field, String value);
}
