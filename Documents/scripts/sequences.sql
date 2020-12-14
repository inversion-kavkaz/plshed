-- Start of DDL Script for Sequence XXI.S_IKP_EVENT_PRESETS
-- Generated 16.11.2020 10:26:51 from XXI@odb604

-- Drop the old instance of S_IKP_EVENT_PRESETS
DROP SEQUENCE s_ikp_event_presets
/

CREATE SEQUENCE s_ikp_event_presets
  INCREMENT BY 1
  START WITH 1
  MINVALUE 1
  MAXVALUE 9999999999999999999999999999
  NOCYCLE
  ORDER
  CACHE 10
/


-- End of DDL Script for Sequence XXI.S_IKP_EVENT_PRESETS

-- Start of DDL Script for Sequence XXI.S_IKP_TASK_EVENTS
-- Generated 16.11.2020 10:26:51 from XXI@odb604

-- Drop the old instance of S_IKP_TASK_EVENTS
DROP SEQUENCE s_ikp_task_events
/

CREATE SEQUENCE s_ikp_task_events
  INCREMENT BY 1
  START WITH 1
  MINVALUE 1
  MAXVALUE 9999999999999999999999999999
  NOCYCLE
  ORDER
  CACHE 10
/

-- Grants for Sequence
GRANT ALTER ON s_ikp_task_events TO odb
/
GRANT SELECT ON s_ikp_task_events TO odb
/

-- End of DDL Script for Sequence XXI.S_IKP_TASK_EVENTS

-- Start of DDL Script for Sequence XXI.S_IKP_TASKS
-- Generated 16.11.2020 10:26:51 from XXI@odb604

-- Drop the old instance of S_IKP_TASKS
DROP SEQUENCE s_ikp_tasks
/

CREATE SEQUENCE s_ikp_tasks
  INCREMENT BY 1
  START WITH 1
  MINVALUE 1
  MAXVALUE 9999999999999999999999999999
  NOCYCLE
  ORDER
  CACHE 10
/

-- Grants for Sequence
GRANT ALTER ON s_ikp_tasks TO odb
/
GRANT SELECT ON s_ikp_tasks TO odb
/

-- End of DDL Script for Sequence XXI.S_IKP_TASKS

