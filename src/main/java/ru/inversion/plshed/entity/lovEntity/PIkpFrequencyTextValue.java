package ru.inversion.plshed.entity.lovEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import java.io.Serializable;

/**
 * @author XDWeloper
 * @since 2020/12/11 13:11:17
 */
@Entity(name = "ru.inversion.plshed.entity.PIkpRunningType")
@NamedNativeQuery(
        name = "frequency",
        query = "SELECT * FROM ikp_text_value where parent_alias ='frequency'"
)

public class PIkpFrequencyTextValue implements Serializable, lovUtils.LovInterface {
    private static final long serialVersionUID = 11_12_2020_13_11_17l;

    private Long ID;
    private String VALUE;

    public PIkpFrequencyTextValue() {
    }

    @Id
    @Column(name = "PARENT_ID", nullable = false, length = 0)
    public Long getID() {
        return ID;
    }

    public void setID(Long val) {
        ID = val;
    }

    @Column(name = "VALUE", length = 50)
    public String getVALUE() {
        return VALUE;
    }

    public void setVALUE(String val) {
        VALUE = val;
    }


    @Override
    public Object getKey() {
        return getID();
    }

    @Override
    public Object getValue() {
        return getVALUE();
    }
}
