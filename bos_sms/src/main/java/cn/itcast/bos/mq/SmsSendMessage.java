package cn.itcast.bos.mq;

import cn.itccast.bos.utli.SmsUtils;
import org.springframework.stereotype.Service;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service("SmsSendMessage")
public class SmsSendMessage implements MessageListener {
    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        //调用工具类发送短信
        try {
            SmsUtils.sendSmsByHTTP(mapMessage.getString("telephone"), mapMessage.getString("msg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
