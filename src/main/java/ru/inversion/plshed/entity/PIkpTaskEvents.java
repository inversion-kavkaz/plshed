package ru.inversion.plshed.entity;

import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.io.Serializable;
import javax.persistence.*;
import ru.inversion.dataset.mark.*;
import ru.inversion.db.entity.ProxyFor;

/**
@author  XDWeloper
@since   2020/12/10 16:07:19
*/
@Entity (name="ru.inversion.plshed.entity.PIkpTaskEvents")
@Table (name="IKP_TASK_EVENTS")
public class PIkpTaskEvents extends IDMarkable implements Serializable
{
    private static final long serialVersionUID = 10_12_2020_16_07_19l;

    private Long IEVENTID;
    private Long IEVENTTASKID;
    private Long IEVENTNPP;
    private String CEVENTNAME;
    private Long IEVENTTYPE;
    private Long IEVENTPRESETID;
    private String LEVENTTEXT;
    private String CEVENTINDIR;
    private String CEVENTOUTDIR;
    private String CEVENTARHDIR;
    private Long BEVENTENABLED;
    private Long IEVENTFILEDIR;

    public PIkpTaskEvents(){}

    @Column(name="IEVENTID",nullable = false,length = 0)
    public Long getIEVENTID() {
        return IEVENTID;
    }
    public void setIEVENTID(Long val) {
        IEVENTID = val; 
    }
    @Id 
    @Column(name="IEVENTTASKID",nullable = false,length = 0)
    public Long getIEVENTTASKID() {
        return IEVENTTASKID;
    }
    public void setIEVENTTASKID(Long val) {
        IEVENTTASKID = val; 
    }
    @Id 
    @Column(name="IEVENTNPP",nullable = false,length = 100)
    public Long getIEVENTNPP() {
        return IEVENTNPP;
    }
    public void setIEVENTNPP(Long val) {
        IEVENTNPP = val; 
    }
    @Column(name="CEVENTNAME",nullable = false,length = 250)
    public String getCEVENTNAME() {
        return CEVENTNAME;
    }
    public void setCEVENTNAME(String val) {
        CEVENTNAME = val; 
    }
    @Column(name="IEVENTTYPE",nullable = false,length = 1)
    public Long getIEVENTTYPE() {
        return IEVENTTYPE;
    }
    public void setIEVENTTYPE(Long val) {
        IEVENTTYPE = val; 
    }
    @Column(name="IEVENTPRESETID",length = 100)
    public Long getIEVENTPRESETID() {
        return IEVENTPRESETID;
    }
    public void setIEVENTPRESETID(Long val) {
        IEVENTPRESETID = val; 
    }
    @Column(name="LEVENTTEXT")
    public String getLEVENTTEXT() {
        return LEVENTTEXT;
    }
    public void setLEVENTTEXT(String val) {
        LEVENTTEXT = val; 
    }
    @Column(name="CEVENTINDIR",length = 2000)
    public String getCEVENTINDIR() {
        return CEVENTINDIR;
    }
    public void setCEVENTINDIR(String val) {
        CEVENTINDIR = val; 
    }
    @Column(name="CEVENTOUTDIR",length = 2000)
    public String getCEVENTOUTDIR() {
        return CEVENTOUTDIR;
    }
    public void setCEVENTOUTDIR(String val) {
        CEVENTOUTDIR = val; 
    }
    @Column(name="CEVENTARHDIR",length = 2000)
    public String getCEVENTARHDIR() {
        return CEVENTARHDIR;
    }
    public void setCEVENTARHDIR(String val) {
        CEVENTARHDIR = val; 
    }
    @Column(name="BEVENTENABLED",nullable = false,length = 1)
    public Long getBEVENTENABLED() {
        return BEVENTENABLED;
    }
    public void setBEVENTENABLED(Long val) {
        BEVENTENABLED = val; 
    }
    @Column(name="IEVENTFILEDIR",nullable = false,length = 1)
    public Long getIEVENTFILEDIR() {
        return IEVENTFILEDIR;
    }
    public void setIEVENTFILEDIR(Long val) {
        IEVENTFILEDIR = val; 
    }
    @Transient
    @Override
    public Long getMarkLongID() {
        return getIEVENTTASKID();
    }
    @Override
    public boolean isMark() {
        return super.isMark();
    }
}
