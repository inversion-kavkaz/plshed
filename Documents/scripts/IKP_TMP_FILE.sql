-- Start of DDL Script for Table XXI.IKP_TMP_FILE
-- Generated 16.11.2020 10:26:03 from XXI@odb604

CREATE GLOBAL TEMPORARY TABLE ikp_tmp_file
    (lfile                          BLOB)
ON COMMIT PRESERVE ROWS
  SEGMENT CREATION IMMEDIATE
  NOPARALLEL
/

-- Create synonym IKP_TMP_FILE
CREATE PUBLIC SYNONYM ikp_tmp_file
  FOR ikp_tmp_file
/

-- Grants for Table
GRANT DELETE ON ikp_tmp_file TO odb
/
GRANT INSERT ON ikp_tmp_file TO odb
/
GRANT SELECT ON ikp_tmp_file TO odb
/
GRANT UPDATE ON ikp_tmp_file TO odb
/




-- End of DDL Script for Table XXI.IKP_TMP_FILE

