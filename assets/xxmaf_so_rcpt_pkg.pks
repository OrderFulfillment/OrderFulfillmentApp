create or replace PACKAGE xxmaf_so_rcpt_pkg
   AUTHID CURRENT_USER
IS
   --+====================================================================================|
   --| Module/Program Name:
   --| Filename     :
   --| Author                : Deloitte Consulting LLP
   --| Source                : EBS
   --| Destination           : Mobile Application
   --|
   --| Purpose               : Package to Exposed to Webservices MAF.
   --|
   --| Calls/Called by       :
   --|
   --| Parameters            :
   --|
   --| Constraints           :
   --|
   --+====================================================================================|
   --| Creation and Modification History
   --+====================================================================================|
   --| Date             Who                             Ver           Change Description
   --| -----------      -------------                  --------        --------------------------|
   --| 19-July-2016      Deloitte Consulting LLP       1.0           Initial
   --+====================================================================================|
   g_order_count             NUMBER;
   g_receipt_count           NUMBER;

   g_so_type                 VARCHAR2 (3) := 'S';        --Denotes Sales Order
   g_rcpt_type               VARCHAR2 (3) := 'R';            --Denotes Receipt

   g_hold_status             VARCHAR2 (3) := 'H';        --Denotes Hold Orders
   g_backorder_status        VARCHAR2 (3) := 'B';  --Denotes backorderd Orders
   g_jeopardy_order_status   VARCHAR2 (3) := 'J';    --Denotes Jeopardy Orders
   g_past_due_status         VARCHAR2 (3) := 'P';      --Denotes Past Due Date

   g_count                   VARCHAR2 (3) := 'C';
   g_aging                   VARCHAR2 (3) := 'A';

   g_header                  VARCHAR2 (3) := 'H';
   g_line                    VARCHAR2 (3) := 'L';

   --Level 1 Information--
   PROCEDURE get_so_rct_details_l1 (px_counts OUT xxmaf_ofa_count_tbl);

   --Level 2 Information--
   PROCEDURE get_so_rct_details_l2 (
      PI_STATUS          IN     VARCHAR2,         --<-- Passed from Middleware
      PI_SO_OR_RCT       IN     VARCHAR2, --<-- Passed from Middleware O - Sales Order, R- Receipt
      PI_MIN_AGING_VAL   IN     NUMBER,
      PI_MAX_AGING_VAL   IN     NUMBER,
      px_cust_count         OUT xxmaf_ofa_l2_tbl);

   --Level 3 Information--
   PROCEDURE get_so_rct_header_details_l3 (
      pi_status          IN     VARCHAR2,         --<-- Passed from Middleware
      pi_customer_id     IN     NUMBER,           --<-- Passed from Middleware
      pi_so_or_rct       IN     VARCHAR2, --<-- Passed from Middleware O - Sales Order, R- Receipt
      pi_aging_count     IN     VARCHAR2, --<--Passed A from Aging Page C from Counts page
      pi_min_aging_val   IN     NUMBER,
      pi_max_aging_val   IN     NUMBER,
      px_header_type        OUT xxmaf_ofa_header_tbl);

   --Level 4 Information--
   PROCEDURE get_so_rct_line_details_l4 (
      pi_header_id       IN     NUMBER,           --<-- Passed from Middleware
      pi_status          IN     VARCHAR2,
      pi_so_or_rct       IN     VARCHAR2, --<-- Passed from Middleware O - Sales Order, R- Receipt
      pi_aging_count     IN     VARCHAR2,
      pi_min_aging_val   IN     NUMBER,
      pi_max_aging_val   IN     NUMBER,
      px_line_type          OUT xxmaf_ofa_line_tbl);

   FUNCTION is_in_aging_limit (pi_count_aging     IN VARCHAR2,
                               pi_min_aging_val   IN NUMBER,
                               pi_max_aging_val   IN NUMBER,
                               pi_document_type   IN VARCHAR2, -- Sales Order or Receipt
                               pi_status          IN VARCHAR2,
                               pi_entity          IN VARCHAR2, --H - Headers L - Lines
                               pi_entity_id       IN NUMBER)
      RETURN VARCHAR2;
END xxmaf_so_rcpt_pkg;