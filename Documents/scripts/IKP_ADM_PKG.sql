-- Start of DDL Script for Package XXI.IKP_ADM
-- Generated 23.11.2020 9:41:21 from XXI@odb604

-- Drop the old instance of IKP_ADM
DROP PACKAGE ikp_adm
/

CREATE OR REPLACE 
PACKAGE ikp_adm
  IS

   type t_param IS record
   (
     n_value number,
     d_value date,
     c_value varchar2(32000)
   );

   type tb_params is table of t_param INDEX BY VARCHAR2(200);

   type t_event IS record
   (
     eventID NUMBER,
     FileName VARCHAR2(2000),
     ErrMsg VARCHAR2(2000),
     params tb_params
   );

   type tb_events is table of t_event;
   vEvents tb_events;

   curEvent number;
   prevEvent number;
   curTask number;

   PROCEDURE initTask(taskID NUMBER);
   PROCEDURE runTask(taskID NUMBER);
   PROCEDURE finishTask(taskID NUMBER);

   FUNCTION init_event (NPP NUMBER) RETURN number;
   PROCEDURE SetEventFileName (NPP NUMBER, FN VARCHAR2);
   FUNCTION GetEventFileName (NPP NUMBER) RETURN VARCHAR2;
   FUNCTION GetEventErrM (NPP NUMBER) RETURN VARCHAR2;
   FUNCTION ExecEvent (NPP NUMBER) RETURN NUMBER;

   FUNCTION get_event_param_c( npp number, p_param_name in varchar2 ) return varchar2;
   FUNCTION get_event_param_n( npp number, p_param_name in varchar2 ) return number;
   FUNCTION get_event_param_d( npp number, p_param_name in varchar2 ) return date;

   PROCEDURE set_event_param( npp number, p_param_name in varchar2, dval in date );
   PROCEDURE set_event_param( npp number, p_param_name in varchar2, nval in number );
   PROCEDURE set_event_param( npp number, p_param_name in varchar2, cval in varchar2 );

END;
/

-- Grants for Package
GRANT EXECUTE ON ikp_adm TO odb
/

CREATE OR REPLACE 
PACKAGE BODY ikp_adm
IS

   PROCEDURE initTask(taskID NUMBER) IS
     ev_cnt number;
   BEGIN
     if vEvents is not null then
       vEvents.delete;
     end if;
     vEvents := tb_events();
     select max(iEventNPP) into ev_cnt from ikp_task_events where iEventTaskID=taskID;
     if ev_cnt is not null then
       vEvents.extend(ev_cnt);
     end if;
     curEvent := -1;
     prevEvent := -1;
     curTask := taskID;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlINFO, 'Начинаем работу по заданию '||taskID);
   exception when others then
     NULL;
   END;

   PROCEDURE runTask(taskID NUMBER) IS
   PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
     update IKP_TASKS set BTASKRUNNING=1 where iTaskID=taskID;
     commit;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlINFO, 'Запускаем задание '||taskID);
   exception when others then
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlERROR, 'Ошибка установки флага выполнения задания '||taskID);
   END;

   PROCEDURE finishTask(taskID NUMBER) IS
   BEGIN
     update IKP_TASKS set BTASKRUNNING=0 where iTaskID=taskID;
     commit;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlINFO, 'Завершаем задание '||taskID);
   exception when others then
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlERROR, 'Ошибка установки флага завершения задания '||taskID);
   END;

   FUNCTION init_event( NPP NUMBER ) RETURN number IS
     P_ID NUMBER := -1;
   BEGIN
     select iEventID into P_ID from ikp_task_events where iEventTaskID=curTask and IEVENTNPP=NPP;
     delete from IKP_TMP_FILE;
     vEvents(NPP).eventID:=P_ID;
     vEvents(NPP).ErrMsg:=NULL;
     prevEvent:=curEvent;
     curEvent:=P_ID;
     RETURN 0;
   EXCEPTION WHEN OTHERS THEN
     vEvents(NPP).eventID:=P_ID;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlERROR, 'Ошибка получения ID действия для действия №'||NPP||': '||SQLERRM);
     vEvents(NPP).ErrMsg:=SQLERRM;
     RETURN -1;
   END;

   PROCEDURE SetEventFileName (NPP NUMBER, FN VARCHAR2) IS
     i_npp number;
   BEGIN
     if NPP<=0 then i_npp:=curEvent; else i_npp:=NPP; end if;
     vEvents(i_NPP).FileName:=FN;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlINFO, 'Работаем с файлом '||FN);
   EXCEPTION WHEN OTHERS THEN
     vEvents(NPP).ErrMsg:=SQLERRM;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlERROR, 'Ошибка установки имени файла '||FN||' для действия '||curEvent||': '||SQLERRM);
   END;

   FUNCTION GetEventFileName (NPP NUMBER) RETURN VARCHAR2 IS
   BEGIN
     RETURN vEvents(NPP).FileName;
   EXCEPTION WHEN OTHERS THEN
     RETURN NULL;
   END;

   FUNCTION GetEventErrM (NPP NUMBER) RETURN VARCHAR2 IS
   BEGIN
     RETURN vEvents(NPP).ErrMsg;
   EXCEPTION WHEN OTHERS THEN
     RETURN NULL;
   END;

   FUNCTION ExecEvent (NPP NUMBER) RETURN NUMBER IS
     r IKP_TASK_EVENTS%rowtype;
     SQLtext VARCHAR2(32000);
   BEGIN
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlINFO, 'Запуск действия '||curEvent);
     select * into r from IKP_TASK_EVENTS WHERE iEventID=vEvents(NPP).eventID;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlINFO, 'Действие ['||r.CEVENTNAME||']');
     if r.iEventType=1 then
       SQLtext:=r.lEventText;
     elsif r.iEventType=2 then
       select cPresetText into SQLtext from IKP_EVENT_PRESETS where iPresetID=r.iEventPresetID;
     else
       SQLtext:=NULL;
     end if;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlALL, SQLtext);
     if SQLtext is not null then
       SQLtext:='BEGIN'||CHR(10)||SQLtext||CHR(10)||'END;';
       EXECUTE IMMEDIATE SQLtext;
       vEvents(NPP).ErrMsg:='Ok';
     else
       vEvents(NPP).ErrMsg:='Non-DB event!';
     end if;
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlINFO, vEvents(NPP).ErrMsg);
     RETURN 0;
   EXCEPTION WHEN OTHERS THEN
     IKP_DEBUG.LogIt(IKP_DEBUG.lvlERROR, SQLERRM);
     vEvents(NPP).ErrMsg:=SQLERRM;
     RETURN -1;
   END;

   FUNCTION get_event_param_d( npp number, p_param_name in varchar2 ) return date IS
     dd date;
   BEGIN
     dd:=vEvents(npp).params(p_param_name).d_value;
     return dd;
   exception when others then return null;
   END;

   FUNCTION get_event_param_n( npp number, p_param_name in varchar2 ) return number IS
     nn number;
   BEGIN
     nn:=vEvents(npp).params(p_param_name).n_value;
     return nn;
   exception when others then return null;
   END;

   FUNCTION get_event_param_c( npp number, p_param_name in varchar2 ) return varchar2 IS
     cc varchar2(4000);
   BEGIN
     cc:=vEvents(npp).params(p_param_name).c_value;
     return cc;
   exception when others then return null;
   END;

   PROCEDURE set_event_param( npp number, p_param_name in varchar2, dval in date ) IS
   BEGIN
     vEvents(npp).params(p_param_name).d_value:=dval;
   END;

   PROCEDURE set_event_param( npp number, p_param_name in varchar2, nval in number ) IS
   BEGIN
     vEvents(npp).params(p_param_name).n_value:=nval;
   END;

   PROCEDURE set_event_param( npp number, p_param_name in varchar2, cval in varchar2 ) IS
   BEGIN
     vEvents(npp).params(p_param_name).c_value:=cval;
   END;

END;
/

CREATE PUBLIC SYNONYM IKP_ADM FOR IKP_ADM
/

-- End of DDL Script for Package XXI.IKP_ADM

