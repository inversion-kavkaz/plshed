package ru.inversion.plshed.entity.lovEntity;

import ru.inversion.dataset.mark.UMarkable;

import javax.persistence.*;
import java.io.Serializable;

/**
@author  XDWeloper
@since   2021/04/19 13:43:57
*/
@Entity (name="ru.inversion.plshed.entity.lovEntity.PDual")
@Table (name="DUAL")
public class PDual extends UMarkable implements Serializable
{
    private static final long serialVersionUID = 19_04_2021_13_43_57l;

    private String VAL;
    private String DESCR;

    public PDual(){}

    @Id 
    @Column(name="VAL",length = 0)
    public String getVAL() {
        return VAL;
    }
    public void setVAL(String val) {
        VAL = val; 
    }
    @Id 
    @Column(name="DESCR",length = 0)
    public String getDESCR() {
        return DESCR;
    }
    public void setDESCR(String val) {
        DESCR = val; 
    }
    @Transient
    @Override
    public String getMarkStringID() {
        return getVAL();
    }
    @Override
    public boolean isMark() {
        return super.isMark();
    }
}