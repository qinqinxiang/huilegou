package cn.itcast.bos.service.impl.take_deliver;

import cn.itcast.bos.conston.HttPUrl;
import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.take_deliver.OrderRepository;
import cn.itcast.bos.dao.take_deliver.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_deliver.OrderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    //注入jmsTemple
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    //注入workBillDao
    @Autowired
    private WorkBillRepository workBillRepository;

    //注入dao
    @Autowired
    private OrderRepository orderRepository;

    //注入定区dao
    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    //注入区域dao
    @Autowired
    private AreaRepository areaRepository;

    //添加订单
    @Override
    public void addOrder(Order order) {
        //设置订单的状态
        order.setStatus("1");
        //设置订单的生成时间
        order.setOrderTime(new Date());

        //根据发件人省市区信息查找区域的信息
        Area sendArea = order.getSendArea();
        //查询到区域
        Area sendAreas = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(),
                sendArea.getDistrict());

        Area recArea = order.getRecArea();
        Area recAreas = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(),
                recArea.getDistrict());
        //重新设置发件人和收件人地址信息
        order.setSendArea(sendAreas);
        order.setRecArea(recAreas);


        //调用webservice服务查询定区id
        String fiexdAreaId = WebClient.create(HttPUrl.CRM_MANAGEMENT_URL + "/services/customerService/findFixedAreaId?address=" + order.getSendAddress())
                .accept(MediaType.APPLICATION_JSON).get(String.class);
        //根据定区id查询定区
        if (fiexdAreaId != null) {
            //调用dao查询地址
            FixedArea fiexdArea = fixedAreaRepository.findOne(fiexdAreaId);
            //从定区中查找快递员
            //   Courier courier = fiexdArea.getCouriers().iterator().next();
            Iterator<Courier> iterator = fiexdArea.getCouriers().iterator();
            //判断 这个定区是否有关联的快递员
            if (iterator.hasNext()) {
                Courier courier = iterator.next();
                if (courier != null) {
                    //将快递员关联到订单
                    saveOrder(order, courier);
                    //发送保存工单，发送短息
                    sendMessage(order);
                    return;
                }
            }

        }

        //遍历分区
        for (SubArea subArea : sendAreas.getSubareas()) {
            //根据关键字查询
            if (order.getSendAddress().contains(subArea.getKeyWords())) {
                //根据分区找到定区找到快递员
                //   Courier courier = subArea.getFixedArea().getCouriers().iterator().next();
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        //将快递员关联到订单
                        saveOrder(order, courier);
                        //发送保存工单，发送短息
                        sendMessage(order);
                        return;
                    }
                }

            }
        }
        /*
         * SubArea 分区包含多个定区
         * Area 区域包含多个分区
         * FixedArea 定区关联快递员
         * */
        //遍历分区
        for (SubArea subArea : sendAreas.getSubareas()) {
            //辅助关键字查询
            if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
                //根据分区查找定区找到关联快递员
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        //将快递员关联到订单
                        saveOrder(order, courier);
                        //发送保存工单，发送短息
                        sendMessage(order);
                        return;
                    }
                }
            }
        }
     /*   //人工分单
        order.setOrderType("2");
        //保存订单
        orderRepository.save(order);*/
    }


    //判断迭代器中是否包含元素
 /*   private boolean hasCourier(Order order, Iterator<Courier> iterator) {
        if (iterator.hasNext()){
            Courier courier = iterator.next();
            if (courier!=null){
                //将快递员关联到订单
                saveOrder(order, courier);
                //发送保存工单，发送短息
                sendMessage(order);
                return true;
            }
        }
        return false;
    }*/

    //自动分单保存订单的方法
    private void saveOrder(Order order, Courier courier) {
        //自动分单
        order.setOrderType("1");
        //关联快递员
        order.setCourier(courier);
        //生成订单编号
        order.setOrderNum(UUID.randomUUID().toString());
        //保存订单
        orderRepository.save(order);
    }

    //发送短信
    private void sendMessage(Order order) {
        //生成工单
        WorkBill workBill = new WorkBill();
        //设置工单状态
        workBill.setType("新");
        //设置取件状态
        workBill.setPickstate("待取件");
        //设置工单生成时间
        workBill.setBuildtime(new Date());
        //设置短信序号
        workBill.setSmsNumber(RandomStringUtils.randomNumeric(4));
        //设置快递员
        workBill.setCourier(order.getCourier());
        //设置 订单
        workBill.setOrder(order);
        //保存工单
        workBillRepository.save(workBill);
        //发送短信通知快递员
/*        jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",workBill.getCourier().getTelephone());
                mapMessage.setString("msg","收到短信后请火速前往:短信序号:"+workBill.getSmsNumber()+"取件地址:"
                        +order.getSendAddress()+"联系人:"+order.getSendName()+
                "联系方式:"+order.getSendMobile());
                return mapMessage;
            }
        });

        //发送短息通知发件人
        jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",order.getSendMobile());
                mapMessage.setString("msg","您的订单已生成:订单号:"+order.getOrderNum()+"请登录后查看订单详情");
                return mapMessage;
            }
        });*/
        //更改工单状态
        workBill.setPickstate("已通知");
    }

    //订单数据表格回显
    @Override
    public Order findByOrderNum(String orderNum) {
        return orderRepository.findByOrderNum(orderNum);
    }
}
