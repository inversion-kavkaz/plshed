package ru.inversion.plshed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
@author  XDWeloper
@since   2021/04/15 12:47:31
*/
@Entity (name="ru.inversion.plshed.entity.PIkpPresetGr")
@Table (name="IKP_PRESET_GR")
public class PIkpPresetGr implements Serializable
{
    private static final long serialVersionUID = 15_04_2021_12_47_31l;

    private Long IPRESETGRID;
    private String CPRESETGRNAME;

    public PIkpPresetGr(){}

    @Id 
    @Column(name="IPRESETGRID",length = 0)
    public Long getIPRESETGRID() {return IPRESETGRID;}
    public void setIPRESETGRID(Long val) {
        IPRESETGRID = val; 
    }
    @Column(name="CPRESETGRNAME",length = 100)
    public String getCPRESETGRNAME() {
        return CPRESETGRNAME;
    }
    public void setCPRESETGRNAME(String val) {
        CPRESETGRNAME = val; 
    }
}