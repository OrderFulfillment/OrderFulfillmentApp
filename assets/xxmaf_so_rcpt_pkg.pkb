create or replace PACKAGE BODY xxmaf_so_rcpt_pkg
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


   --l1 : Level 1 Procedure Call := Only Counts of statuses would be sent. Input :- None
   --l2 : Level 2 Procedure Call := Counts of a particular status grouped by Customer would be sent. Input :- Name of the Status
   --l3 : Level 3 Procedure Call := Header information(SO/RCPT) would be displayed for a particular status + customer combo. Input :- Customer Name,Status
   --l4 : Level 4 Procedure Call := Line Information for a particular Header(SO/RCPT) would be displayed.Input SO Number/Recipt Number



   --l1 denotes Level 1 Information--
   PROCEDURE get_so_rct_details_l1 (px_counts OUT xxmaf_ofa_count_tbl)
   IS
      ln_so_hold_count           NUMBER DEFAULT 0;
      ln_so_backorder_count      NUMBER DEFAULT 0;
      ln_jeopardy_count          NUMBER DEFAULT 0;
      ln_so_past_due_count       NUMBER DEFAULT 0;
      ln_header_hold_count       NUMBER DEFAULT 0;
      ln_line_hold_count         NUMBER DEFAULT 0;
      ln_receip_past_due_count   NUMBER DEFAULT 0;


      lv_status                  VARCHAR2 (3);
      lv_error_message           VARCHAR2 (3000);
   BEGIN
      ----Sales Order Level 1 Information
      BEGIN
         SELECT COUNT (1)
           INTO ln_header_hold_count
           FROM oe_order_holds_all ooha,
                oe_hold_sources_all ohsa,
                oe_hold_definitions ohd,
                oe_order_headers_all ooh
          WHERE     1 = 1
                AND ooha.header_id = ooh.header_id
                AND ooha.hold_source_id = ohsa.hold_source_id
                AND ohsa.hold_id = ohd.hold_id
                AND ooha.released_flag = 'N';
      EXCEPTION
         WHEN OTHERS
         THEN
            ln_header_hold_count := 0;
      END;

      BEGIN
         SELECT COUNT (1)
           INTO ln_so_past_due_count
           FROM oe_order_headers_all ooh
          WHERE     1 = 1
                AND ooh.booked_flag = 'Y'
                AND EXISTS
                       (SELECT 1
                          FROM oe_order_lines_all ool
                         WHERE     ool.header_id = ooh.header_id
                               AND ool.schedule_ship_date IS NOT NULL
                               AND ool.schedule_ship_date < SYSDATE);
      EXCEPTION
         WHEN OTHERS
         THEN
            ln_so_past_due_count := 0;
      END;

      BEGIN
         SELECT COUNT (1)
           INTO ln_line_hold_count
           FROM oe_order_holds_all ooha,
                oe_hold_sources_all ohsa,
                oe_hold_definitions ohd,
                oe_order_headers_all ooh
          WHERE     1 = 1
                AND ooha.header_id = ooha.header_id
                AND ooha.header_id = ooh.header_id
                AND ooha.hold_source_id = ohsa.hold_source_id
                AND ohsa.hold_id = ohd.hold_id
                AND EXISTS
                       (SELECT 1
                          FROM oe_order_lines_all ool
                         WHERE     ool.header_id = ooh.header_id
                               AND ooha.line_id = ool.line_id
                               AND ooha.released_flag = 'N');
      EXCEPTION
         WHEN OTHERS
         THEN
            ln_line_hold_count := 0;
      END;

      ln_so_hold_count := ln_line_hold_count + ln_header_hold_count;

      BEGIN
         SELECT COUNT (1)
           INTO ln_so_backorder_count
           FROM oe_order_headers_all ooh
          WHERE     1 = 1
                AND ooh.booked_flag = 'Y'
                AND EXISTS
                       (SELECT 1
                          FROM oe_order_lines_all ool,
                               wsh_delivery_details wdd
                         WHERE     ool.header_id = ooh.header_id
                               AND wdd.source_line_id = ool.line_id
                               AND wdd.released_status = 'B'     --Backordered
                                                            );
      EXCEPTION
         WHEN OTHERS
         THEN
            ln_so_backorder_count := 0;
      END;

      BEGIN
         SELECT COUNT (1)
           INTO ln_jeopardy_count
           FROM oe_order_headers_all ooh
          WHERE     1 = 1
                AND ooh.booked_flag = 'Y'
                AND EXISTS
                       (SELECT 1
                          FROM oe_order_lines_all ool
                         WHERE     ooh.header_id = ool.header_id
                               AND ool.schedule_status_code IS NULL
                               AND (SYSDATE - ool.creation_date) > 1);
      EXCEPTION
         WHEN OTHERS
         THEN
            ln_jeopardy_count := 0;
      END;

      --Recipt Level 1 Information ---
      BEGIN
         SELECT COUNT (1)
           INTO ln_receip_past_due_count
           FROM rcv_shipment_headers rsh, rcv_shipment_lines rsl
          WHERE     1 = 1
                AND rsh.Shipment_Header_ID = rsl.Shipment_Header_ID
                AND rsh.expected_receipt_date <= SYSDATE
                AND rsl.shipment_line_status_code = 'EXPECTED';
      EXCEPTION
         WHEN OTHERS
         THEN
            ln_receip_past_due_count := 0;
      END;

      px_counts := xxmaf_ofa_count_tbl ();
      px_counts.EXTEND (5);
      --Below are the Level 1 OUT Variables that would be exposed to Middleware--
      px_counts (1) := xxmaf_ofa_count ('SO', 'On Hold', ln_so_hold_count);
      px_counts (2) :=
         xxmaf_ofa_count ('SO', 'Back Ordered Count', ln_so_backorder_count);
      px_counts (3) :=
         xxmaf_ofa_count ('SO', 'In Jeopardy', ln_jeopardy_count);
      px_counts (4) :=
         xxmaf_ofa_count ('SO', 'Past Due', ln_so_past_due_count);
      px_counts (5) :=
         xxmaf_ofa_count ('RPT', 'Past Due', ln_receip_past_due_count);
   EXCEPTION
      WHEN OTHERS
      THEN
         lv_status := 'E';
         lv_error_message := SQLERRM || DBMS_UTILITY.format_error_backtrace;
   END get_so_rct_details_l1;

   --Level 2 Information--
   PROCEDURE get_so_rct_details_l2 (
      pi_status          IN     VARCHAR2,         --<-- Passed from Middleware
      pi_so_or_rct       IN     VARCHAR2, --<-- Passed from Middleware O - Sales Order, R- Receipt
      pi_min_aging_val   IN     NUMBER,
      pi_max_aging_val   IN     NUMBER,
      px_cust_count         OUT xxmaf_ofa_l2_tbl)
   IS
      CURSOR cur_so_hold_cust_details
      IS
         SELECT *
           FROM (SELECT *
                   FROM (  SELECT COUNT (1) COUNT,
                                  hp.party_name customer,
                                  hp.party_id customer_id,
                                  'C' count_aging_flag,
                                  'S' so_rcpt_flag
                             FROM oe_order_holds_all ooha,
                                  oe_hold_sources_all ohsa,
                                  oe_hold_definitions ohd,
                                  oe_order_headers_all ooh,
                                  hz_cust_site_uses_all hcsua, -- uses of customer addresses
                                  hz_cust_acct_sites_all hcasa, -- customer addresses
                                  hz_cust_accounts hca,   -- customer accounts
                                  hz_parties hp                     -- parties
                            WHERE     1 = 1
                                  AND ooha.header_id = ooh.header_id
                                  AND ooha.hold_source_id = ohsa.hold_source_id
                                  AND ohsa.hold_id = ohd.hold_id
                                  AND ooha.released_flag = 'N'
                                  AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                  AND hcsua.cust_acct_site_id =
                                         hcasa.cust_acct_site_id
                                  AND hcasa.cust_account_id =
                                         hca.cust_account_id
                                  AND hca.party_id = hp.party_id
                         GROUP BY hp.party_name,
                                  hp.party_id,
                                  'C',
                                  'S'
                           HAVING COUNT (1) < 6)
                 UNION
                 SELECT *
                   FROM (  SELECT COUNT (1),
                                  hp.party_name customer,
                                  hp.party_id customer_id,
                                  'C' count_aging_flag,
                                  'S' so_rcpt_flag
                             FROM oe_order_holds_all ooha,
                                  oe_hold_sources_all ohsa,
                                  oe_hold_definitions ohd,
                                  oe_order_headers_all ooh,
                                  hz_cust_site_uses_all hcsua, -- uses of customer addresses
                                  hz_cust_acct_sites_all hcasa, -- customer addresses
                                  hz_cust_accounts hca,   -- customer accounts
                                  hz_parties hp                     -- parties
                            WHERE     1 = 1
                                  AND ooha.header_id = ooha.header_id
                                  AND ooha.header_id = ooh.header_id
                                  AND ooha.hold_source_id = ohsa.hold_source_id
                                  AND ohsa.hold_id = ohd.hold_id
                                  AND EXISTS
                                         (SELECT 1
                                            FROM oe_order_lines_all ool
                                           WHERE     ool.header_id =
                                                        ooh.header_id
                                                 AND ooha.line_id = ool.line_id
                                                 AND ooha.released_flag = 'N')
                                  AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                  AND hcsua.cust_acct_site_id =
                                         hcasa.cust_acct_site_id
                                  AND hcasa.cust_account_id =
                                         hca.cust_account_id
                                  AND hca.party_id = hp.party_id
                         GROUP BY hp.party_name,
                                  hp.party_id,
                                  'C',
                                  'S'
                           HAVING COUNT (1) < 6)
                 ORDER BY 1)
          WHERE ROWNUM < 6;

      CURSOR cur_so_hold_cust_aging
      IS
         SELECT *
           FROM (SELECT *
                   FROM (  SELECT COUNT (1) COUNT,
                                  hp.party_name customer,
                                  hp.party_id customer_id,
                                  g_aging count_aging_flag,
                                  'S' so_rcpt_flag
                             FROM oe_order_holds_all ooha,
                                  oe_hold_sources_all ohsa,
                                  oe_hold_definitions ohd,
                                  oe_order_headers_all ooh,
                                  hz_cust_site_uses_all hcsua, -- uses of customer addresses
                                  hz_cust_acct_sites_all hcasa, -- customer addresses
                                  hz_cust_accounts hca,   -- customer accounts
                                  hz_parties hp                     -- parties
                            WHERE     1 = 1
                                  AND ooha.header_id = ooh.header_id
                                  AND ooha.hold_source_id = ohsa.hold_source_id
                                  AND ohsa.hold_id = ohd.hold_id
                                  AND ooha.released_flag = 'N'
                                  AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                  AND hcsua.cust_acct_site_id =
                                         hcasa.cust_acct_site_id
                                  AND hcasa.cust_account_id =
                                         hca.cust_account_id
                                  AND hca.party_id = hp.party_id
                                  AND is_in_aging_limit (g_aging,
                                                         pi_min_aging_val,
                                                         pi_max_aging_val,
                                                         g_so_type,
                                                         g_hold_status,
                                                         g_header,
                                                         ooh.header_id) = 'Y'
                         GROUP BY hp.party_name,
                                  hp.party_id,
                                  'C',
                                  'S'
                           HAVING COUNT (1) < 6)
                 UNION
                 (  SELECT COUNT (1),
                           hp.party_name customer,
                           hp.party_id customer_id,
                           g_aging count_aging_flag,
                           'S' so_rcpt_flag
                      FROM oe_order_holds_all ooha,
                           oe_hold_sources_all ohsa,
                           oe_hold_definitions ohd,
                           oe_order_headers_all ooh,
                           hz_cust_site_uses_all hcsua, -- uses of customer addresses
                           hz_cust_acct_sites_all hcasa, -- customer addresses
                           hz_cust_accounts hca,          -- customer accounts
                           hz_parties hp                            -- parties
                     WHERE     1 = 1
                           AND ooha.header_id = ooha.header_id
                           AND ooha.header_id = ooh.header_id
                           AND ooha.hold_source_id = ohsa.hold_source_id
                           AND ohsa.hold_id = ohd.hold_id
                           AND EXISTS
                                  (SELECT 1
                                     FROM oe_order_lines_all ool
                                    WHERE     ool.header_id = ooh.header_id
                                          AND ooha.line_id = ool.line_id
                                          AND ooha.released_flag = 'N')
                           AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                           AND hcsua.cust_acct_site_id =
                                  hcasa.cust_acct_site_id
                           AND hcasa.cust_account_id = hca.cust_account_id
                           AND hca.party_id = hp.party_id
                           AND is_in_aging_limit (g_aging,
                                                  pi_min_aging_val,
                                                  pi_max_aging_val,
                                                  g_so_type,
                                                  g_hold_status,
                                                  g_header,
                                                  ooh.header_id) = 'Y'
                  GROUP BY hp.party_name,
                           hp.party_id,
                           'C',
                           'S'
                    HAVING COUNT (1) < 6)
                 ORDER BY 1)
          WHERE ROWNUM < 6;

      CURSOR cur_backord_cust
      IS
         SELECT *
           FROM (  SELECT *
                     FROM (  SELECT COUNT (1) COUNT,
                                    hp.party_name customer,
                                    hp.party_id customer_id,
                                    'C' count_aging_flag,
                                    'S' so_rcpt_flag
                               FROM oe_order_headers_all ooh,
                                    hz_cust_site_uses_all hcsua, -- uses of customer addresses
                                    hz_cust_acct_sites_all hcasa, -- customer addresses
                                    hz_cust_accounts hca, -- customer accounts
                                    hz_parties hp                   -- parties
                              WHERE     1 = 1
                                    AND ooh.booked_flag = 'Y'
                                    AND EXISTS
                                           (SELECT 1
                                              FROM oe_order_lines_all ool,
                                                   wsh_delivery_details wdd
                                             WHERE     ool.header_id =
                                                          ooh.header_id
                                                   AND wdd.source_line_id =
                                                          ool.line_id
                                                   AND wdd.released_status = 'B' --Backordered
                                                                                )
                                    AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                    AND hcsua.cust_acct_site_id =
                                           hcasa.cust_acct_site_id
                                    AND hcasa.cust_account_id =
                                           hca.cust_account_id
                                    AND hca.party_id = hp.party_id
                           GROUP BY hp.party_name,
                                    hp.party_id,
                                    'C',
                                    'S'
                             HAVING COUNT (1) < 6)
                 ORDER BY 1)
          WHERE ROWNUM < 6;

      CURSOR cur_backord_cust_aging
      IS
         SELECT *
           FROM (  SELECT *
                     FROM (  SELECT COUNT (1) COUNT,
                                    hp.party_name customer,
                                    hp.party_id customer_id,
                                    g_aging count_aging_flag,
                                    g_so_type so_rcpt_flag
                               FROM oe_order_headers_all ooh,
                                    hz_cust_site_uses_all hcsua, -- uses of customer addresses
                                    hz_cust_acct_sites_all hcasa, -- customer addresses
                                    hz_cust_accounts hca, -- customer accounts
                                    hz_parties hp                   -- parties
                              WHERE     1 = 1
                                    AND ooh.booked_flag = 'Y'
                                    AND is_in_aging_limit (g_aging,
                                                           pi_min_aging_val,
                                                           pi_max_aging_val,
                                                           g_so_type,
                                                           g_backorder_status,
                                                           g_header,
                                                           ooh.header_id) = 'Y'
                                    AND EXISTS
                                           (SELECT 1
                                              FROM oe_order_lines_all ool,
                                                   wsh_delivery_details wdd
                                             WHERE     ool.header_id =
                                                          ooh.header_id
                                                   AND wdd.source_line_id =
                                                          ool.line_id
                                                   AND wdd.released_status = 'B' --Backordered
                                                                                )
                                    AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                    AND hcsua.cust_acct_site_id =
                                           hcasa.cust_acct_site_id
                                    AND hcasa.cust_account_id =
                                           hca.cust_account_id
                                    AND hca.party_id = hp.party_id
                           GROUP BY hp.party_name,
                                    hp.party_id,
                                    'C',
                                    'S'
                             HAVING COUNT (1) < 6)
                 ORDER BY 1)
          WHERE ROWNUM < 6;

      CURSOR cur_injeopardy_cust
      IS
         SELECT *
           FROM (  SELECT *
                     FROM (  SELECT COUNT (1) COUNT,
                                    hp.party_name customer,
                                    hp.party_id customer_id,
                                    'C' count_aging_flag,
                                    'S' so_rcpt_flag
                               FROM oe_order_headers_all ooh,
                                    hz_cust_site_uses_all hcsua, -- uses of customer addresses
                                    hz_cust_acct_sites_all hcasa, -- customer addresses
                                    hz_cust_accounts hca, -- customer accounts
                                    hz_parties hp                   -- parties
                              WHERE     1 = 1
                                    AND ooh.booked_flag = 'Y'
                                    AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                    AND hcsua.cust_acct_site_id =
                                           hcasa.cust_acct_site_id
                                    AND hcasa.cust_account_id =
                                           hca.cust_account_id
                                    AND hca.party_id = hp.party_id
                                    AND ooh.creation_date < SYSDATE
                                    AND EXISTS
                                           (SELECT 1
                                              FROM oe_order_lines_all ool
                                             WHERE     ooh.header_id =
                                                          ool.header_id
                                                   AND ool.schedule_status_code
                                                          IS NULL
                                                   AND (  SYSDATE
                                                        - ool.creation_date) > 1)
                           GROUP BY hp.party_name,
                                    hp.party_id,
                                    'C',
                                    'S'
                             HAVING COUNT (1) < 6)
                 ORDER BY 1)
          WHERE ROWNUM < 6;

      CURSOR cur_injeopardy_cust_aging
      IS
         SELECT *
           FROM (  SELECT *
                     FROM (  SELECT COUNT (1) COUNT,
                                    hp.party_name customer,
                                    hp.party_id customer_id,
                                    g_aging count_aging_flag,
                                    'S' so_rcpt_flag
                               FROM oe_order_headers_all ooh,
                                    hz_cust_site_uses_all hcsua, -- uses of customer addresses
                                    hz_cust_acct_sites_all hcasa, -- customer addresses
                                    hz_cust_accounts hca, -- customer accounts
                                    hz_parties hp                   -- parties
                              WHERE     1 = 1
                                    AND ooh.booked_flag = 'Y'
                                    AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                    AND hcsua.cust_acct_site_id =
                                           hcasa.cust_acct_site_id
                                    AND hcasa.cust_account_id =
                                           hca.cust_account_id
                                    AND hca.party_id = hp.party_id
                                    AND is_in_aging_limit (
                                           g_aging,
                                           pi_min_aging_val,
                                           pi_max_aging_val,
                                           g_so_type,
                                           g_jeopardy_order_status,
                                           g_header,
                                           ooh.header_id) = 'Y'
                                    AND EXISTS
                                           (SELECT 1
                                              FROM oe_order_lines_all ool
                                             WHERE     ooh.header_id =
                                                          ool.header_id
                                                   AND ool.schedule_status_code
                                                          IS NULL
                                                   AND (  SYSDATE
                                                        - ool.creation_date) > 1)
                           GROUP BY hp.party_name,
                                    hp.party_id,
                                    'A',
                                    'S'
                             HAVING COUNT (1) < 6)
                 ORDER BY 1)
          WHERE ROWNUM < 6;

      CURSOR cur_past_due_cust
      IS
         SELECT *
           FROM (  SELECT *
                     FROM (  SELECT COUNT (1) COUNT,
                                    hp.party_name customer,
                                    hp.party_id customer_id,
                                    'C' count_aging_flag,
                                    'S' so_rcpt_flag
                               FROM oe_order_headers_all ooh,
                                    hz_cust_site_uses_all hcsua, -- uses of -- customer addresses
                                    hz_cust_acct_sites_all hcasa, -- customer addresses
                                    hz_cust_accounts hca, -- customer accounts
                                    hz_parties hp                   -- parties
                              WHERE     1 = 1
                                    AND ooh.booked_flag = 'Y'
                                    AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                    AND hcsua.cust_acct_site_id =
                                           hcasa.cust_acct_site_id
                                    AND hcasa.cust_account_id =
                                           hca.cust_account_id
                                    AND hca.party_id = hp.party_id
                                    AND EXISTS
                                           (SELECT 1
                                              FROM oe_order_lines_all ool
                                             WHERE     ool.header_id =
                                                          ooh.header_id
                                                   AND ool.schedule_ship_date
                                                          IS NOT NULL
                                                   AND ool.schedule_ship_date <
                                                          SYSDATE)
                           GROUP BY hp.party_name,
                                    hp.party_id,
                                    'C',
                                    'S'
                             HAVING COUNT (1) < 6)
                 ORDER BY 1)
          WHERE ROWNUM < 6;

      CURSOR cur_past_due_cust_aging
      IS
         SELECT *
           FROM (  SELECT *
                     FROM (  SELECT COUNT (1) COUNT,
                                    hp.party_name customer,
                                    hp.party_id customer_id,
                                    'A' count_aging_flag,
                                    'S' so_rcpt_flag
                               FROM oe_order_headers_all ooh,
                                    hz_cust_site_uses_all hcsua, -- uses of -- customer addresses
                                    hz_cust_acct_sites_all hcasa, -- customer addresses
                                    hz_cust_accounts hca, -- customer accounts
                                    hz_parties hp                   -- parties
                              WHERE     1 = 1
                                    AND ooh.booked_flag = 'Y'
                                    AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                                    AND hcsua.cust_acct_site_id =
                                           hcasa.cust_acct_site_id
                                    AND hcasa.cust_account_id =
                                           hca.cust_account_id
                                    AND hca.party_id = hp.party_id
                                    AND is_in_aging_limit (g_aging,
                                                           pi_min_aging_val,
                                                           pi_max_aging_val,
                                                           g_so_type,
                                                           g_past_due_status,
                                                           g_header,
                                                           ooh.header_id) = 'Y'
                                    AND EXISTS
                                           (SELECT 1
                                              FROM oe_order_lines_all ool
                                             WHERE     ool.header_id =
                                                          ooh.header_id
                                                   AND ool.schedule_ship_date
                                                          IS NOT NULL
                                                   AND ool.schedule_ship_date <
                                                          SYSDATE)
                           GROUP BY hp.party_name,
                                    hp.party_id,
                                    'A',
                                    'S'
                             HAVING COUNT (1) < 6)
                 ORDER BY 1)
          WHERE ROWNUM < 6;

      CURSOR cur_rct_cust
      IS
         SELECT *
           FROM (SELECT *
                   FROM (  SELECT COUNT (1) COUNT,
                                  pv.vendor_name customer,
                                  pv.party_id customer_id,
                                  'C' count_aging_flag,
                                  'R' so_rcpt_flag
                             FROM rcv_shipment_headers rsh,
                                  rcv_shipment_lines rsl,
                                  po_headers_all poh,
                                  po_vendors pv
                            WHERE     rsh.shipment_header_id =
                                         rsl.shipment_header_id
                                  AND rsl.po_header_id = poh.po_header_id
                                  AND poh.vendor_id = pv.vendor_id
                                  AND rsh.expected_receipt_date <= SYSDATE
                                  AND rsl.shipment_line_status_code =
                                         'EXPECTED'
                         GROUP BY pv.vendor_id,
                                  pv.vendor_name,
                                  'C',
                                  'R'
                           HAVING COUNT (1) < 6
                         ORDER BY 1))
          WHERE ROWNUM < 6;

      CURSOR cur_rct_cust_aging
      IS
         SELECT *
           FROM (SELECT *
                   FROM (  SELECT COUNT (1) COUNT,
                                  pv.vendor_name customer,
                                  pv.party_id customer_id,
                                  'A' count_aging_flag,
                                  'R' so_rcpt_flag
                             FROM rcv_shipment_headers rsh,
                                  rcv_shipment_lines rsl,
                                  po_headers_all poh,
                                  po_vendors pv
                            WHERE     rsh.shipment_header_id =
                                         rsl.shipment_header_id
                                  AND rsl.po_header_id = poh.po_header_id
                                  AND poh.vendor_id = pv.vendor_id
                                  AND is_in_aging_limit (g_aging,
                                                         pi_min_aging_val,
                                                         pi_max_aging_val,
                                                         g_rcpt_type,
                                                         NULL,
                                                         g_header,
                                                         poh.po_header_id) =
                                         'Y'
                                  AND rsl.shipment_line_status_code =
                                         'EXPECTED'
                         GROUP BY pv.vendor_id,
                                  pv.vendor_name,
                                  'A',
                                  'R'
                           HAVING COUNT (1) < 6
                         ORDER BY 1))
          WHERE ROWNUM < 6;

      TYPE lcu_cust_tbl_type IS TABLE OF cur_so_hold_cust_details%ROWTYPE
         INDEX BY PLS_INTEGER;

      tt_cust_tbl        lcu_cust_tbl_type;

      lv_status          VARCHAR2 (3);
      lv_error_message   VARCHAR2 (3000);
   BEGIN
      IF pi_so_or_rct = g_so_type
      THEN
         IF pi_status = g_hold_status
         THEN                                                    --Hold Status
            OPEN cur_so_hold_cust_details;

            FETCH cur_so_hold_cust_details BULK COLLECT INTO tt_cust_tbl;

            CLOSE cur_so_hold_cust_details;

            OPEN cur_so_hold_cust_aging;

            FETCH cur_so_hold_cust_aging BULK COLLECT INTO tt_cust_tbl;

            CLOSE cur_so_hold_cust_aging;
         END IF;

         IF pi_status = g_backorder_status
         THEN                                                    --Backordered
            OPEN cur_backord_cust;

            FETCH cur_backord_cust BULK COLLECT INTO tt_cust_tbl;

            CLOSE cur_backord_cust;

            OPEN cur_backord_cust_aging;

            FETCH cur_backord_cust_aging BULK COLLECT INTO tt_cust_tbl;

            CLOSE cur_backord_cust_aging;
         END IF;

         IF pi_status = g_jeopardy_order_status
         THEN                                                       --Jeopardy
            OPEN cur_injeopardy_cust;

            FETCH cur_injeopardy_cust BULK COLLECT INTO tt_cust_tbl;

            CLOSE cur_injeopardy_cust;

            OPEN cur_injeopardy_cust_aging;

            FETCH cur_injeopardy_cust_aging BULK COLLECT INTO tt_cust_tbl;

            CLOSE cur_injeopardy_cust_aging;
         END IF;

         IF pi_status = g_past_due_status
         THEN
            OPEN cur_past_due_cust;

            FETCH cur_past_due_cust BULK COLLECT INTO tt_cust_tbl;

            CLOSE cur_past_due_cust;

            OPEN cur_past_due_cust_aging;

            FETCH cur_past_due_cust_aging BULK COLLECT INTO tt_cust_tbl;

            CLOSE cur_past_due_cust_aging;
         END IF;
      ELSIF pi_so_or_rct = g_rcpt_type
      THEN
         OPEN cur_rct_cust;

         FETCH cur_rct_cust BULK COLLECT INTO tt_cust_tbl;

         CLOSE cur_rct_cust;

         OPEN cur_rct_cust_aging;

         FETCH cur_rct_cust_aging BULK COLLECT INTO tt_cust_tbl;

         CLOSE cur_rct_cust_aging;
      END IF;

      px_cust_count := xxmaf_ofa_l2_tbl ();

      FOR indx IN 1 .. tt_cust_tbl.COUNT
      LOOP
         px_cust_count.EXTEND ();
         px_cust_count (indx) :=
            xxmaf_ofa_l2_obj (NULL,
                              NULL,
                              NULL,
                              NULL,
                              NULL);
         --  dbms_output.put_line('Inserting Data');
         px_cust_count (indx).customer_name := tt_cust_tbl (indx).customer;
         px_cust_count (indx).COUNT := tt_cust_tbl (indx).COUNT;
         px_cust_count (indx).customer_id := tt_cust_tbl (indx).customer_id;
         px_cust_count (indx).so_rcpt_flag := tt_cust_tbl (indx).so_rcpt_flag;
         px_cust_count (indx).count_aging_flag :=
            tt_cust_tbl (indx).count_aging_flag;
      --  dbms_output.put_line('px_cust_count(indx).custoomer_name'||px_cust_count(indx).custoomer_name);
      --  dbms_output.put_line('tt_cust_tbl(indx).customer'||tt_cust_tbl(indx).customer);
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         lv_status := 'E';
         lv_error_message := SQLERRM || DBMS_UTILITY.format_error_backtrace;
         DBMS_OUTPUT.put_line ('Error' || lv_error_message);
   END get_so_rct_details_l2;

   --l3 denotes Level 3 Information--
   PROCEDURE get_so_rct_header_details_l3 (
      pi_status          IN     VARCHAR2,         --<-- Passed from Middleware
      pi_customer_id     IN     NUMBER,           --<-- Passed from Middleware
      pi_so_or_rct       IN     VARCHAR2, --<-- Passed from Middleware O - Sales Order, R- Receipt
      pi_aging_count     IN     VARCHAR2, --<--Passed A from Aging Page C from Counts page
      pi_min_aging_val   IN     NUMBER,
      pi_max_aging_val   IN     NUMBER,
      px_header_type        OUT xxmaf_ofa_header_tbl)
   IS
      lv_status          VARCHAR2 (3);
      lv_error_message   VARCHAR2 (3000);

      --On Hold Order Header--
      CURSOR cur_order_hold
      IS
         SELECT DISTINCT ooh.header_id doc_header_id,
                         ooh.order_number document_number,
                         g_so_type document_type,
                         pi_aging_count count_or_aging,
                         0 order_value,
                         hp.party_name customer,
                         hcsua.location
           FROM oe_order_holds_all ooha,
                oe_hold_sources_all ohsa,
                oe_hold_definitions ohd,
                oe_order_headers_all ooh,
                hz_cust_site_uses_all hcsua,     -- uses of customer addresses
                hz_cust_acct_sites_all hcasa,            -- customer addresses
                hz_cust_accounts hca,                     -- customer accounts
                hz_parties hp                                       -- parties
          WHERE     1 = 1
                AND ooha.header_id = ooh.header_id
                AND ooha.hold_source_id = ohsa.hold_source_id
                AND ohsa.hold_id = ohd.hold_id
                AND ooha.released_flag = 'N'
                AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                AND hcsua.cust_acct_site_id = hcasa.cust_acct_site_id
                AND hcasa.cust_account_id = hca.cust_account_id
                AND hca.party_id = hp.party_id
                AND hca.party_id = pi_customer_id
                AND is_in_aging_limit (pi_aging_count,
                                       pi_min_aging_val,
                                       pi_max_aging_val,
                                       g_so_type,
                                       g_hold_status,
                                       g_header,
                                       ooh.header_id) = 'Y'
         UNION
         SELECT DISTINCT ooh.header_id doc_header_id,
                         ooh.order_number document_number,
                         g_so_type document_type,
                         pi_aging_count count_or_aging,
                         0 order_value,
                         hp.party_name customer,
                         hcsua.location
           FROM oe_order_holds_all ooha,
                oe_hold_sources_all ohsa,
                oe_hold_definitions ohd,
                oe_order_headers_all ooh,
                hz_cust_site_uses_all hcsua,     -- uses of customer addresses
                hz_cust_acct_sites_all hcasa,            -- customer addresses
                hz_cust_accounts hca,                     -- customer accounts
                hz_parties hp                                       -- parties
          WHERE     1 = 1
                AND ooha.header_id = ooha.header_id
                AND ooha.header_id = ooh.header_id
                AND ooha.hold_source_id = ohsa.hold_source_id
                AND ohsa.hold_id = ohd.hold_id
                AND is_in_aging_limit (pi_aging_count,
                                       pi_min_aging_val,
                                       pi_max_aging_val,
                                       g_so_type,
                                       g_hold_status,
                                       g_header,
                                       ooh.header_id) = 'Y'
                AND EXISTS
                       (SELECT 1
                          FROM oe_order_lines_all ool
                         WHERE     ool.header_id = ooh.header_id
                               AND ooha.line_id = ool.line_id
                               AND ooha.released_flag = 'N')
                AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                AND hcsua.cust_acct_site_id = hcasa.cust_acct_site_id
                AND hcasa.cust_account_id = hca.cust_account_id
                AND hca.party_id = hp.party_id
                AND hca.party_id = pi_customer_id;


      --Back Order Cursor--
      CURSOR cur_backorder
      IS
         SELECT ooh.header_id doc_header_id,
                ooh.order_number document_number,
                g_so_type document_type,
                pi_aging_count count_or_aging,
                0 order_value,
                hp.party_name customer,
                hcsua.location
           FROM oe_order_headers_all ooh,
                hz_cust_site_uses_all hcsua,     -- uses of customer addresses
                hz_cust_acct_sites_all hcasa,            -- customer addresses
                hz_cust_accounts hca,                     -- customer accounts
                hz_parties hp                                       -- parties
          WHERE     1 = 1
                AND ooh.booked_flag = 'Y'
                AND EXISTS
                       (SELECT 1
                          FROM oe_order_lines_all ool,
                               wsh_delivery_details wdd
                         WHERE     ool.header_id = ooh.header_id
                               AND wdd.source_line_id = ool.line_id
                               AND wdd.released_status = 'B'     --Backordered
                                                            )
                AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                AND hcsua.cust_acct_site_id = hcasa.cust_acct_site_id
                AND hcasa.cust_account_id = hca.cust_account_id
                AND hca.party_id = hp.party_id
                AND is_in_aging_limit (pi_aging_count,
                                       pi_min_aging_val,
                                       pi_max_aging_val,
                                       g_so_type,
                                       g_backorder_status,
                                       g_header,
                                       ooh.header_id) = 'Y'
                AND hca.party_id = pi_customer_id;

      --Past Due Date Order Cursor--
      CURSOR cur_past_due
      IS
         SELECT ooh.header_id doc_header_id,
                ooh.order_number document_number,
                g_so_type document_type,
                pi_aging_count count_or_aging,
                0 order_value,
                hp.party_name customer,
                hcsua.location
           FROM oe_order_headers_all ooh,
                hz_cust_site_uses_all hcsua,  -- uses of -- customer addresses
                hz_cust_acct_sites_all hcasa,            -- customer addresses
                hz_cust_accounts hca,                     -- customer accounts
                hz_parties hp                                       -- parties
          WHERE     1 = 1
                AND ooh.booked_flag = 'Y'
                AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                AND hcsua.cust_acct_site_id = hcasa.cust_acct_site_id
                AND hcasa.cust_account_id = hca.cust_account_id
                AND hca.party_id = hp.party_id
                AND is_in_aging_limit (pi_aging_count,
                                       pi_min_aging_val,
                                       pi_max_aging_val,
                                       g_so_type,
                                       g_past_due_status,
                                       g_header,
                                       ooh.header_id) = 'Y'
                AND EXISTS
                       (SELECT 1
                          FROM oe_order_lines_all ool
                         WHERE     ool.header_id = ooh.header_id
                               AND ool.schedule_ship_date IS NOT NULL
                               AND ool.schedule_ship_date < SYSDATE);

      --In Jeopardy Cursor--

      CURSOR cur_injeopardy
      IS
         SELECT ooh.header_id doc_header_id,
                ooh.order_number document_number,
                g_so_type document_type,
                pi_aging_count count_or_aging,
                0 order_value,
                hp.party_name customer,
                hcsua.location
           FROM oe_order_headers_all ooh,
                hz_cust_site_uses_all hcsua,     -- uses of customer addresses
                hz_cust_acct_sites_all hcasa,            -- customer addresses
                hz_cust_accounts hca,                     -- customer accounts
                hz_parties hp                                       -- parties
          WHERE     1 = 1
                AND ooh.booked_flag = 'Y'
                AND ooh.ship_to_org_id = hcsua.site_use_id -- or a.invoice_to_org_id
                AND hcsua.cust_acct_site_id = hcasa.cust_acct_site_id
                AND hcasa.cust_account_id = hca.cust_account_id
                AND hca.party_id = hp.party_id
                AND hca.party_id = pi_customer_id
                AND is_in_aging_limit (pi_aging_count,
                                       pi_min_aging_val,
                                       pi_max_aging_val,
                                       g_so_type,
                                       g_jeopardy_order_status,
                                       g_header,
                                       ooh.header_id) = 'Y'
                AND EXISTS
                       (SELECT 1
                          FROM oe_order_lines_all ool
                         WHERE     ooh.header_id = ool.header_id
                               AND ool.schedule_status_code IS NULL
                               AND (SYSDATE - ool.creation_date) > 1);



      ---Receipt Information--
      CURSOR cur_rcp_header
      IS
           SELECT poh.po_header_id doc_header_id,
                  poh.segment1 document_number,
                  g_rcpt_type document_type,
                  pi_aging_count count_or_aging,
                  SUM ( (pol.quantity * pol.unit_price)) order_value,
                  pv.vendor_name customer,
                  hl.city location
             FROM rcv_shipment_headers rsh,
                  rcv_shipment_lines rsl,
                  po_lines_all pol,
                  po_headers_all poh,
                  po_vendors pv,
                  hz_locations hl
            WHERE     1 = 1
                  AND rsh.shipment_header_id = rsl.shipment_header_id
                  AND rsl.po_header_id = pol.po_header_id
                  AND rsl.po_line_id = pol.po_line_id
                  AND pol.po_header_id = poh.po_header_id
                  AND rsh.expected_receipt_date <= SYSDATE
                  AND rsl.shipment_line_status_code = 'EXPECTED'
                  AND poh.vendor_id = pv.vendor_id
                  AND pv.party_id = pi_customer_id
                  AND hl.location_id(+) = rsh.ship_from_location_id
                  AND is_in_aging_limit (pi_aging_count,
                                         pi_min_aging_val,
                                         pi_max_aging_val,
                                         g_rcpt_type,
                                         NULL,
                                         g_header,
                                         poh.po_header_id) = 'Y'
         GROUP BY poh.po_header_id,
                  poh.segment1,
                  pv.vendor_name,
                  4,
                  hl.city;


      TYPE lcu_header_tbl_type IS TABLE OF cur_order_hold%ROWTYPE
         INDEX BY PLS_INTEGER;

      tt_header_tbl      lcu_header_tbl_type;
   BEGIN
      IF pi_so_or_rct = g_so_type
      THEN
         IF pi_status = g_hold_status
         THEN                                                    --Hold Status
            OPEN cur_order_hold;

            FETCH cur_order_hold BULK COLLECT INTO tt_header_tbl;

            CLOSE cur_order_hold;
         END IF;

         IF pi_status = g_backorder_status
         THEN                                                    --Backordered
            OPEN cur_backorder;

            FETCH cur_backorder BULK COLLECT INTO tt_header_tbl;

            CLOSE cur_backorder;
         END IF;

         IF pi_status = g_past_due_status
         THEN
            OPEN cur_past_due;

            FETCH cur_past_due BULK COLLECT INTO tt_header_tbl;

            CLOSE cur_past_due;
         END IF;

         IF pi_status = g_jeopardy_order_status
         THEN                                                       --Jeopardy
            OPEN cur_injeopardy;

            FETCH cur_injeopardy BULK COLLECT INTO tt_header_tbl;

            CLOSE cur_injeopardy;
         END IF;
      ELSIF pi_so_or_rct = g_rcpt_type
      THEN
         OPEN cur_rcp_header;

         FETCH cur_rcp_header BULK COLLECT INTO tt_header_tbl;

         CLOSE cur_rcp_header;
      END IF;

      px_header_type := xxmaf_ofa_header_tbl ();

      FOR indx IN 1 .. tt_header_tbl.COUNT
      LOOP
         px_header_type.EXTEND ();
         px_header_type (indx) :=
            xxmaf_ofa_header_obj (NULL,
                                  NULL,
                                  NULL,
                                  NULL,
                                  NULL,
                                  NULL,
                                  NULL);
         px_header_type (indx).doc_header_id :=
            tt_header_tbl (indx).doc_header_id;
         px_header_type (indx).document_number :=
            tt_header_tbl (indx).document_number;
         px_header_type (indx).order_value := tt_header_tbl (indx).order_value;
         px_header_type (indx).customer := tt_header_tbl (indx).customer;
         px_header_type (indx).location := tt_header_tbl (indx).location;
         px_header_type (indx).document_type :=
            tt_header_tbl (indx).document_type;
         px_header_type (indx).count_or_aging :=
            tt_header_tbl (indx).count_or_aging;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         lv_status := 'E';
         lv_error_message := SQLERRM || DBMS_UTILITY.format_error_backtrace;
   END get_so_rct_header_details_l3;

   PROCEDURE get_so_rct_line_details_l4 (
      pi_header_id       IN     NUMBER,           --<-- Passed from Middleware
      pi_status          IN     VARCHAR2,
      pi_so_or_rct       IN     VARCHAR2, --<-- Passed from Middleware O - Sales Order, R- Receipt
      pi_aging_count     IN     VARCHAR2,
      pi_min_aging_val   IN     NUMBER,
      pi_max_aging_val   IN     NUMBER,
      px_line_type          OUT xxmaf_ofa_line_tbl)
   IS
      lv_status          VARCHAR2 (3);
      lv_error_message   VARCHAR2 (3000);

      CURSOR cur_so_line
      IS
         SELECT g_so_type document_type,
                pi_aging_count count_or_aging,
                ool.header_id doc_header_id,
                ool.line_id doc_line_id,
                ool.line_number doc_line_num,
                msi.segment1 item_number,
                ool.ordered_quantity quantity,
                ool.freight_carrier_code carrier,
                mp.organization_code warehouse,
                ool.schedule_ship_date ssd
           FROM oe_order_lines_all ool,
                mtl_system_items_b msi,
                mtl_parameters mp
          WHERE     1 = 1
                AND ool.header_id = pi_header_id
                AND msi.inventory_item_id = ool.inventory_item_id
                AND ool.ship_from_org_id = msi.organization_id
                AND is_in_aging_limit (pi_aging_count,
                                       pi_min_aging_val,
                                       pi_max_aging_val,
                                       g_so_type,
                                       pi_status,
                                       g_line,
                                       ool.line_id) = 'Y'
                AND mp.organization_id = ool.ship_from_org_id;


      CURSOR cur_rcpt_line
      IS
         SELECT g_rcpt_type document_type,
                pi_aging_count count_or_aging,
                poh.po_header_id doc_header_id,
                pol.po_line_id doc_line_id,
                pol.line_num doc_line_num,
                msi.segment1 item_number,
                pol.quantity,
                NULL carrier,
                mp.organization_code warehouse,
                expected_receipt_date ssd
           FROM rcv_shipment_headers rsh,
                rcv_shipment_lines rsl,
                po_lines_all pol,
                po_headers_all poh,
                mtl_system_items_b msi,
                mtl_parameters mp
          WHERE     1 = 1
                AND rsh.shipment_header_id = rsl.shipment_header_id
                AND rsl.po_header_id = pol.po_header_id
                AND rsl.po_line_id = pol.po_line_id
                AND pol.po_header_id = poh.po_header_id
                AND pol.item_id = msi.inventory_item_id
                AND rsl.to_organization_id = msi.organization_id
                AND mp.organization_id = msi.organization_id
                AND rsh.expected_receipt_date <= SYSDATE
                AND rsl.shipment_line_status_code = 'EXPECTED'
                AND is_in_aging_limit (pi_aging_count,
                                       pi_min_aging_val,
                                       pi_max_aging_val,
                                       g_rcpt_type,
                                       pi_status,
                                       g_line,
                                       pol.po_line_id) = 'Y'
                AND poh.Segment1 = pi_header_id;


      TYPE lcu_line_tbl_type IS TABLE OF cur_so_line%ROWTYPE
         INDEX BY PLS_INTEGER;

      tt_line_tbl        lcu_line_tbl_type;
   BEGIN
      IF pi_so_or_rct = g_so_type
      THEN                                                 --Sales Order Lines
         OPEN cur_so_line;

         FETCH cur_so_line BULK COLLECT INTO tt_line_tbl;

         CLOSE cur_so_line;
      END IF;

      IF pi_so_or_rct = g_rcpt_type
      THEN
         OPEN cur_rcpt_line;

         FETCH cur_rcpt_line BULK COLLECT INTO tt_line_tbl;

         CLOSE cur_rcpt_line;
      END IF;

      px_line_type := xxmaf_ofa_line_tbl ();

      FOR indx IN 1 .. tt_line_tbl.COUNT
      LOOP
         px_line_type.EXTEND ();
         px_line_type (indx) :=
            xxmaf_ofa_line_obj (NULL,
                                NULL,
                                NULL,
                                NULL,
                                NULL,
                                NULL,
                                NULL,
                                NULL,
                                NULL,
                                NULL,
                                NULL);
         px_line_type (indx).doc_header_id := tt_line_tbl (indx).doc_header_id;
         px_line_type (indx).doc_line_id := tt_line_tbl (indx).doc_line_id;
         px_line_type (indx).doc_line_num := tt_line_tbl (indx).doc_line_num;
         px_line_type (indx).item_number := tt_line_tbl (indx).item_number;
         px_line_type (indx).quantity := tt_line_tbl (indx).quantity;
         px_line_type (indx).carrier := tt_line_tbl (indx).carrier;
         px_line_type (indx).warehouse := tt_line_tbl (indx).warehouse;
         px_line_type (indx).ssd := tt_line_tbl (indx).ssd;
         px_line_type (indx).document_type := tt_line_tbl (indx).document_type;
         px_line_type (indx).count_or_aging :=
            tt_line_tbl (indx).count_or_aging;
         px_line_type (indx).aging_days := 0;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         lv_status := 'E';
         lv_error_message := SQLERRM || DBMS_UTILITY.format_error_backtrace;
   END get_so_rct_line_details_l4;

   --This Function shall calcualte if the passed entity SO/RCPT Header/Line falls inside the Aging Value Limit
   FUNCTION is_in_aging_limit (pi_count_aging     IN VARCHAR2,
                               pi_min_aging_val   IN NUMBER,
                               pi_max_aging_val   IN NUMBER,
                               pi_document_type   IN VARCHAR2, -- Sales Order or Receipt
                               pi_status          IN VARCHAR2,
                               pi_entity          IN VARCHAR2, --H - Headers L - Lines
                               pi_entity_id       IN NUMBER)
      RETURN VARCHAR2
   IS
   lv_return_val  VARCHAR2(3) := 'Y';
   lv_temp        NUMBER := 0;
   ln_only_header_hold NUMBER := 0;
   ln_only_line_hold   NUMBER := 0;
   ln_count  NUMBER := 0;
   BEGIN 
   --<Logic to derive if the Header/Line falls inside the Aging Limit>
IF pi_count_aging = g_aging THEN  --AAAA
  IF pi_entity = g_header THEN    --BBBB
     IF pi_document_type = g_so_type THEN --CCCC
     
     --For Sales Order--     
        IF pi_status = g_hold_status THEN
        
        SELECT COUNT(1)
        INTO ln_only_header_hold 
        FROM oe_order_headers_all ooh,
             oe_order_holds_all ooha
        WHERE ooh.header_id = pi_entity_id
        AND   ooh.header_id = ooha.header_id
        AND   ooha.line_id IS NULL
        AND   TRUNC(SYSDATE - ooha.creation_date) BETWEEN pi_max_aging_val AND pi_min_aging_val;
        
        SELECT COUNT(1)
        INTO ln_only_line_hold 
        FROM oe_order_headers_all ooh
        WHERE ooh.header_id = pi_entity_id
        AND   EXISTS (SELECT 1 
                      FROM oe_order_lines_all ool,
                           oe_order_holds_all ooha
                      WHERE ooha.header_id = ooh.header_id
                      AND   ool.header_id  = ooh.header_id
                      AND   ool.line_id = ooha.line_id
                      AND   TRUNC(SYSDATE - ooha.creation_date) BETWEEN pi_max_aging_val AND pi_min_aging_val);
                      
         ln_count := ln_only_header_hold + ln_only_line_hold;
               
        END IF; --Hold Status
        
        IF pi_status = g_backorder_status THEN 
        
        SELECT COUNT(1)
        INTO ln_count
        FROM oe_order_headers_all ooh
        WHERE ooh.header_id = pi_entity_id
        AND EXISTS
                        (SELECT 1
                              FROM oe_order_lines_all ool,
                                   wsh_delivery_details wdd
                              WHERE     ool.header_id = ooh.header_id
                                                   AND wdd.source_line_id =
                                                          ool.line_id
                                                   AND wdd.released_status = 'B'
                                                   AND (SYSDATE - wdd.last_update_date) BETWEEN pi_max_aging_val AND pi_min_aging_val); --Backordered
                                                                                
        END IF; --Backorder count
        
        IF pi_status = g_jeopardy_order_status THEN
        
        SELECT COUNT(1)
        INTO ln_count
        FROM oe_order_headers_all ooh
        WHERE ooh.header_id = pi_entity_id
        AND EXISTS
                                           (SELECT 1
                                              FROM oe_order_lines_all ool
                                             WHERE     ooh.header_id =
                                                          ool.header_id
                                                   AND ool.schedule_status_code
                                                          IS NULL
                                                   AND (  SYSDATE
                                                        - ool.creation_date) > 1
                                                   AND  (SYSDATE
                                                        - ool.creation_date) BETWEEN pi_max_aging_val AND pi_min_aging_val);
        
        END IF; --Jeopardy Count
        
        IF pi_status = g_past_due_status THEN
        SELECT COUNT(1)
        INTO ln_count
        FROM oe_order_headers_all ooh
        WHERE ooh.header_id = pi_entity_id
         AND EXISTS
          (
            SELECT
              1
            FROM
              oe_order_lines_all ool
            WHERE
              ool.header_id             = ooh.header_id
            AND ool.schedule_ship_date IS NOT NULL
            AND ool.schedule_ship_date  < SYSDATE
          );
        END IF; --Past due date count
       ELSE --BBBB
       --Receipt status --
       SELECT COUNT (1) COUNT
       INTO ln_count
                             FROM rcv_shipment_headers rsh,
                                  rcv_shipment_lines rsl,
                                  po_headers_all poh,
                                  po_vendors pv
                            WHERE     rsh.shipment_header_id =
                                         rsl.shipment_header_id
                                  AND rsl.po_header_id = poh.po_header_id
                                  AND poh.vendor_id = pv.vendor_id
                                  AND rsh.expected_receipt_date <= SYSDATE
                                  AND rsl.shipment_line_status_code =
                                         'EXPECTED'
                                  AND (SYSDATE - rsh.expected_receipt_date) BETWEEN pi_max_aging_val AND pi_min_aging_val;
                              
       END IF;     
       IF pi_entity = g_line THEN
           IF pi_status = g_hold_status THEN
           SELECT COUNT(1)
           INTO ln_count 
           FROM oe_order_lines_all ool,
                oe_order_holds_all ooha
           WHERE ool.line_id = pi_entity_id
           AND   ool.line_id = ooha.line_id
           AND   ooha.released_flag = 'N' 
           AND   (SYSDATE - ooha.creation_date) BETWEEN pi_max_aging_val AND pi_min_aging_val;
           END IF;
      
           IF pi_status = g_backorder_status THEN
           SELECT COUNT(1)
           INTO ln_count 
           FROM oe_order_lines_all ool,
                wsh_delivery_details wdd
           WHERE ool.line_id = pi_entity_id
           AND   wdd.source_line_id = ool.line_id
           AND wdd.released_status = 'B'
           AND (SYSDATE - wdd.last_update_date) BETWEEN pi_max_aging_val AND pi_min_aging_val;
           END IF;
      
           IF pi_status = g_jeopardy_order_status THEN
           SELECT COUNT(1)
           INTO ln_count 
           FROM oe_order_lines_all ool
           WHERE ool.line_id = pi_entity_id
           AND ool.schedule_status_code IS NULL
           AND (SYSDATE - ool.creation_date) > 1
           AND (SYSDATE - ool.creation_date) BETWEEN pi_max_aging_val AND pi_min_aging_val;
           END IF;
           
           IF pi_status = g_past_due_status THEN
           SELECT COUNT(1)
           INTO ln_count 
           FROM oe_order_lines_all ool
           WHERE ool.line_id = pi_entity_id
           AND ool.schedule_ship_date IS NOT NULL
           AND ool.schedule_ship_date  < SYSDATE
           AND (SYSDATE - ool.schedule_ship_date) BETWEEN pi_max_aging_val AND pi_min_aging_val;
           END IF;           
       END IF;
     END IF;               
        IF pi_document_type = g_rcpt_type THEN
         SELECT COUNT(1) 
         INTO ln_count
         FROM rcv_shipment_headers rsh,
              rcv_shipment_lines rsl
         WHERE rsl.po_line_id = pi_entity_id
         AND rsl.shipment_line_status_code = 'EXPECTED'
         AND rsh.shipment_header_id = rsl.shipment_header_id
         AND rsh.expected_receipt_date <= SYSDATE
         AND (SYSDATE - rsh.expected_receipt_date) BETWEEN pi_max_aging_val AND pi_min_aging_val;
        END IF;                  
      IF ln_count = 0 THEN 
      lv_return_val := 'N';
      END IF;      
   ELSE
   lv_return_val := 'Y';
   END IF; --AAAA          
      RETURN lv_return_val;
   END is_in_aging_limit;
END xxmaf_so_rcpt_pkg;