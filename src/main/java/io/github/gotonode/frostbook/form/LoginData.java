package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginData {

    @NotEmpty
    private String handle;

    @NotEmpty
    private String password;

    @Override
    public String toString() {
        return "LoginData{" +
                "handle='" + handle + '\'' +
                ", password(length)='" + password.length() + '\'' +
                '}';
    }
}
