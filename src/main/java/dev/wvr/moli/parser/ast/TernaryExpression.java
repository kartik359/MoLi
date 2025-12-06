package dev.wvr.moli.parser.ast;

import dev.wvr.moli.runtime.Environment;

public record TernaryExpression(Expression condition, Expression ifTrue, Expression ifFalse) implements Expression {
    @Override
    public float eval(Environment env) {
        return condition.eval(env) != 0.0f ? ifTrue.eval(env) : ifFalse.eval(env);
    }
}