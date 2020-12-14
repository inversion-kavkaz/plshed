-- Start of DDL Script for Package XXI.IKP_DEBUG
-- Generated 23.11.2020 9:42:09 from XXI@odb604

-- Drop the old instance of IKP_DEBUG
DROP PACKAGE ikp_debug
/

CREATE OR REPLACE 
PACKAGE ikp_debug
  IS

  type str_array is array(4) of varchar2(5);

  lvlALL number(1,0) := 1;
  lvlDEBUG number(1,0) := 2;
  lvlINFO number(1,0) := 3;
  lvlERROR number(1,0) := 4;

  lvls CONSTANT str_array := str_array('ALL','DEBUG','INFO','ERROR');

  curSession CONSTANT INTEGER := USERENV ('SESSIONID');
  curLvl NUMBER(1,0) := 1; -- по умолчанию - ALL (если не установлено через модуль)
  curDir NUMBER(1,0) := 0; -- 0 в таблицу, 1 в DBMS, 2 оба

  PROCEDURE LogIt (lvl number, msg varchar2);

  PROCEDURE SetLvl (lvl number);

END; -- Package spec
/

-- Grants for Package
GRANT EXECUTE ON ikp_debug TO odb
/

CREATE OR REPLACE 
PACKAGE BODY ikp_debug
IS

PROCEDURE LogIt (lvl number, msg varchar2) IS
PRAGMA AUTONOMOUS_TRANSACTION;
  npp number;
BEGIN
  if nvl(lvl,curLvl)>=curLvl then
    select nvl(max(strno),0)+1 into npp from ikp_log where sessid=curSession;
    insert into ikp_log(sessid, strno, strtype, msgtxt, dt, logname) values (curSession, npp, lvls(lvl), substr(msg,1,4000), sysdate, user);
    commit;
  end if;
END;

PROCEDURE SetLvl (lvl number) IS
BEGIN
  curLvl:=nvl(lvl,3);
END;

END;
/

CREATE PUBLIC SYNONYM IKP_DEBUG FOR IKP_DEBUG
/

-- End of DDL Script for Package XXI.IKP_DEBUG

