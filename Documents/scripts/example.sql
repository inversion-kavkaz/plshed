declare
  fn varchar2(2000);
  res number;
begin
  -- инициализация задания целиком
  ikp_adm.initTask(:tID);
  -- взводим флаг что задание запущено
  ikp_adm.runTask(:tID);
  -- идем по всем действиям в задании
  for i in (select * from IKP_TASK_EVENTS where iEventTaskid=:tID order by iEventNPP) loop
    -- инициализация действия
    res:=ikp_adm.init_event(i.iEventNPP);
    -- если загрузка
    if i.IEVENTFILEDIR=0 then
      -- грузим файл
      -- load fn into blob - таблица IKP_TMP_FILE поле lFile
      fn:=i.CEVENTINDIR||'\'||'test_file.dbf'; -- берем вход.дир-ю и найденный файл
      -- даем имя файла в пакет
      IKP_ADM.SetEventFileName(i.iEventNPP,fn);
    end if;
    -- выполняем действие
    res:=ikp_adm.execevent(i.iEventNPP);
    -- если выгрузка
    if i.IEVENTFILEDIR=0 then
      fn:=i.CEVENTOUTDIR||'\'||IKP_ADM.GetEventFileName(i.iEventNPP); -- берем исход.дир-ю и имя файла из пакета
      -- выгружаем файл
      -- save blob to fn - таблица IKP_TMP_FILE поле lFile
    end if;
  end loop;
  ikp_adm.finishTask(:tID);
end;