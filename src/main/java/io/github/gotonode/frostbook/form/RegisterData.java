package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterData {

    @NotEmpty(message = "Please enter your handle.")
    @Size(min = 3, max = 16, message = "Your handle must be between 3 and 16 characters.")
    private String handle;

    @NotEmpty(message = "Please enter your password.")
    @Size(min = 8, max = 128, message = "Your password must be between 8 and 128 characters.")
    private String password;

    @NotEmpty(message = "Please enter your path.")
    @Size(min = 3, max = 16, message = "Your path must be between 3 and 16 characters.")
    private String path;

    @NotEmpty(message = "Please enter your name.")
    @Size(min = 3, max = 64, message = "Your path must be between 3 and 64 characters.")
    private String name;

    @Override
    public String toString() {
        return "RegisterData{" +
                "handle='" + handle + '\'' +
                ", password='" + String.valueOf("[PROTECTED]") + '\'' +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
