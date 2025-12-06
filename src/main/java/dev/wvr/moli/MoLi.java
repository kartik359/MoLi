package dev.wvr.moli;

import dev.wvr.moli.parser.MolangParser;
import dev.wvr.moli.parser.ast.Expression;
import dev.wvr.moli.runtime.Environment;

public final class MoLi {

    private MoLi() {}

    public static Expression parse(String expression) {
        return new MolangParser(expression).parse();
    }

    public static float eval(String expression, Environment env) {
        env.resetFlow();
        return parse(expression).eval(env);
    }

    public static float eval(String expression) {
        return eval(expression, Environment.empty());
    }
}