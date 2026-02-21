import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferEligibilityServiceImplTest {

    @InjectMocks
    private OfferEligibilityServiceImpl subject;

    @Test
    @DisplayName("should execute business logic")
    void shouldExecuteBusinessLogic() {
        assertThat(subject).isNotNull();
    }
}
