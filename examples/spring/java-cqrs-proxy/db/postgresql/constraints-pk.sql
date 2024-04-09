--;
--table name: eportal.account_check_point;
--;
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT
    ADD CONSTRAINT NULL PRIMARY KEY (ID);
--;
--table name: eportal.account_check_point_rev;
--;
ALTER TABLE EPORTAL.ACCOUNT_CHECK_POINT_REV
    ADD CONSTRAINT NULL PRIMARY KEY (ID);
--;
--table name: eportal.account_movement;
--;
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT
    ADD CONSTRAINT NULL PRIMARY KEY (ID);
--;
--table name: eportal.account_movement_state;
--;
ALTER TABLE EPORTAL.ACCOUNT_MOVEMENT_STATE
    ADD CONSTRAINT NULL PRIMARY KEY (ID);
--;
--table name: eportal.account_target;
--;
ALTER TABLE EPORTAL.ACCOUNT_TARGET
    ADD CONSTRAINT NULL PRIMARY KEY (ID);
