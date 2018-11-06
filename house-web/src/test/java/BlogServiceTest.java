import com.april.house.biz.service.BlogService;
import com.april.house.common.model.Blog;
import com.april.house.common.util.DateTimeUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class BlogServiceTest extends BaseServiceTest {
    @Autowired
    private BlogService blogService;

    @Test
    public void testInsert() {
        /*Blog blog = new Blog();
        blog.setTags("潮流, 笋房");
        blog.setTitle("便宜好房，房东急卖");
        blog.setContent("朝南的小3房目前有多套在售 这套的卖点在于其位置是不靠任何马路的 相对比较安静 并且西面的房间正对小区的绿化景观 楼层也比较适中 ");
        blog.setCreateTime(new Date());
        blog.setCat(1);

        int result = blogService.insert(blog);
        System.out.printf("result = %d, id = %d\n", result, blog.getId());*/

        /*Blog blog = new Blog();
        blog.setTags("冬暖夏凉,新房,交通方便");
        blog.setTitle("湖畔天下售楼");
        blog.setContent("户型优点 【采光&通风】1.每个房间都有窗，保证日间任意房间都有自然光。2.有飘窗，扩充了室内空间，增加改造灵活性。3.客厅和餐厅贯穿南北，室内视野更开阔，开窗时可在房屋中间形成穿堂风，冬暖夏凉。 ");
        blog.setCreateTime(new Date());
        blog.setCat(2);

        int result = blogService.insert(blog);
        System.out.printf("result = %d, id = %d\n", result, blog.getId());

        blog = new Blog();
        blog.setTags("交通方便,世博地区,冬暖夏凉");
        blog.setTitle("凯德二期,地暖+新风,低噪音区");
        blog.setContent("凯德嘉博名邸位于地铁7号线沿线，出入口紧近7号线杨高南路地铁口 总建筑面积超过5万平方米，9座16层楼座共540户 外墙内保温系统，隔热保温，冬暖夏凉");
        blog.setCreateTime(new Date());
        blog.setCat(1);

        result = blogService.insert(blog);
        System.out.printf("result = %d, id = %d\n", result, blog.getId());

        blog = new Blog();
        blog.setTags("笋盘,世博地区,精装修");
        blog.setTitle("置换急售 直降50万 带阁楼送露台");
        blog.setContent("小区2002年竣工，小区户型正气，绿化高\n" +
                "此房一梯2户，主卧朝南，厅朝南，次卧朝北，厨卫朝北，标准板式户型，通透全明，房子业主自住，装修保养干净清爽，要求不高，可以拎包入住");
        blog.setCreateTime(new Date());
        blog.setCat(2);

        result = blogService.insert(blog);
        System.out.printf("result = %d, id = %d\n", result, blog.getId());*/

        Blog blog = new Blog();
        blog.setTags("世博地区,精装修,采光好");
        blog.setTitle("07年商品房带电梯+地铁8号线杨思站");
        blog.setContent("户型：厅朝南带阳台，主卧朝南带飘窗，两个次卧朝北有窗，厨卫朝北全明，南北通透格局好，此房近小区大花园窗外望出很舒适");
        blog.setCreateTime(new Date());
        blog.setCat(1);

        int result = blogService.insert(blog);
        System.out.printf("result = %d, id = %d\n", result, blog.getId());
    }

    @Test
    public void testQueryPage() {
        Blog blog = new Blog();
        blog.setCat(1);
        blog.setTags("精装修");
        List<Blog> blogs = blogService.queryBlogs(blog);
        System.out.println("blogs.size() = " + blogs.size());

        blog = new Blog();
        blog.setTags("精装修");
        blog.setCreateTime(DateTimeUtil.strToDate("2018-11-06 00:00:00"));
        blogs = blogService.queryBlogs(blog);
        System.out.println("blogs.size() = " + blogs.size());
    }

}
