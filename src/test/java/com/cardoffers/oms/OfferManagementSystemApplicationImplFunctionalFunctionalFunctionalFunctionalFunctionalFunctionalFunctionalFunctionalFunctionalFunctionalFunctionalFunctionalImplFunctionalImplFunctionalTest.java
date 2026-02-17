import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OfferManagementSystemApplicationImplFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalImplFunctionalImplFunctionalTest {

    @Autowired
    private OfferManagementSystemApplicationImplFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalFunctionalImplFunctionalImpl subject;

    @Test
    @DisplayName("functional: should wire and execute")
    void shouldWireAndExecute() {
        assertThat(subject).isNotNull();
    }
}
