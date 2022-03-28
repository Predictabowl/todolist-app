package it.aldinucci.todoapp.adapter.out;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The purpose of this class is to enable context configuration for other test classes.
 * Since the main application is not visible from this module, is not possible to configure
 * Hibernate and thus @DataJpaTest won't work without this class.
 * For it to work it must be placed in the same or a parent package of the classes who need it.
 * @author piero
 *
 */

@SpringBootApplication
public class MockApplication {
}
