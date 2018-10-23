package cn.itcast.bos.action;

import cn.itcast.bos.conston.HttPUrl;
import cn.itcast.bos.utils.BaseAction;
import cn.itcast.bos.utils.MailUtils;
import cn.itcast.crm.domain.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {
    //注入jmsTemplate
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    //发送验证码
    @Action(value = "sendMessage")
    public String sendMsg() throws Exception {
        //使用工具类生成验证码
        String code = RandomStringUtils.randomNumeric(4);

        //将验证码存入session中用于验证输入验证码是否正确
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), code);

        System.out.println("验证码:" + code);
        //编辑短息内容
        final String msg = "【速运快递】尊敬的用户您好，您本次的验证码" + code +
                "按页面提示信息提交验证切勿将验证码泄露于其他人";
       /* //使用工具类发送短信
        String result = SmsUtils.sendSmsByHTTP(model.getTelephone(), msg);
        //判断短信是否发送成功
        if (result.startsWith("000")){
            return NONE;
        }else {
            throw new RuntimeException("短信发送失败"+result);
        }*/
        //使用ActiveMQ发送消息
   /*     jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //构建Map消息集合，可以存储多个消息
                MapMessage mapMessage = session.createMapMessage();
                //告知发送给的对象
                mapMessage.setString("telephone",model.getTelephone());
                //发送消息的内容
                mapMessage.setString("msg",msg);
                return mapMessage;
            }
        });*/
        return NONE;
    }


    //校验验证码，用户注册
    //接收验证码
    private String Code;

    public void setCode(String code) {
        Code = code;
    }

    //注入redis
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Action(value = "registFrom", results = {
            @Result(name = "success", type = "redirect", location = "signup-success.html"),
            @Result(name = "input", type = "redirect", location = "signup.html")})
    public String regist() {
        //获取session中的验证码
        String sessionCode = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
        System.out.println(sessionCode + "===");
        ServletActionContext.getServletContext().removeAttribute(model.getTelephone());
        if (sessionCode == null || !(sessionCode.equals(Code))) {
            //验证码校验失败
            return INPUT;
        } else {
            //验证码校验成功，保存用户
            WebClient.create("http://localhost:9091/crm_management/services/customerService/customer")
                    .type(MediaType.APPLICATION_JSON).post(model);
            //发送激活激活邮件
            //使用工具类生成激活码
            String activationCode = RandomStringUtils.randomNumeric(16);
            //设置激活码24小时过期
            redisTemplate.opsForValue().set(model.getTelephone(), activationCode, 24, TimeUnit.HOURS);
            //编辑邮件内容
            final String content = "<h2>【速运快递邮件】尊敬的用户请点击下面的链接激活您的账号!" +
                    "</h2><h3><a href='" + MailUtils.activeUrl + "?telephone=" + model.getTelephone()
                    + "&activationCode=" + activationCode + "'>" + MailUtils.activeUrl + "</a><h3>";
           /* System.out.println(model.getEmail());
            //发送邮件
            MailUtils.sendMail("激活邮件",content,model.getEmail());
            //sendMail("测试邮件", "你好，>>", "1013554313@qq.com");*/
            //使用ActiveMQ发送邮件
            jmsTemplate.send("bos_sms", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    //创建MapMessage
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("theme", "激活邮件");
                    mapMessage.setString("content", content);
                    mapMessage.setString("email", model.getEmail());
                    return mapMessage;
                }
            });
            return SUCCESS;
        }
    }

    //用户绑定邮箱
    //接收链接提交的激活码
    private String activationCode;

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @Action(value = "activacustomer")
    public String activation() throws IOException {
        //防止页面输出乱码
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
//        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
        //先获取redis中的激活码
        String redisCode = redisTemplate.opsForValue().get(model.getTelephone());
        //判断激活码是否过期
        if (redisCode == null || !redisCode.equals(activationCode)) {
            ServletActionContext.getResponse().getWriter().println("激活码过期，请重新登录官方网站获取激活码！");
        } else {
            //先判断用户是否激活过
            //先根据用户号码查询到用户
            Customer customer = WebClient.create("http://localhost:9091/crm_management/services/customerService/noactivecustomer/" + model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON).get(Customer.class);
            //在根据用户的状态判断用户是否激活
            if (customer.getType() == null || customer.getType() != 1) {
                //说明用户未激活
                WebClient.create("http://localhost:9091/crm_management/services/customerService/updatecustomertype/" + model.getTelephone())
                        .get();
                ServletActionContext.getResponse().getWriter().println("激活成功");
            } else {
                //说明用户已激活
                ServletActionContext.getResponse().getWriter().println("您已激活，请勿重复操作！");
            }
        }
        return NONE;
    }

    //用户注册
    @Action(value = "customer_login",
            results = {@Result(name = "success", location = "index.html#/myhome", type = "redirect"),
                    @Result(name = "login", location = "login.html", type = "redirect")})
    public String login() {
        Customer customer =
                WebClient.create(HttPUrl.CRM_MANAGEMENT_URL + "/services/customerService/customerLogin?telephone="
                        + model.getTelephone() + "&password=" + model.getPassword())
                        .accept(MediaType.APPLICATION_JSON).get(Customer.class);
        //判断用户是否存
        if (customer == null) {
            //登录失败
            this.addActionError("用户名或密码错误");
            return LOGIN;
        } else {
            //登录成功将用户存到session中
            ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
            return SUCCESS;
        }
    }
}
