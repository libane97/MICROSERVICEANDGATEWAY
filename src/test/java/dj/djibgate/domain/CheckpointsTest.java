package dj.djibgate.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import dj.djibgate.web.rest.TestUtil;

public class CheckpointsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Checkpoints.class);
        Checkpoints checkpoints1 = new Checkpoints();
        checkpoints1.setId(1L);
        Checkpoints checkpoints2 = new Checkpoints();
        checkpoints2.setId(checkpoints1.getId());
        assertThat(checkpoints1).isEqualTo(checkpoints2);
        checkpoints2.setId(2L);
        assertThat(checkpoints1).isNotEqualTo(checkpoints2);
        checkpoints1.setId(null);
        assertThat(checkpoints1).isNotEqualTo(checkpoints2);
    }
}
