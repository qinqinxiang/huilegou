package cn.itcast.activemq.consumer;

import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/*@Service("TopicReceiver1")*/
@Service
public class TopicReceiver1 implements MessageListener {
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("TopicReceiver1" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
