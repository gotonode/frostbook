package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentData {

    @NotEmpty
    @Size(min = 1, max = 1024)
    private String comment;
}
