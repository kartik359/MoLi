package dev.wvr.moli.parser.ast;

import dev.wvr.moli.runtime.Environment;

public record ConstantExpression(float value) implements Expression {
    @Override
    public float eval(Environment env) {
        return value;
    }
}