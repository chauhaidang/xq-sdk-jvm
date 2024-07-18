import com.xq.MessageModel;
import com.xq.PrintService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PrintServiceTest {
    @Tag("slow")
    @Test
    void shouldPrintMessageSlow() {
        PrintService service = new PrintService();
        MessageModel model = new MessageModel("Hello, world, slow!");
        service.print(model);
    }

    @Test
    void shouldPrintMessageFast() {
        PrintService service = new PrintService();
        MessageModel model = new MessageModel("Hello, world, fast!");
        service.print(model);
    }
}
