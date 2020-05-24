import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class CrowdfundingTest {
    @Test
    public void test(){
        XmlWebApplicationContext xmlWebApplicationContext = new XmlWebApplicationContext();
        System.out.println(xmlWebApplicationContext);
    }
}
