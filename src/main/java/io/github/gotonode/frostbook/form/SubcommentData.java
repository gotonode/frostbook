package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SubcommentData {

    @NotEmpty(message = "Your subcomment cannot be empty.")
    @Size(min = 1, max = 255, message = "Your subcomment must be between 1 and 255 characters.")
    private String comment;

    @Override
    public String toString() {
        return "SubcommentData{" +
                "comment='" + comment + '\'' +
                '}';
    }
}
