package io.github.gotonode.frostbook;

import io.github.gotonode.frostbook.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileContainer {

    private Profile profile;

    private String plaintextPassword;

}
