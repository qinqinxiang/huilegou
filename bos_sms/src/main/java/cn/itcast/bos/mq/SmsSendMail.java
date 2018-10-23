package cn.itcast.bos.mq;

import cn.itccast.bos.utli.MailUtils;
import org.springframework.stereotype.Service;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service("SmsSendMail")
public class SmsSendMail implements MessageListener {
    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        try {
            MailUtils.sendMail(mapMessage.getString("theme"),
                    mapMessage.getString("content"), mapMessage.getString("email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
