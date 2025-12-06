# `MoLi`

![Java](https://img.shields.io/badge/Java-17%2B-ed8b00?style=for-the-badge&logo=java&logoColor=white)
![License](https://img.shields.io/github/license/LotusSeries/moli?style=for-the-badge)
![Build](https://img.shields.io/github/actions/workflow/status/LotusSeries/moli/build.yml?style=for-the-badge)

**MoLi** (**Mo**lang **Li**brary) is a modern, lightweight, and high-performance **Molang** interpreter written in java, designed for speed, flexibility, and ease of use in your projects!

## Example:
```java
import dev.wvr.moli.MoLi;
import dev.wvr.moli.runtime.Environment;

public class Example {
    static void main(String[] args) {
        // simple eval
        float result = MoLi.eval("math.cos(0) * 10 + 5"); 
        System.out.println(result); // 15.0

        // with variables
        Environment env = Environment.map();
        env.set("v.health", 20.0f);
        
        float logic = MoLi.eval("v.health < 10 ? 1 : 0", env);
        System.out.println(logic); // 0.0
    }
}
```
