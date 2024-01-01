package com.topiatest.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.topiatest.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopiaUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopiaUser.class);
        TopiaUser topiaUser1 = new TopiaUser();
        topiaUser1.setId(1L);
        TopiaUser topiaUser2 = new TopiaUser();
        topiaUser2.setId(topiaUser1.getId());
        assertThat(topiaUser1).isEqualTo(topiaUser2);
        topiaUser2.setId(2L);
        assertThat(topiaUser1).isNotEqualTo(topiaUser2);
        topiaUser1.setId(null);
        assertThat(topiaUser1).isNotEqualTo(topiaUser2);
    }
}
