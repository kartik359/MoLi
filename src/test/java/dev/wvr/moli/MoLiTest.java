package dev.wvr.moli;

import dev.wvr.moli.runtime.Environment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoLiTest {
    @Test
    void testBasicArithmetic() {
        assertEquals(2.0f, MoLi.eval("1 + 1"));
        assertEquals(7.0f, MoLi.eval("1 + 2 * 3"));
        assertEquals(9.0f, MoLi.eval("(1 + 2) * 3"));
        assertEquals(0.5f, MoLi.eval("1 / 2"));
    }

    @Test
    void testMathFunctions() {
        assertEquals(1.0f, MoLi.eval("math.abs(-1)"));
        assertEquals(5.0f, MoLi.eval("math.max(2, 5)"));
        assertEquals(3.0f, MoLi.eval("math.ceil(2.1)"));
        assertEquals(1.0f, MoLi.eval("math.clamp(10, 0, 1)"));
        assertEquals(1.0f, MoLi.eval("math.sin(90)"), 0.0001f);
    }

    @Test
    void testLogic() {
        assertEquals(1.0f, MoLi.eval("1 == 1"));
        assertEquals(0.0f, MoLi.eval("1 == 2"));
        assertEquals(1.0f, MoLi.eval("1 < 2"));
        assertEquals(1.0f, MoLi.eval("1 && 1"));
        assertEquals(0.0f, MoLi.eval("1 && 0"));
        assertEquals(1.0f, MoLi.eval("0 || 1"));
        assertEquals(0.0f, MoLi.eval("!1"));
    }

    @Test
    void testTernary() {
        assertEquals(10.0f, MoLi.eval("1 ? 10 : 20"));
        assertEquals(20.0f, MoLi.eval("0 ? 10 : 20"));
        assertEquals(30.0f, MoLi.eval("1 ? (0 ? 20 : 30) : 40"));
    }

    @Test
    void testVariables() {
        Environment env = Environment.map();
        env.set("v.x", 10.0f);

        assertEquals(10.0f, MoLi.eval("v.x", env));
        assertEquals(20.0f, MoLi.eval("v.x + 10", env));

        MoLi.eval("v.y = 50", env);
        assertEquals(50.0f, env.resolve("v.y"));

        assertEquals(5.0f, MoLi.eval("v.z = 5", env));
    }

    @Test
    void testComplexExpression() {
        String script = "v.x = 10; v.y = 20; (v.x * v.y) > 100 ? math.sqrt(100) : 0";
        Environment env = Environment.map();
        assertEquals(10.0f, MoLi.eval(script, env));
    }

    @Test
    void testReturnEarly() {
        String script = "v.x = 0; return 5; v.x = 10; return 20";
        Environment env = Environment.map();

        float result = MoLi.eval(script, env);

        assertEquals(5.0f, result);
        assertEquals(0.0f, env.resolve("v.x"));
    }

    @Test
    void testLoopWithBreak() {
        String script =
                "v.sum = 0; " +
                        "loop(10, { " +
                        "  v.sum = v.sum + 5; " +
                        "  v.sum > 10 ? break : 0 " +
                        "}); " +
                        "return v.sum";

        Environment env = Environment.map();
        float result = MoLi.eval(script, env);

        assertEquals(15.0f, result);
    }

    @Test
    void testLoopWithContinue() {
        String script =
                "v.sum = 0; v.i = 0; " +
                        "loop(4, { " +
                        "  v.i = v.i + 1; " +
                        "  math.mod(v.i, 2) != 0 ? continue : 0; " +
                        "  v.sum = v.sum + 1; " +
                        "}); " +
                        "return v.sum";

        Environment env = Environment.map();
        float result = MoLi.eval(script, env);
        assertEquals(2.0f, result);
    }
}