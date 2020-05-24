import com.shl.crowdfunding.bean.User;
import com.shl.crowdfunding.manager.mapper.UserMapper;
import com.shl.crowdfunding.util.MD5Util;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InsertTest {
    @Test
    public void testInsert(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) ac.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setEmail(i+"choudidi@163.com");
            user.setCreatetime("2020-3-12");
            user.setLoginacct("test"+i);
            user.setUsername("user");
            user.setUserpswd(MD5Util.digest("123"));
            userMapper.insert(user);
        }
    }
}
