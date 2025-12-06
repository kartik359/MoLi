package dev.wvr.moli.parser.ast;

import dev.wvr.moli.runtime.Environment;

@FunctionalInterface
public interface Expression {
    float eval(Environment env);

    Expression ZERO = env -> 0.0f;
    Expression ONE = env -> 1.0f;
}