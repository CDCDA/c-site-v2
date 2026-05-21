import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Single {

     public static void main(String[] args) {
         getInstance();
    }


    private volatile static Single single=null;
    public static Single getInstance() {
        if (single == null) {
            synchronized(test.A.class) {
                if (single == null) {
                    single= new Single();
                }
            }
        }
        return single;
    }
}