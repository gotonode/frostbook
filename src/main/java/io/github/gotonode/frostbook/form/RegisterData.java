package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterData {

    @NotEmpty
    @Size(min = 3, max = 10)
    private String handle;

    @NotEmpty
    //@Size(min = 64, max = 64)
    private String password;

    @NotEmpty
    //@Size(min = 64, max = 64)
    private String passwordVerification;

    @NotEmpty
    @Size(min = 3, max = 10)
    private String path;

    @NotEmpty
    @Size(min = 3, max = 10)
    private String name;

    @Override
    public String toString() {
        return "RegisterData{" +
                "handle='" + handle + '\'' +
                ", password='" + password + '\'' +
                ", passwordVerification='" + passwordVerification + '\'' +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
