-- Run this script as a user having the DBA role --


CREATE USER DEMO_SRC IDENTIFIED BY DEMO_SRC;
GRANT CONNECT  TO DEMO_SRC;
GRANT RESOURCE TO DEMO_SRC;



CREATE USER DEMO_TRG IDENTIFIED BY DEMO_TRG;
GRANT CONNECT  TO DEMO_TRG;
GRANT RESOURCE TO DEMO_TRG;
