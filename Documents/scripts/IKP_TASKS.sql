-- Start of DDL Script for Table XXI.IKP_TASKS
-- Generated 16.11.2020 10:13:13 from XXI@odb604

CREATE TABLE ikp_tasks
    (itaskid                        NUMBER NOT NULL,
    ctaskname                      VARCHAR2(250 BYTE) NOT NULL,
    itaskperiod                    NUMBER(1,0) DEFAULT 0 NOT NULL,
    dtaskfromdt                    DATE DEFAULT sysdate,
    dtaskfromtm                    DATE DEFAULT sysdate,
    itaskfrequency                 NUMBER DEFAULT 0,
    itaskinterval                  NUMBER DEFAULT 1,
    itaskside                      NUMBER(1,0) DEFAULT 0 NOT NULL,
    btaskenabled                   NUMBER(1,0) DEFAULT 0 NOT NULL
  ,
  CONSTRAINT PK_IKP_TASKS
  PRIMARY KEY (itaskid)
  USING INDEX)
  NOPARALLEL
  LOGGING
  MONITORING
/

-- Create synonym IKP_TASKS
CREATE PUBLIC SYNONYM ikp_tasks
  FOR ikp_tasks
/

-- Grants for Table
GRANT DELETE ON ikp_tasks TO odb
/
GRANT INSERT ON ikp_tasks TO odb
/
GRANT SELECT ON ikp_tasks TO odb
/
GRANT UPDATE ON ikp_tasks TO odb
/




-- Triggers for IKP_TASKS

CREATE OR REPLACE TRIGGER tbi_ikp_tasks
 BEFORE
  INSERT
 ON ikp_tasks
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
  IF :NEW.iTaskID is NULL then
    select s_ikp_tasks.NextVal into :NEW.iTaskID from dual;
  END IF;
END;
/


-- Comments for IKP_TASKS

COMMENT ON COLUMN ikp_tasks.btaskenabled IS 'Запущена(1)/не запущена(0)'
/
COMMENT ON COLUMN ikp_tasks.ctaskname IS 'Наименование задачи'
/
COMMENT ON COLUMN ikp_tasks.dtaskfromdt IS 'Дата старта'
/
COMMENT ON COLUMN ikp_tasks.dtaskfromtm IS 'Время старта'
/
COMMENT ON COLUMN ikp_tasks.itaskfrequency IS 'Частота выполнения'
/
COMMENT ON COLUMN ikp_tasks.itaskid IS 'ID задачи'
/
COMMENT ON COLUMN ikp_tasks.itaskinterval IS 'Интервал выполнения'
/
COMMENT ON COLUMN ikp_tasks.itaskperiod IS 'Период выполнения'
/
COMMENT ON COLUMN ikp_tasks.itaskside IS 'Сторона выполнения (клиент-0/сервер-1, по умолчанию 0)'
/

-- End of DDL Script for Table XXI.IKP_TASKS

