package dani.com.dialogflowassistance.logica.model;

public class UserSender implements User {
    private Long userId;

    public UserSender() {
        this.userId = 1L;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getNickname() {
        return null;
    }
}
