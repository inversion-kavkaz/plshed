package ru.inversion.plshed.entity.lovEntity;



import javax.persistence.*;
import java.io.Serializable;

/**
@author  XDWeloper
@since   2021/04/16 16:29:26
*/
@Entity (name="ru.inversion.plshed.entity.lovEntity.PDual")
@Table(name="DUAL")
@NamedNativeQuery(name = "query", query = "select '' as VAL,'' as DESCR FROM DUAL")
public class PDual implements Serializable
{
    private static final long serialVersionUID = 16_04_2021_16_29_26l;

    private String VAL;
    private String DESCR;

    public PDual(){}

    @Id
    @Column(name="VAL",length = 1)
    public String getVAL() {
        return VAL;
    }
    public void setVAL(String val) {
        VAL = val;
    }
    @Id
    @Column(name="DESCR",length = 7)
    public String getDESCR() {
        return DESCR;
    }
    public void setDESCR(String val) {
        DESCR = val;
    }
}
