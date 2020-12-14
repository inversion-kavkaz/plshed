-- Start of DDL Script for Table XXI.IKP_EVENT_PRESETS
-- Generated 16.11.2020 10:25:36 from XXI@odb604

CREATE TABLE ikp_event_presets
    (ipresetid                      NUMBER NOT NULL,
    cpresetname                    VARCHAR2(250 BYTE) NOT NULL,
    cpresettext                    VARCHAR2(4000 BYTE) NOT NULL
  ,
  CONSTRAINT PK_IK_PRESETS
  PRIMARY KEY (ipresetid)
  USING INDEX)
  NOPARALLEL
  LOGGING
  MONITORING
/

-- Create synonym IKP_EVENT_PRESETS
CREATE PUBLIC SYNONYM ikp_event_presets
  FOR ikp_event_presets
/

-- Grants for Table
GRANT DELETE ON ikp_event_presets TO odb
/
GRANT INSERT ON ikp_event_presets TO odb
/
GRANT SELECT ON ikp_event_presets TO odb
/
GRANT UPDATE ON ikp_event_presets TO odb
/




-- Triggers for IKP_EVENT_PRESETS

CREATE OR REPLACE TRIGGER tbi_ikp_event_presets
 BEFORE
  INSERT
 ON ikp_event_presets
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
  IF :NEW.iPresetID is NULL then
    select s_ikp_event_presets.NextVal into :NEW.iPresetID from dual;
  END IF;
END;
/


-- Comments for IKP_EVENT_PRESETS

COMMENT ON TABLE ikp_event_presets IS 'Предустановленные действия. IKP.'
/
COMMENT ON COLUMN ikp_event_presets.cpresetname IS 'Наименование пресета'
/
COMMENT ON COLUMN ikp_event_presets.cpresettext IS 'Текст пресета'
/
COMMENT ON COLUMN ikp_event_presets.ipresetid IS 'ID пресета'
/

-- End of DDL Script for Table XXI.IKP_EVENT_PRESETS

