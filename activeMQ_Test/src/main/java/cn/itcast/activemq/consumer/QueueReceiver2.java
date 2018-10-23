package cn.itcast.activemq.consumer;

import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/*@Service("QueueReceiver2")*/
@Service
public class QueueReceiver2 implements MessageListener {
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("QueueReceiver2:" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
