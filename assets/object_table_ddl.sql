CREATE OR REPLACE TYPE xxmaf_ofa_count AS OBJECT
(
   count_type VARCHAR2 (10),
   count_name VARCHAR2 (30),
   count_value NUMBER
);

drop type xxmaf_ofa_count_tbl force;

CREATE OR REPLACE TYPE xxmaf_ofa_count_tbl AS TABLE OF xxmaf_ofa_count;


----
DROP TYPE XXMAF_OFA_L2_OBJ;

CREATE OR REPLACE TYPE xxmaf_ofa_l2_obj AS OBJECT
(
count_aging_flag VARCHAR2(5),
so_rcpt_flag VARCHAR2(5),
customer_name VARCHAR2(240),
customer_id NUMBER,
count NUMBER
);
/


CREATE OR REPLACE TYPE XXMAF_OFA_L2_TBL AS TABLE OF xxmaf_ofa_l2_obj;
/