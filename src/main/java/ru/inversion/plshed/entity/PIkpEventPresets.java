package ru.inversion.plshed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
@author  XDWeloper
@since   2021/02/17 12:18:24
*/
@Entity (name="ru.inversion.plshed.entity.PIkpEventPresets")
@Table (name="IKP_EVENT_PRESETS")
public class PIkpEventPresets implements Serializable
{
    private static final long serialVersionUID = 17_02_2021_12_18_24l;

    private Long IPRESETID;
    private String CPRESETNAME;
    private String CPRESETTEXT;
    private Long IEVENTFILEDIR;
    private Long IPRESETGR;

    public PIkpEventPresets(){}

    @Id 
    @Column(name="IPRESETID",length = 0)
    public Long getIPRESETID() {
        return IPRESETID;
    }
    public void setIPRESETID(Long val) {
        IPRESETID = val; 
    }
    @Column(name="CPRESETNAME",nullable = false,length = 250)
    public String getCPRESETNAME() {
        return CPRESETNAME;
    }
    public void setCPRESETNAME(String val) {
        CPRESETNAME = val; 
    }
    @Column(name="CPRESETTEXT",length = 4000)
    public String getCPRESETTEXT() {
        return CPRESETTEXT;
    }
    public void setCPRESETTEXT(String val) {
        CPRESETTEXT = val; 
    }
    @Column(name="IEVENTFILEDIR",nullable = false,length = 1)
    public Long getIEVENTFILEDIR() {
        return IEVENTFILEDIR;
    }
    public void setIEVENTFILEDIR(Long val) {
        IEVENTFILEDIR = val;
    }
    @Column(name="IPRESETGR",nullable = false,length = 1)
    public Long getIPRESETGR() {return IPRESETGR;}
    public void setIPRESETGR(Long IPRESETGR) {this.IPRESETGR = IPRESETGR;}
}
