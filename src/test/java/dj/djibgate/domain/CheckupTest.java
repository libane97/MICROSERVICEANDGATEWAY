package dj.djibgate.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import dj.djibgate.web.rest.TestUtil;

public class CheckupTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Checkup.class);
        Checkup checkup1 = new Checkup();
        checkup1.setId(1L);
        Checkup checkup2 = new Checkup();
        checkup2.setId(checkup1.getId());
        assertThat(checkup1).isEqualTo(checkup2);
        checkup2.setId(2L);
        assertThat(checkup1).isNotEqualTo(checkup2);
        checkup1.setId(null);
        assertThat(checkup1).isNotEqualTo(checkup2);
    }
}
