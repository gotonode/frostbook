package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CommentData {

    @Size(min = 1, max = 1024, message = "Your comment must be 1 to 1024 characters.")
    private String comment;

    @Override
    public String toString() {
        return "CommentData{" +
                "comment='" + comment + '\'' +
                '}';
    }
}
