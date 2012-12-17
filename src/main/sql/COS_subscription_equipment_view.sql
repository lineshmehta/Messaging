select subscr_id, IMSI_NUMBER_ID
from SUBSCRIPTION_EQUIPMENT_INFO
where INFO_IS_DELETED != 'N' and (valid_from_date is null or valid_from_date > getDate())
go