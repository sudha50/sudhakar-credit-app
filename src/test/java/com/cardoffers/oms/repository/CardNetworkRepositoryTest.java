import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CardNetworkRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardNetworkRepository repository;

    @Test
    @DisplayName("should save and retrieve entity")
    void shouldSaveAndRetrieve() {
        assertThat(repository).isNotNull();
    }
}
