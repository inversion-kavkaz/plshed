package ru.inversion.test;

import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.io.Serializable;
import javax.persistence.*;
import ru.inversion.dataset.mark.*;
import ru.inversion.db.entity.ProxyFor;

/**
@author  XDWeloper
@since   2020/12/14 11:53:48
*/
@Entity (name="ru.inversion.test.PIkpTasks")
@Table (name="IKP_TASKS")
public class PIkpTasks implements Serializable
{
    private static final long serialVersionUID = 14_12_2020_11_53_48l;

    private BigDecimal ITASKID;
    private String CTASKNAME;
    private Long ITASKPERIOD;
    private LocalDate DTASKFROMDT;
    private String DTASKFROMTM;
    private BigDecimal ITASKFREQUENCY;
    private BigDecimal ITASKINTERVAL;
    private Long ITASKSIDE;
    private Long BTASKRUNNING;

    public PIkpTasks(){}

    @Id 
    @Column(name="ITASKID",nullable = false,length = 0)
    public BigDecimal getITASKID() {
        return ITASKID;
    }
    public void setITASKID(BigDecimal val) {
        ITASKID = val; 
    }
    @Column(name="CTASKNAME",nullable = false,length = 250)
    public String getCTASKNAME() {
        return CTASKNAME;
    }
    public void setCTASKNAME(String val) {
        CTASKNAME = val; 
    }
    @Column(name="ITASKPERIOD",nullable = false,length = 1)
    public Long getITASKPERIOD() {
        return ITASKPERIOD;
    }
    public void setITASKPERIOD(Long val) {
        ITASKPERIOD = val; 
    }
    @Column(name="DTASKFROMDT")
    public LocalDate getDTASKFROMDT() {
        return DTASKFROMDT;
    }
    public void setDTASKFROMDT(LocalDate val) {
        DTASKFROMDT = val; 
    }
    @Column(name="DTASKFROMTM")
    public String getDTASKFROMTM() {
        return DTASKFROMTM;
    }
    public void setDTASKFROMTM(String val) {
        DTASKFROMTM = val; 
    }
    @Column(name="ITASKFREQUENCY",length = 0)
    public BigDecimal getITASKFREQUENCY() {
        return ITASKFREQUENCY;
    }
    public void setITASKFREQUENCY(BigDecimal val) {
        ITASKFREQUENCY = val; 
    }
    @Column(name="ITASKINTERVAL",length = 0)
    public BigDecimal getITASKINTERVAL() {
        return ITASKINTERVAL;
    }
    public void setITASKINTERVAL(BigDecimal val) {
        ITASKINTERVAL = val; 
    }
    @Column(name="ITASKSIDE",nullable = false,length = 1)
    public Long getITASKSIDE() {
        return ITASKSIDE;
    }
    public void setITASKSIDE(Long val) {
        ITASKSIDE = val; 
    }
    @Column(name="BTASKRUNNING",nullable = false,length = 1)
    public Long getBTASKRUNNING() {
        return BTASKRUNNING;
    }
    public void setBTASKRUNNING(Long val) {
        BTASKRUNNING = val; 
    }
}