package dev.wvr.moli.parser.ast;

import dev.wvr.moli.lexer.TokenType;
import dev.wvr.moli.runtime.Environment;

public record StatementExpression(TokenType type) implements Expression {
    @Override
    public float eval(Environment env) {
        if (type == TokenType.BREAK) {
            env.setBreak(true);
        } else if (type == TokenType.CONTINUE) {
            env.setContinue(true);
        }
        return 0.0f;
    }
}