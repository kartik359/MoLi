package dev.wvr.moli.lexer;

public record Token(TokenType type, String value, int line, int column) {
    public float asFloat() {
        return Float.parseFloat(value);
    }
}