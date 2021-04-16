package ru.inversion.plshed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
@author  XDWeloper
@since   2021/02/17 12:15:23
*/
@Entity (name="ru.inversion.plshed.entity.PIkpPresetParams")
@Table (name="IKP_PRESET_PARAMS")
public class PIkpPresetParams implements Serializable
{
    private static final long serialVersionUID = 17_02_2021_12_15_23l;

    private Long ID_PRESET;
    private String CPARAMFULLNAME;
    private String CPARAMNAME;
    private Long IS_SPR;
    private String CSPRSQL;
    private Long IS_MULTI;

    public PIkpPresetParams(){}

    @Id 
    @Column(name="ID_PRESET",nullable = false,length = 0)
    public Long getID_PRESET() {
        return ID_PRESET;
    }
    public void setID_PRESET(Long val) {
        ID_PRESET = val; 
    }
    @Column(name="CPARAMFULLNAME",nullable = false,length = 250)
    public String getCPARAMFULLNAME() {
        return CPARAMFULLNAME;
    }
    public void setCPARAMFULLNAME(String val) {
        CPARAMFULLNAME = val; 
    }
    @Id 
    @Column(name="CPARAMNAME",nullable = false,length = 200)
    public String getCPARAMNAME() {
        return CPARAMNAME;
    }
    public void setCPARAMNAME(String val) {
        CPARAMNAME = val; 
    }
    @Column(name="IS_SPR",nullable = false,length = 1)
    public Long getIS_SPR() {
        return IS_SPR;
    }
    public void setIS_SPR(Long val) {
        IS_SPR = val; 
    }
    @Column(name="CSPRSQL")
    public String getCSPRSQL() {
        return CSPRSQL;
    }
    public void setCSPRSQL(String val) {
        CSPRSQL = val; 
    }
    @Column(name="IS_MULTI",nullable = false,length = 1)
    public Long getIS_MULTI() {
        return IS_MULTI;
    }
    public void setIS_MULTI(Long val) {
        IS_MULTI = val; 
    }
}