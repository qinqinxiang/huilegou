import cn.itcast.bos.service.take_deliver.WayBillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Show {
    @Autowired
    private WayBillService wayBillService;

    @Test
    public void show() {
        wayBillService.sysIndex();
    }
}
