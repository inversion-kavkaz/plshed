package ru.inversion.plshed.entity;

import ru.inversion.dataset.mark.IDMarkable;
import ru.inversion.db.entity.ProxyFor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author XDWeloper
 * @since 2020/12/10 15:57:00
 */
@Entity(name = "ru.inversion.plshed.entity.PIkpTasks")
@Table(name = "IKP_TASKS")
public class PIkpTasks extends IDMarkable implements Serializable {
    private static final long serialVersionUID = 10_12_2020_15_57_00l;

    private Long ITASKID;
    private String CTASKNAME;
    private Long ITASKPERIOD;
    private LocalDate DTASKFROMDT;
    private LocalDate DTASKTODT;
    private LocalDateTime DTASKFROMTM;
    private LocalDateTime DTASKTOTM;

    private Long ITASKFREQUENCY;
    private Long ITASKINTERVAL;
    private Long ITASKSIDE;
    private Long BTASKRUNNING;
    private Long RUNNINGEVENT;
    private Long FTASKRUN;
    private String EXCEPTDAY;

    /**Это транзиентные поля*/
    private Long LOGLEVEL;
    LocalDateTime DTASKFROMTMMV;

    public PIkpTasks() {
    }

    @Id
    @Column(name = "ITASKID", nullable = false, length = 0)
    public Long getITASKID() {
        return ITASKID;
    }

    public void setITASKID(Long val) {
        ITASKID = val;
    }

    @Column(name = "CTASKNAME", nullable = false, length = 250)
    public String getCTASKNAME() {
        return CTASKNAME;
    }

    public void setCTASKNAME(String val) {
        CTASKNAME = val;
    }

    @Column(name = "ITASKPERIOD", nullable = false, length = 1)
    public Long getITASKPERIOD() {
        return ITASKPERIOD;
    }

    public void setITASKPERIOD(Long val) {
        ITASKPERIOD = val;
    }

    @Column(name = "DTASKFROMDT")
    public LocalDate getDTASKFROMDT() {
        return DTASKFROMDT;
    }
    public void setDTASKFROMDT(LocalDate val) {
        DTASKFROMDT = val;
    }

    @Column(name = "DTASKTODT")
    public LocalDate getDTASKTODT() {return DTASKTODT;}
    public void setDTASKTODT(LocalDate val) {DTASKTODT = val;}

    @Column(name = "DTASKFROMTM")
    public LocalDateTime getDTASKFROMTM() {
        return DTASKFROMTM;
    }
    public void setDTASKFROMTM(LocalDateTime val) {
        DTASKFROMTM = val;
    }

    @Column(name = "DTASKTOTM")
    public LocalDateTime getDTASKTOTM() {return DTASKTOTM;}
    public void setDTASKTOTM(LocalDateTime val) {DTASKTOTM = val;}

    @Column(name = "ITASKFREQUENCY", length = 0)
    public Long getITASKFREQUENCY() {
        return ITASKFREQUENCY;
    }

    public void setITASKFREQUENCY(Long val) {
        ITASKFREQUENCY = val;
    }

    @Column(name = "ITASKINTERVAL", length = 0)
    public Long getITASKINTERVAL() {
        return ITASKINTERVAL;
    }

    public void setITASKINTERVAL(Long val) {
        ITASKINTERVAL = val;
    }

    @Column(name = "ITASKSIDE", nullable = false, length = 1)
    public Long getITASKSIDE() {
        return ITASKSIDE;
    }

    public void setITASKSIDE(Long val) {
        ITASKSIDE = val;
    }

    @Column(name = "BTASKRUNNING", nullable = false, length = 1)
    public Long getBTASKRUNNING() {
        return BTASKRUNNING;
    }
    public void setBTASKRUNNING(Long val) {
        BTASKRUNNING = val;
    }

    @Column(name = "RUNNINGEVENT", nullable = false, length = 1)
    public Long getRUNNINGEVENT() {
        return RUNNINGEVENT;
    }
    public void setRUNNINGEVENT(Long RUNNINGEVENT) {
        this.RUNNINGEVENT = RUNNINGEVENT;
    }

    @Column(name = "FTASKRUN", length = 0)
    public Long getFTASKRUN() {return FTASKRUN == null ? 0L : FTASKRUN;}
    public void setFTASKRUN(Long FTASKRUN) {this.FTASKRUN = FTASKRUN == null ? 0L : FTASKRUN;}

    @Column(name = "EXCEPTDAY")
    public String getEXCEPTDAY() {return EXCEPTDAY != null ? EXCEPTDAY : "" ;}
    public void setEXCEPTDAY(String EXCEPTDAY) {this.EXCEPTDAY = EXCEPTDAY;}

    @Transient
    @ProxyFor(columnName = "DTASKFROMTM")
    public LocalTime getDTASKFROMTMV() {
        return  DTASKFROMTM == null ? null : DTASKFROMTM.toLocalTime();
    }
    public void setDTASKFROMTMV(LocalTime val) {  DTASKFROMTM = LocalDateTime.of(DTASKFROMDT, val);}

    @ProxyFor(columnName = "DTASKTOTM")
    public LocalTime getDTASKTOTMV() {return  DTASKTOTM == null ? null : DTASKTOTM.toLocalTime();}
    public void setDTASKTOTMV(LocalTime val) {DTASKTOTM = val != null ? LocalDateTime.of(DTASKFROMDT, val) : null;}

    public LocalDateTime getDTASKFROMTMMV() {return  DTASKFROMTMMV == null ? DTASKFROMTM : DTASKFROMTMMV; }
    public void setDTASKFROMTMMV(LocalDateTime val) {  DTASKFROMTMMV = val;}


    public Long getLOGLEVEL() {return LOGLEVEL;}
    public void setLOGLEVEL(Long LOGLEVEL) {this.LOGLEVEL = LOGLEVEL;}

    @Override
    public Long getMarkLongID() {
        return getITASKID();
    }

    @Override
    public boolean isMark() {
        return super.isMark();
    }

}
