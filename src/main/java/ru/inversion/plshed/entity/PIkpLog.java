package ru.inversion.plshed.entity;

import ru.inversion.dataset.mark.IDMarkable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
@author  XDWeloper
@since   2020/12/21 10:29:31
*/
@Entity (name="ru.inversion.plshed.entity.PIkpLog")
@Table (name="IKP_LOG")
public class PIkpLog extends IDMarkable implements Serializable
{
    private static final long serialVersionUID = 21_12_2020_10_29_31l;

    private Long SESSID;
    private Long STRNO;
    private String STRTYPE;
    private String MSGTXT;
    private LocalDateTime DT;
    private String LOGNAME;
    private Long TASKID;

    public PIkpLog(){}

    @Id 
    @Column(name="SESSID",nullable = false,length = 38)
    public Long getSESSID() {
        return SESSID;
    }
    public void setSESSID(Long val) {
        SESSID = val; 
    }
    @Id 
    @Column(name="STRNO",nullable = false,length = 5)
    public Long getSTRNO() {
        return STRNO;
    }
    public void setSTRNO(Long val) {
        STRNO = val; 
    }
    @Id 
    @Column(name="STRTYPE",nullable = false,length = 5)
    public String getSTRTYPE() {
        return STRTYPE;
    }
    public void setSTRTYPE(String val) {
        STRTYPE = val; 
    }
    @Column(name="MSGTXT",length = 4000)
    public String getMSGTXT() {
        return MSGTXT;
    }
    public void setMSGTXT(String val) {
        MSGTXT = val; 
    }
    @Id 
    @Column(name="DT",nullable = false)
    public LocalDateTime getDT() {
        return DT;
    }
    public void setDT(LocalDateTime val) {
        DT = val; 
    }
    @Column(name="LOGNAME",length = 30)
    public String getLOGNAME() {
        return LOGNAME;
    }
    public void setLOGNAME(String val) {
        LOGNAME = val; 
    }
    @Column(name="TASKID",length = 0)
    public Long getTASKID() {
        return TASKID;
    }
    public void setTASKID(Long val) {
        TASKID = val; 
    }

    @Transient
    @Override
    public Long getMarkLongID() {
        return getTASKID();
    }
    @Override
    public boolean isMark() {
        return super.isMark();
    }

}
