package backend.test_memory_base;

import backend.AccountService;
import backend.User;
import backend.enums.AccountEnum;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by mid on 30.09.14.
 * //в этом классе происходит обработка всех действий над пользователями - логин, регистрация, итд
 *
 */

//TODO предусмотреть случаи когда
    // от одного компа логинится сверху кто-то
public class AccoutServiveImpMemory implements AccountService {

    private Map<String, User> arraySessionId = new HashMap<>();//все сессии пользователей - sessionId/User
    private Map<String, User> users = new HashMap<>();//все зарегистрированые юзеры - логин/User


    @Override
    public Map<String, User> getArraySessionId() {return  this.arraySessionId;};

    @Override
    public Map<String, User> getUsers() {return this.users;};

    @Override
    public AccountEnum checkRegistration(String userName) {//проверка регистрации пользователя

        if (users.containsKey(userName)) {
            return AccountEnum.UserRegistered;
        } else  {
            return AccountEnum.UserNotRegistered;
        }
    }
    @Override
    public AccountEnum checkLogIn (HttpServletRequest request) {//проверка залогинен ли пользватель
        if (arraySessionId.containsKey(request.getSession().getId())) {//есть ли у данного sid юзер
            return AccountEnum.UserLoggedIn;//TODO все не может быть так просто
        } else {
            return AccountEnum.UserNotLoggedIn;
        }
    }
    @Override
    public AccountEnum logIn(String login, String password,HttpServletRequest request) {//залогинивагние пользователя
        if (checkLogIn(request)==AccountEnum.UserNotLoggedIn) {
            if (this.checkRegistration(login)==AccountEnum.UserRegistered && !arraySessionId.containsValue(login)) {// TODO эта проверка не совсем корректна
                if (users.get(login).getPassword().equals(password)) {
                    arraySessionId.put(request.getSession().getId(), users.get(login));
                    return AccountEnum.LogInSuccess;
                } else {
                    return AccountEnum.LogInFail;
                }
            } else {
                return AccountEnum.LogInFail;
            }
        } else {
            return AccountEnum.UserLoggedIn;//TODO обдумать что делать, если юзер уже залогинен//изменение в свзя с enum
        }

    }
    @Override
    public AccountEnum logOff (HttpServletRequest request) {//разлогинивание пользователя
        User cur;
        if (arraySessionId.containsKey(request.getSession().getId())) {
            cur = arraySessionId.get(request.getSession().getId());
            if (this.checkRegistration(cur.getLogin()) && this.checkLogIn(request)) {
                arraySessionId.remove(request.getSession().getId());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    @Override
    public boolean register(User user) {//регистрация пользователя
        if (users.containsKey(user.getLogin())) {
            return false;
        } else {
            users.put(user.getLogin(), user);
            return true;
        }
    }
    @Override
    public boolean editProfile(User user) {
        return true;
    }
}