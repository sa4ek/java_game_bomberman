package backend;

/**
 * Created by narek on 21.11.14.
 */
public interface User {
    public String getLogin();

    public String getPassword();

    public String getEmail();

    public void setLogin(String login);

    public void setPassword(String password);

    public void setEmail(String email);

    public void setScore(Integer score);

    public Integer getScore();

    public Integer incrementMyScore();//by one


}