package com.dani.dialogflowassistant.logica.model;

public class UserDialogflow implements User {
    private Long userId;
    private String nickname;

    public UserDialogflow (){
        this.userId = 0L;
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
