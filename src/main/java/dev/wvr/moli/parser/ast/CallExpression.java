package dev.wvr.moli.parser.ast;

import dev.wvr.moli.runtime.Environment;
import dev.wvr.moli.runtime.MathLib;
import java.util.List;

public record CallExpression(String function, List<Expression> arguments) implements Expression {
    @Override
    public float eval(Environment env) {
        float[] args = new float[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            args[i] = arguments.get(i).eval(env);
        }

        if (function.startsWith("math.")) {
            String name = function.substring(5);
            return switch (name) {
                case "abs" -> MathLib.abs(args[0]);
                case "sin" -> MathLib.sin(args[0]);
                case "cos" -> MathLib.cos(args[0]);
                case "sqrt" -> MathLib.sqrt(args[0]);
                case "floor" -> MathLib.floor(args[0]);
                case "ceil" -> MathLib.ceil(args[0]);
                case "round" -> MathLib.round(args[0]);
                case "trunc" -> MathLib.trunc(args[0]);
                case "clamp" -> MathLib.clamp(args[0], args[1], args[2]);
                case "lerp" -> MathLib.lerp(args[0], args[1], args[2]);
                case "min" -> MathLib.min(args[0], args[1]);
                case "max" -> MathLib.max(args[0], args[1]);
                case "pow" -> MathLib.pow(args[0], args[1]);
                case "random" -> MathLib.random(args[0], args[1]);
                case "hermite_blend" -> MathLib.hermite_blend(args[0]);
                case "die_roll" -> MathLib.die_roll(args[0], args[1], args[2]);
                case "die_roll_integer" -> MathLib.die_roll_integer(args[0], args[1], args[2]);
                case "mod" -> MathLib.mod(args[0], args[1]);
                case "pi" -> (float) Math.PI;
                default -> 0.0f;
            };
        }

        return env.call(function, args);
    }
}