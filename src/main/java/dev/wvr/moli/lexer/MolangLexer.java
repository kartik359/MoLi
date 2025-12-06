package dev.wvr.moli.lexer;

import dev.wvr.moli.exception.MolangException;
import java.util.ArrayList;
import java.util.List;

public final class MolangLexer {
    private final String source;
    private final int length;
    private int pos = 0;
    private int line = 1;
    private int col = 1;

    public MolangLexer(String source) {
        this.source = source;
        this.length = source.length();
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < length) {
            char c = peek();

            if (Character.isWhitespace(c)) {
                advance();
                continue;
            }

            if (Character.isDigit(c) || (c == '.' && Character.isDigit(peek(1)))) {
                tokens.add(number());
            } else if (Character.isJavaIdentifierStart(c)) {
                tokens.add(identifier());
            } else if (c == '\'') {
                tokens.add(string());
            } else {
                tokens.add(operator());
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line, col));
        return tokens;
    }

    private Token number() {
        int start = pos;
        int startCol = col;
        boolean hasDot = false;

        while (pos < length) {
            char c = peek();
            if (c == '.') {
                if (hasDot) break;
                hasDot = true;
            } else if (!Character.isDigit(c)) {
                break;
            }
            advance();
        }
        return new Token(TokenType.NUMBER, source.substring(start, pos), line, startCol);
    }

    private Token identifier() {
        int start = pos;
        int startCol = col;

        while (pos < length && (Character.isJavaIdentifierPart(peek()) || peek() == '.')) {
            advance();
        }

        String value = source.substring(start, pos);
        TokenType type = switch (value.toLowerCase()) {
            case "return" -> TokenType.RETURN;
            case "break" -> TokenType.BREAK;
            case "continue" -> TokenType.CONTINUE;
            default -> TokenType.IDENTIFIER;
        };

        return new Token(type, value, line, startCol);
    }

    private Token string() {
        int startCol = col;
        advance();
        int start = pos;

        while (pos < length && peek() != '\'') {
            advance();
        }

        if (pos >= length) throw new MolangException("Unterminated string at line " + line);

        String value = source.substring(start, pos);
        advance();

        return new Token(TokenType.STRING, value, line, startCol);
    }

    private Token operator() {
        int startCol = col;
        char c = advance();

        TokenType type = switch (c) {
            case '+' -> TokenType.PLUS;
            case '-' -> match('>') ? TokenType.ARROW : TokenType.MINUS;
            case '*' -> TokenType.STAR;
            case '/' -> TokenType.SLASH;
            case '%' -> TokenType.PERCENT;
            case '(' -> TokenType.LPAREN;
            case ')' -> TokenType.RPAREN;
            case '{' -> TokenType.LBRACE;
            case '}' -> TokenType.RBRACE;
            case '[' -> TokenType.LBRACKET;
            case ']' -> TokenType.RBRACKET;
            case ',' -> TokenType.COMMA;
            case ';' -> TokenType.SEMICOLON;
            case '?' -> match('?') ? TokenType.NULL_COALESCE : TokenType.QUESTION;
            case ':' -> TokenType.COLON;
            case '!' -> match('=') ? TokenType.NEQ : TokenType.NOT;
            case '=' -> match('=') ? TokenType.EQ : TokenType.ASSIGN;
            case '<' -> match('=') ? TokenType.LTE : TokenType.LT;
            case '>' -> match('=') ? TokenType.GTE : TokenType.GT;
            case '&' -> match('&') ? TokenType.AND : TokenType.AND;
            case '|' -> match('|') ? TokenType.OR : TokenType.OR;
            default -> throw new MolangException("Unexpected character '" + c + "' at line " + line);
        };

        return new Token(type, String.valueOf(c), line, startCol);
    }

    private char peek() {
        return peek(0);
    }

    private char peek(int offset) {
        if (pos + offset >= length) return '\0';
        return source.charAt(pos + offset);
    }

    private char advance() {
        char c = source.charAt(pos++);
        if (c == '\n') {
            line++;
            col = 1;
        } else {
            col++;
        }
        return c;
    }

    private boolean match(char expected) {
        if (pos >= length || source.charAt(pos) != expected) return false;
        advance();
        return true;
    }
}