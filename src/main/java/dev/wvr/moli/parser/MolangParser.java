package dev.wvr.moli.parser;

import dev.wvr.moli.exception.MolangException;
import dev.wvr.moli.lexer.MolangLexer;
import dev.wvr.moli.lexer.Token;
import dev.wvr.moli.lexer.TokenType;
import dev.wvr.moli.parser.ast.*;

import java.util.ArrayList;
import java.util.List;

public final class MolangParser {
    private final List<Token> tokens;
    private int current = 0;

    public MolangParser(String source) {
        this.tokens = new MolangLexer(source).tokenize();
    }

    public Expression parse() {
        if (tokens.isEmpty() || peek().type() == TokenType.EOF) return Expression.ZERO;

        List<Expression> expressions = new ArrayList<>();
        while (!isAtEnd()) {
            if (peek().type() == TokenType.SEMICOLON) {
                advance();
                continue;
            }
            expressions.add(assignment());
            match(TokenType.SEMICOLON);
        }

        if (expressions.isEmpty()) return Expression.ZERO;
        if (expressions.size() == 1) return expressions.get(0);

        return new CompoundExpression(expressions);
    }

    private Expression assignment() {
        Expression expr = ternary();
        if (match(TokenType.ASSIGN)) {
            if (expr instanceof AccessExpression access) {
                Expression value = assignment();
                return new AssignmentExpression(access.name(), value);
            }
            throw new MolangException("Invalid assignment target");
        }
        return expr;
    }

    private Expression ternary() {
        Expression expr = logicOr();
        if (match(TokenType.QUESTION)) {
            Expression trueBranch = assignment();
            consume(TokenType.COLON, "Expected ':' after '?' in ternary operator");
            Expression falseBranch = assignment();
            return new TernaryExpression(expr, trueBranch, falseBranch);
        }
        return expr;
    }

    private Expression logicOr() {
        Expression expr = logicAnd();
        while (match(TokenType.OR)) {
            Expression right = logicAnd();
            expr = new BinaryExpression(expr, TokenType.OR, right);
        }
        return expr;
    }

    private Expression logicAnd() {
        Expression expr = equality();
        while (match(TokenType.AND)) {
            Expression right = equality();
            expr = new BinaryExpression(expr, TokenType.AND, right);
        }
        return expr;
    }

    private Expression equality() {
        Expression expr = comparison();
        while (match(TokenType.EQ, TokenType.NEQ)) {
            TokenType op = previous().type();
            Expression right = comparison();
            expr = new BinaryExpression(expr, op, right);
        }
        return expr;
    }

    private Expression comparison() {
        Expression expr = term();
        while (match(TokenType.GT, TokenType.GTE, TokenType.LT, TokenType.LTE)) {
            TokenType op = previous().type();
            Expression right = term();
            expr = new BinaryExpression(expr, op, right);
        }
        return expr;
    }

    private Expression term() {
        Expression expr = factor();
        while (match(TokenType.PLUS, TokenType.MINUS, TokenType.NULL_COALESCE)) {
            TokenType op = previous().type();
            Expression right = factor();
            expr = new BinaryExpression(expr, op, right);
        }
        return expr;
    }

    private Expression factor() {
        Expression expr = unary();
        while (match(TokenType.STAR, TokenType.SLASH, TokenType.PERCENT)) {
            TokenType op = previous().type();
            Expression right = unary();
            expr = new BinaryExpression(expr, op, right);
        }
        return expr;
    }

    private Expression unary() {
        if (match(TokenType.NOT, TokenType.MINUS, TokenType.RETURN)) {
            TokenType op = previous().type();
            Expression right = unary();
            return new UnaryExpression(op, right);
        }
        return primary();
    }

    private Expression primary() {
        if (match(TokenType.NUMBER)) {
            return new ConstantExpression(previous().asFloat());
        }

        if (match(TokenType.IDENTIFIER)) {
            String name = previous().value();

            if (name.equals("loop") && match(TokenType.LPAREN)) {
                Expression count = expression();
                consume(TokenType.COMMA, "Expected ',' after loop count");
                Expression body = expression();
                consume(TokenType.RPAREN, "Expected ')' after loop body");
                return new LoopExpression(count, body);
            }

            if (match(TokenType.LPAREN)) {
                List<Expression> args = new ArrayList<>();
                if (!check(TokenType.RPAREN)) {
                    do {
                        args.add(assignment());
                    } while (match(TokenType.COMMA));
                }
                consume(TokenType.RPAREN, "Expected ')' after arguments");
                return new CallExpression(name, args);
            }
            return new AccessExpression(name);
        }

        if (match(TokenType.BREAK)) return new StatementExpression(TokenType.BREAK);
        if (match(TokenType.CONTINUE)) return new StatementExpression(TokenType.CONTINUE);

        if (match(TokenType.LPAREN)) {
            Expression expr = assignment();
            consume(TokenType.RPAREN, "Expected ')' after expression");
            return expr;
        }

        if (match(TokenType.LBRACE)) {
            return scope();
        }

        throw new MolangException("Unexpected token: " + peek().type());
    }

    private Expression scope() {
        List<Expression> expressions = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            if (peek().type() == TokenType.SEMICOLON) {
                advance();
                continue;
            }
            expressions.add(assignment());
            match(TokenType.SEMICOLON);
        }
        consume(TokenType.RBRACE, "Expected '}' after scope");

        if (expressions.isEmpty()) return Expression.ZERO;
        if (expressions.size() == 1) return expressions.get(0);

        return new CompoundExpression(expressions);
    }

    private Expression expression() {
        return assignment();
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private void consume(TokenType type, String message) {
        if (check(type)) {
            advance();
            return;
        }
        throw new MolangException(message);
    }
}