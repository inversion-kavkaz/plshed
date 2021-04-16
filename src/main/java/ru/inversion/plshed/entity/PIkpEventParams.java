package ru.inversion.plshed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
@author  XDWeloper
@since   2021/04/16 13:48:19
*/
@Entity (name="ru.inversion.plshed.entity.PIkpEventParams")
@Table (name="IKP_EVENT_PARAMS")
public class PIkpEventParams implements Serializable
{
    private static final long serialVersionUID = 16_04_2021_13_48_19l;


/*
* ID задачи
*/
    private BigDecimal IEVENTID;

/*
* Имя параметра
*/
    private String CPARAMNAME;

/*
* Значение параметра
*/
    private String CPARAMVALUE;

    private String CPARAMFULLNAME;

    public PIkpEventParams(){}

    @Id 
    @Column(name="IEVENTID",nullable = false,length = 0)
    public BigDecimal getIEVENTID() {
        return IEVENTID;
    }
    public void setIEVENTID(BigDecimal val) {
        IEVENTID = val; 
    }
    @Id 
    @Column(name="CPARAMNAME",nullable = false,length = 200)
    public String getCPARAMNAME() {
        return CPARAMNAME;
    }
    public void setCPARAMNAME(String val) {
        CPARAMNAME = val; 
    }
    @Column(name="CPARAMVALUE",length = 200)
    public String getCPARAMVALUE() {
        return CPARAMVALUE;
    }
    public void setCPARAMVALUE(String val) {
        CPARAMVALUE = val; 
    }

    @Column(name="CPARAMFULLNAME",length = 250,columnDefinition = "(select a.CPARAMFULLNAME from IKP_PRESET_PARAMS a where a.CPARAMNAME = CPARAMNAME " +
            "and a.ID_PRESET = :PRESET_ID)",
            insertable = false,updatable = false)
    public String getCPARAMFULLNAME() {return CPARAMFULLNAME;}
    public void setCPARAMFULLNAME(String CPARAMFULLNAME) {this.CPARAMFULLNAME = CPARAMFULLNAME;}
}
