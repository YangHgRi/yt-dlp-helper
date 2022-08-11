import java.util.Map;

public class TestSystemVariable {
    public static void main(String[] args) {
        systemVariable();
    }

    static void systemVariable() {
        Map<String, String> envMap = System.getenv();
        envMap.forEach((k, v) -> System.out.println(k + " = " + v));
    }
}