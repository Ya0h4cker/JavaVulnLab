import javax.naming.InitialContext;

public class JNDIClient {
    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();
        context.lookup("rmi://127.0.0.1:1099/ref");
    }
}
