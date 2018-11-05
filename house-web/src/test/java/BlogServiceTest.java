import com.april.house.biz.service.BlogService;
import com.april.house.common.model.Blog;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class BlogServiceTest extends BaseServiceTest {
    @Autowired
    private BlogService blogService;

    @Test
    public void testInsert() {
        Blog blog = new Blog();
        blog.setTags("潮流, 笋房");
        blog.setTitle("便宜好房，房东急卖");
        blog.setContent("朝南的小3房目前有多套在售 这套的卖点在于其位置是不靠任何马路的 相对比较安静 并且西面的房间正对小区的绿化景观 楼层也比较适中 ");
        blog.setCreateTime(new Date());
        blog.setCat(1);

        int result = blogService.insert(blog);
        System.out.printf("result = %d, id = %d\n", result, blog.getId());
    }

}
