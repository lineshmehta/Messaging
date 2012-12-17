select  C.CUST_ID,
        C.MASTER_ID,
        C.CUST_FIRST_NAME, 
        C.CUST_MIDDLE_NAME,
        C.CUST_LAST_NAME,
        C.CUST_UNIT_NUMBER,
        C.POSTCODE_ID_MAIN,
        C.POSTCODE_NAME_MAIN,
        C.ADDR_LINE_MAIN,
        C.ADDR_CO_NAME,
        C.ADDR_STREET_NAME,
        C.ADDR_STREET_NUMBER
from    CUSTOMER C
where   C.INFO_IS_DELETED = 'N'
go