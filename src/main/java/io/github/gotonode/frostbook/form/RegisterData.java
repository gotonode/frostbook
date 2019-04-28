package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RegisterData {

    @Pattern(regexp = "[\\w\\d]{4,16}", message = "Your handle must be 4 to 16 alphanumeric characters (no spaces).")
    private String handle;

    @Pattern(regexp = ".{8,128}", message = "Your password must be 8 to 128 characters.")
    private String password;

    @Pattern(regexp = "[\\w\\d]{4,16}", message = "Your path must be 4 to 16 alphanumeric characters (no spaces).")
    private String path;

    @Pattern(regexp = "[\\w\\d\\s]{4,64}", message = "Your name must be 4 to 64 characters.")
    private String name;

    @Override
    public String toString() {
        return "RegisterData{" +
                "handle='" + handle + '\'' +
                ", password(length)='" + password.length() + '\'' +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
