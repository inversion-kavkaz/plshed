-- Start of DDL Script for Table XXI.IKP_TASK_EVENTS
-- Generated 16.11.2020 12:35:54 from XXI@odb604

-- Drop the old instance of IKP_TASK_EVENTS
DROP TABLE ikp_task_events CASCADE CONSTRAINTS
/

CREATE TABLE ikp_task_events
    (ieventid                       NUMBER NOT NULL,
    ieventtaskid                   NUMBER NOT NULL,
    ieventnpp                      NUMBER NOT NULL,
    ceventname                     VARCHAR2(250 BYTE) NOT NULL,
    ieventtype                     NUMBER(1,0) NOT NULL,
    ieventpresetid                 NUMBER,
    leventtext                     CLOB,
    ceventindir                    VARCHAR2(2000 BYTE),
    ceventoutdir                   VARCHAR2(2000 BYTE),
    ceventarhdir                   VARCHAR2(2000 BYTE),
    beventenabled                  NUMBER(1,0) DEFAULT 1 NOT NULL,
    ieventfiledir                  NUMBER(1,0) DEFAULT -1 NOT NULL
  ,
  CONSTRAINT PK_IKP_TASK_EVENTS
  PRIMARY KEY (ieventtaskid, ieventnpp)
  USING INDEX)
  SEGMENT CREATION IMMEDIATE
  LOB ("LEVENTTEXT") STORE AS BASICFILE
  (
   NOCACHE LOGGING
   CHUNK 8192
  )
  NOPARALLEL
  LOGGING
  MONITORING
/

-- Drop the old synonym IKP_TASK_EVENTS
DROP PUBLIC SYNONYM IKP_TASK_EVENTS
/

-- Create synonym IKP_TASK_EVENTS
CREATE PUBLIC SYNONYM ikp_task_events
  FOR ikp_task_events
/

-- Grants for Table
GRANT DELETE ON ikp_task_events TO odb
/
GRANT INSERT ON ikp_task_events TO odb
/
GRANT SELECT ON ikp_task_events TO odb
/
GRANT UPDATE ON ikp_task_events TO odb
/




-- Indexes for IKP_TASK_EVENTS

CREATE UNIQUE INDEX u_ikp_task_events_id ON ikp_task_events
  (
    ieventid                        ASC
  )
NOPARALLEL
LOGGING
/



-- Constraints for IKP_TASK_EVENTS




-- Triggers for IKP_TASK_EVENTS

CREATE OR REPLACE TRIGGER tbi_ikp_task_events
 BEFORE
  INSERT
 ON ikp_task_events
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
  IF :NEW.iEventID is NULL then
    select s_ikp_task_events.NextVal into :NEW.iEventID from dual;
  END IF;
END;
/


-- Comments for IKP_TASK_EVENTS

COMMENT ON COLUMN ikp_task_events.beventenabled IS 'Вкл/выкл'
/
COMMENT ON COLUMN ikp_task_events.ceventarhdir IS 'Архивная директория'
/
COMMENT ON COLUMN ikp_task_events.ceventindir IS 'Входящая директория'
/
COMMENT ON COLUMN ikp_task_events.ceventname IS 'Наименование действия'
/
COMMENT ON COLUMN ikp_task_events.ceventoutdir IS 'Исходящая директория'
/
COMMENT ON COLUMN ikp_task_events.ieventfiledir IS 'Тип работы с файлами (-1 не файл/0 загрузка/1 выгрузка)'
/
COMMENT ON COLUMN ikp_task_events.ieventid IS 'ID действия'
/
COMMENT ON COLUMN ikp_task_events.ieventnpp IS '№ п/п'
/
COMMENT ON COLUMN ikp_task_events.ieventpresetid IS 'ID пресета'
/
COMMENT ON COLUMN ikp_task_events.ieventtaskid IS 'ID задачи действия'
/
COMMENT ON COLUMN ikp_task_events.ieventtype IS 'Тип действия (0-preset, 1-PLSQL, 2-script, 3-OS cmd)'
/
COMMENT ON COLUMN ikp_task_events.leventtext IS 'Текст PLSQL/скрипта'
/

-- End of DDL Script for Table XXI.IKP_TASK_EVENTS

-- Foreign Key
ALTER TABLE ikp_task_events
ADD CONSTRAINT fk_ikp_task_events FOREIGN KEY (ieventtaskid)
REFERENCES ikp_tasks (itaskid)
/
ALTER TABLE ikp_task_events
ADD CONSTRAINT fk_ikp_task_events_preset FOREIGN KEY (ieventpresetid)
REFERENCES ikp_event_presets (ipresetid)
/
-- End of DDL script for Foreign Key(s)
