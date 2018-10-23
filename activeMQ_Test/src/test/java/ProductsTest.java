import cn.itcast.activemq.products.QueueSendMessage;
import cn.itcast.activemq.products.TopicSendMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-mq.xml")
public class ProductsTest {
    //注入
    @Autowired
    private QueueSendMessage queueSendMessage;
    @Autowired
    private TopicSendMessage topicSendMessage;

    @Test
    public void method() {
        queueSendMessage.send("queuetext", "点点");
        topicSendMessage.send("topictext", "积极");
    }
}
