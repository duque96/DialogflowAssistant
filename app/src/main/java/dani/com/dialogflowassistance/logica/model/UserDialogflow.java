package dani.com.dialogflowassistance.logica.model;

public class UserDialogflow implements User {
    private Long userId;
    private String nickname;

    public UserDialogflow (){
        this.userId = new Long(0);
        this.nickname = "Asistente";
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public String getNickname(){
        return nickname;
    }
}
