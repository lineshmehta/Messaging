select CUST_UNIT_NUMBER,
       MASTER_ID 
from   MASTER_CUSTOMER
where INFO_IS_DELETED = 'N'
      AND CUST_UNIT_NUMBER is not null
go