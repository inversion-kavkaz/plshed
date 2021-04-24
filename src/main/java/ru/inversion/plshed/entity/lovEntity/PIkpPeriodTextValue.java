package ru.inversion.plshed.entity.lovEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author XDWeloper
 * @since 2020/12/11 13:11:17
 */
@Entity(name = "ru.inversion.plshed.entity.PIkpRunningType")
@NamedNativeQuery(
        name = "period",
        query = "SELECT UPPER(PARENT_ID) as PARENT_ID, UPPER(VALUE) as VALUE FROM ikp_text_value where parent_alias ='period'"
)

public class PIkpPeriodTextValue implements Serializable, lovUtils.LovInterface {
    private static final long serialVersionUID = 11_12_2020_13_11_17l;

    private Long ID;
    private String VALUE;

    public PIkpPeriodTextValue() {
    }

    @Column(name = "PARENT_ID", nullable = false, length = 0)
    public Long getID() {
        return ID;
    }
    public void setID(Long val) {
        ID = val;
    }

    @Id
    @Column(name = "VALUE", length = 50)
    public String getVALUE() {
        return VALUE;
    }
    public void setVALUE(String val) {
        VALUE = val;
    }

    @Transient
    @Override
    public Object getKey() {
        return getID();
    }

    @Override
    public Object getValue() {
        return getVALUE();
    }
}
