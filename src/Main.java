import com.raiden.homework.pattern.prototype.User;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setName("张三");
        user.setAge(21);
        List<String> family = new ArrayList<String>();
        family.add("父亲");
        family.add("母亲");
        user.setFamily(family);
        User user1 = user.clone();

        System.out.println("比较对象:" + (user.getFamily() == user1.getFamily()));
    }
}
