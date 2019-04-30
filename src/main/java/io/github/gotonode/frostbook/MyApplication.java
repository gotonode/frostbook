package io.github.gotonode.frostbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Welcome to Frostbook!
 * <p>
 * To view a live version of this project, please go to the following URL:
 * <a href="https://frostbook.herokuapp.com/">Frostbook</a> (Heroku)
 * <p>
 * Since it's hosted on a free Heroku dyno, please allow up to 10 seconds
 * for the app to start on your first request. Subsequent requests will be
 * served immediately (up until 30 minutes are up upon which the app sleeps).
 * <p>
 * To view the source of this project in a more comfortable fashion, go to:
 * <a href="https://github.com/gotonode/frostbook">Frostbook</a> (GitHub).
 * <p>
 * Feedback is most welcome. Thank you in advance!
 * <p>
 * - gotonode
 */
@SpringBootApplication
@ComponentScan({
        "io.github.gotonode.frostbook.controller",
        "io.github.gotonode.frostbook.repository",
        "io.github.gotonode.frostbook.security",
        "io.github.gotonode.frostbook.domain",
        "io.github.gotonode.frostbook.service",
        "io.github.gotonode.frostbook.form",
        "io.github.gotonode.frostbook.details"})
public class MyApplication {

    public static final String APP_NAME = "Frostbook";
    public static final String PROTECTED_STRING = "[PROTECTED]";

    public static final int MAX_GALLERY_IMAGES_PER_USER = 10;

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
