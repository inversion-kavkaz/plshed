package ru.inversion.plshed.entity;

import ru.inversion.dataset.mark.IDMarkable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private String DTASKFROMTM;
    private Long ITASKFREQUENCY;
    private Long ITASKINTERVAL;
    private Long ITASKSIDE;
    private Long BTASKRUNNING;
    private LocalTime DTASKFROMTMV;

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

    @Column(name = "DTASKFROMTM")
    public String getDTASKFROMTM() {
        return DTASKFROMTM;
    }

    public void setDTASKFROMTM(String val) {
        DTASKFROMTM = val;
    }

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

    @Transient
    @Override
    public Long getMarkLongID() {
        return getITASKID();
    }

    @Override
    public boolean isMark() {
        return super.isMark();
    }

    @Transient
    public LocalTime getDTASKFROMTMV() {
            return (getDTASKFROMTM() != null && !getDTASKFROMTM().isEmpty()) ? LocalTime.parse(getDTASKFROMTM()): null;
    }

    public void setDTASKFROMTMV(LocalTime DTASKFROMTMV) {
        this.DTASKFROMTMV = DTASKFROMTMV;
    }
}
