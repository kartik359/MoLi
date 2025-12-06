package dev.wvr.moli.parser.ast;

import dev.wvr.moli.runtime.Environment;

public record AccessExpression(String name) implements Expression {
    @Override
    public float eval(Environment env) {
        return env.resolve(name);
    }
}