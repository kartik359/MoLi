package dev.wvr.moli.parser.ast;

import dev.wvr.moli.runtime.Environment;

public record LoopExpression(Expression countExpr, Expression body) implements Expression {
    @Override
    public float eval(Environment env) {
        int count = (int) countExpr.eval(env);
        float lastResult = 0.0f;

        for (int i = 0; i < count; i++) {
            if (env.isReturning()) break;

            lastResult = body.eval(env);

            if (env.isBreaking()) {
                env.clearLoopFlags();
                break;
            }

            if (env.isContinuing()) {
                env.clearLoopFlags();
                continue;
            }
        }

        return lastResult;
    }
}