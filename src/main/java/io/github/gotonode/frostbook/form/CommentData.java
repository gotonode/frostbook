package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentData {

    @NotEmpty(message = "Your comment cannot be empty.")
    @Size(min = 1, max = 1024, message = "Your comment must be between 1 and 1024 characters.")
    private String comment;

    @Override
    public String toString() {
        return "CommentData{" +
                "comment='" + comment + '\'' +
                '}';
    }
}
