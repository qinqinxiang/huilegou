package cn.itcast.activemq.consumer;

import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/*@Service("QueueReceiver1")*/
@Service
public class QueueReceiver1 implements MessageListener {
    public void onMessage(Message message) {
        //文本消息
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("QueueReceiver1:" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
