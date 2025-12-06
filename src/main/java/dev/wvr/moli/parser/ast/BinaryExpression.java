package dev.wvr.moli.parser.ast;

import dev.wvr.moli.lexer.TokenType;
import dev.wvr.moli.runtime.Environment;

public record BinaryExpression(Expression left, TokenType operator, Expression right) implements Expression {
    @Override
    public float eval(Environment env) {
        float l = left.eval(env);
        float r = right.eval(env);

        return switch (operator) {
            case PLUS -> l + r;
            case MINUS -> l - r;
            case STAR -> l * r;
            case SLASH -> r == 0 ? 0 : l / r;
            case PERCENT -> l % r;
            case EQ -> Math.abs(l - r) < 1e-5 ? 1.0f : 0.0f;
            case NEQ -> Math.abs(l - r) >= 1e-5 ? 1.0f : 0.0f;
            case LT -> l < r ? 1.0f : 0.0f;
            case LTE -> l <= r ? 1.0f : 0.0f;
            case GT -> l > r ? 1.0f : 0.0f;
            case GTE -> l >= r ? 1.0f : 0.0f;
            case AND -> (l != 0 && r != 0) ? 1.0f : 0.0f;
            case OR -> (l != 0 || r != 0) ? 1.0f : 0.0f;
            case NULL_COALESCE -> l != 0 ? l : r;
            default -> 0.0f;
        };
    }
}