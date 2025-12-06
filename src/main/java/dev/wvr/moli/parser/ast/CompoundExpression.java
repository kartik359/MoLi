package dev.wvr.moli.parser.ast;

import dev.wvr.moli.runtime.Environment;
import java.util.List;

public record CompoundExpression(List<Expression> expressions) implements Expression {
    @Override
    public float eval(Environment env) {
        float result = 0.0f;
        for (Expression expression : expressions) {
            if (env.isReturning() || env.isBreaking() || env.isContinuing()) {
                return env.isReturning() ? env.getReturnValue() : result;
            }

            result = expression.eval(env);

            if (env.isReturning() || env.isBreaking() || env.isContinuing()) {
                return env.isReturning() ? env.getReturnValue() : result;
            }
        }
        return result;
    }
}