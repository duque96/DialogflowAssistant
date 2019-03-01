package dani.com.dialogflowassistance.logica.model;

public class UserSender implements User{
    private Long userId;

    public UserSender() {
        this.userId = new Long(1);
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getNickname() {
        return null;
    }
}
