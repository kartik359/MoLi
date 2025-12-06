package dev.wvr.moli.parser.ast;

import dev.wvr.moli.lexer.TokenType;
import dev.wvr.moli.runtime.Environment;

public record UnaryExpression(TokenType operator, Expression right) implements Expression {
    @Override
    public float eval(Environment env) {
        float val = right.eval(env);
        return switch (operator) {
            case MINUS -> -val;
            case NOT -> val == 0 ? 1.0f : 0.0f;
            case RETURN -> {
                env.setReturnValue(val);
                yield val;
            }
            default -> val;
        };
    }
}