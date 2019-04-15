package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginData {

    @NotEmpty
    @Size(min = 3, max = 10)
    private String handle;

    @NotEmpty
    //@Size(min = 64, max = 64)
    private String password;
}
