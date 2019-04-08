package io.github.gotonode.frostbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Welcome to Frostbook!
 *
 * To view a live version of this project, please go to the following URL:
 * <a href="https://frostbook.herokuapp.com/">Frostbook</a> (Heroku)
 *
 * Since it's hosted on a free Heroku dyno, please allow up to 10 seconds
 * for the app to start on your first request. Subsequent requests will be
 * served immediately (up until 30 minutes are up upon which the app sleeps).
 *
 * To view the source of this project in a more comfortable fashion, go to:
 * <a href="https://github.com/gotonode/frostbook">Frostbook</a> (GitHub).
 *
 * Feedback is most welcome. Thank you in advance!
 *
 * - gotonode
 */
@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class);
    }

}
