package io.github.gotonode.frostbook.form;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SubcommentData {

    @Size(min = 1, max = 255, message = "Your subcomment must be 1 to 255 characters.")
    private String comment;

    @Override
    public String toString() {
        return "SubcommentData{" +
                "comment='" + comment + '\'' +
                '}';
    }
}
