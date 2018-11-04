import com.april.house.HouseApplication;
import com.april.house.biz.service.AgencyService;
import com.april.house.common.model.Agency;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HouseApplication.class)
public class AgencyServiceTest {
    @Autowired
    private AgencyService agencyService;

    @Test
    public void testGetAllAgency() {
        Agency agency = new Agency();
        agency.setEmail("010@gmail.com");
        List<Agency> agencies = agencyService.getAllAgency(agency);
        System.out.println(agencies.get(0));

    }

    @Test
    public void testgetPageAgency() {
        Agency agency = new Agency();
        PageInfo<Agency> pageInfo = agencyService.getPageAgency(agency);
        System.out.println(pageInfo.getTotal());
    }
}
