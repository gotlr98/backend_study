//import com.example.demo.User;
//import org.springframework.stereotype.Service;
//import java.util.*;
//
//@Service // 이 어노테이션이 있어야 스프링이 관리해줍니다.
//public class UserService {
//    private List<User> users = new ArrayList<>(Arrays.asList(
//            new User(1L, "알고리즘천재"),
//            new User(2L, "자바마스터")
//    ));
//
//    public List<User> getAllUsers() {
//        return users;
//    }
//
//    public void addUser(String name) {
//        long newId = users.size() + 1;
//        users.add(new User(newId, name));
//    }
//}