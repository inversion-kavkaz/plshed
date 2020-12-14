-- Start of DDL Script for Table XXI.IKP_LOG
-- Generated 16.11.2020 16:25:46 from XXI@odb604

-- Drop the old instance of IKP_LOG
DROP TABLE ikp_log CASCADE CONSTRAINTS
/

CREATE TABLE ikp_log
    (sessid                         NUMBER(*,0) NOT NULL,
    strno                          NUMBER(5,0) NOT NULL,
    strtype                        VARCHAR2(5 BYTE) NOT NULL,
    msgtxt                         VARCHAR2(4000 BYTE),
    dt                             DATE NOT NULL,
    logname                        VARCHAR2(30 BYTE))
  SEGMENT CREATION IMMEDIATE
  NOPARALLEL
  LOGGING
  MONITORING
/

-- Drop the old synonym IKP_LOG
DROP PUBLIC SYNONYM IKP_LOG
/

-- Create synonym IKP_LOG
CREATE PUBLIC SYNONYM ikp_log
  FOR ikp_log
/

-- Grants for Table
GRANT DELETE ON ikp_log TO odb
/
GRANT INSERT ON ikp_log TO odb
/
GRANT SELECT ON ikp_log TO odb
/
GRANT UPDATE ON ikp_log TO odb
/




-- Indexes for IKP_LOG

CREATE UNIQUE INDEX i_u_ikp_log ON ikp_log
  (
    sessid                          ASC,
    strno                           ASC
  )
NOPARALLEL
LOGGING
/



-- Comments for IKP_LOG

COMMENT ON TABLE ikp_log IS 'Журнальная таблица для модуля "Автовыполнение заданий" Инверсия-Кавказ'
/
COMMENT ON COLUMN ikp_log.dt IS 'Дата/время события'
/
COMMENT ON COLUMN ikp_log.logname IS 'LOGIN пользователя'
/
COMMENT ON COLUMN ikp_log.msgtxt IS 'Текст события'
/
COMMENT ON COLUMN ikp_log.sessid IS 'ID сессии'
/
COMMENT ON COLUMN ikp_log.strno IS 'Номер строки'
/
COMMENT ON COLUMN ikp_log.strtype IS 'Тип события'
/

-- End of DDL Script for Table XXI.IKP_LOG

