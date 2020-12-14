declare
  fn varchar2(2000);
  res number;
begin
  -- ������������� ������� �������
  ikp_adm.initTask(:tID);
  -- ������� ���� ��� ������� ��������
  ikp_adm.runTask(:tID);
  -- ���� �� ���� ��������� � �������
  for i in (select * from IKP_TASK_EVENTS where iEventTaskid=:tID order by iEventNPP) loop
    -- ������������� ��������
    res:=ikp_adm.init_event(i.iEventNPP);
    -- ���� ��������
    if i.IEVENTFILEDIR=0 then
      -- ������ ����
      -- load fn into blob - ������� IKP_TMP_FILE ���� lFile
      fn:=i.CEVENTINDIR||'\'||'test_file.dbf'; -- ����� ����.���-� � ��������� ����
      -- ���� ��� ����� � �����
      IKP_ADM.SetEventFileName(i.iEventNPP,fn);
    end if;
    -- ��������� ��������
    res:=ikp_adm.execevent(i.iEventNPP);
    -- ���� ��������
    if i.IEVENTFILEDIR=0 then
      fn:=i.CEVENTOUTDIR||'\'||IKP_ADM.GetEventFileName(i.iEventNPP); -- ����� �����.���-� � ��� ����� �� ������
      -- ��������� ����
      -- save blob to fn - ������� IKP_TMP_FILE ���� lFile
    end if;
  end loop;
  ikp_adm.finishTask(:tID);
end;