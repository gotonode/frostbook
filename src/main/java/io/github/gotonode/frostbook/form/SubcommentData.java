package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SubcommentData {

    @NotEmpty
    @Size(min = 1, max = 255)
    private String comment;
}
