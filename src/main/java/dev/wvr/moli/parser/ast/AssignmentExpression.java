package dev.wvr.moli.parser.ast;

import dev.wvr.moli.runtime.Environment;

public record AssignmentExpression(String name, Expression value) implements Expression {
    @Override
    public float eval(Environment env) {
        float val = value.eval(env);
        env.set(name, val);
        return val;
    }
}