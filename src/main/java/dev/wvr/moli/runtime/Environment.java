package dev.wvr.moli.runtime;

import java.util.HashMap;
import java.util.Map;

public interface Environment {
    float resolve(String name);

    void set(String name, float value);

    void setReturnValue(float value);
    float getReturnValue();
    boolean isReturning();

    void setBreak(boolean value);
    boolean isBreaking();

    void setContinue(boolean value);
    boolean isContinuing();

    void resetFlow();
    void clearLoopFlags();

    default float call(String function, float[] args) {
        return 0.0f;
    }

    static Environment map() {
        return new MapEnvironment();
    }

    static Environment empty() {
        return map();
    }

    class MapEnvironment implements Environment {
        private final Map<String, Float> variables = new HashMap<>();

        private boolean returning = false;
        private float returnValue = 0.0f;
        private boolean breaking = false;
        private boolean continuing = false;

        @Override
        public float resolve(String name) {
            if (name.equals("math.pi")) return (float) Math.PI;
            return variables.getOrDefault(name, 0.0f);
        }

        @Override
        public void set(String name, float value) {
            variables.put(name, value);
        }

        @Override
        public void setReturnValue(float value) {
            this.returnValue = value;
            this.returning = true;
        }

        @Override
        public float getReturnValue() {
            return returnValue;
        }

        @Override
        public boolean isReturning() {
            return returning;
        }

        @Override
        public void setBreak(boolean value) {
            this.breaking = value;
        }

        @Override
        public boolean isBreaking() {
            return breaking;
        }

        @Override
        public void setContinue(boolean value) {
            this.continuing = value;
        }

        @Override
        public boolean isContinuing() {
            return continuing;
        }

        @Override
        public void resetFlow() {
            this.returning = false;
            this.breaking = false;
            this.continuing = false;
            this.returnValue = 0.0f;
        }

        @Override
        public void clearLoopFlags() {
            this.breaking = false;
            this.continuing = false;
        }
    }
}