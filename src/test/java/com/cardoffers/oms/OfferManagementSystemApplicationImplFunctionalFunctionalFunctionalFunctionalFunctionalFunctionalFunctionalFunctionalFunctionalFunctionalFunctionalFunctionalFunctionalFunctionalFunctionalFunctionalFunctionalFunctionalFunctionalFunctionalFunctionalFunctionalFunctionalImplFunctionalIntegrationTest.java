import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OfferManagementSystemApplicationImplFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalImplFunctionalIntegrationTest {

    @Autowired
    private OfferManagementSystemApplicationImplFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalImplFunctional subject;

    @Test
    @DisplayName("integration: should wire all dependencies")
    void shouldWireAllDependencies() {
        assertThat(subject).isNotNull();
    }
}
