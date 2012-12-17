select      r.RESOURCE_ID,
            r.RESOURCE_TYPE_ID,
            r.RESOURCE_TYPE_ID_KEY,
            r.RESOURCE_HAS_CONTENT_INHERIT,
            r.RESOURCE_HAS_STRUCTURE_INHERIT
    from    RESOURCE r
    where   r.RESOURCE_TYPE_ID in (0,1,2,3,4) and
            isnumeric(r.RESOURCE_TYPE_ID_KEY) = 1 and
            (
                (r.RESOURCE_TYPE_ID = 0) OR
                (r.RESOURCE_TYPE_ID = 1 and
                exists (select 'TRUE' from CustomerMaster..customer c where c.cust_id = convert(numeric(12,0), r.RESOURCE_TYPE_ID_KEY))) OR
                (r.RESOURCE_TYPE_ID = 2 and
                exists (select 'TRUE' from CustomerMaster..account a where a.acc_id = convert(int, r.RESOURCE_TYPE_ID_KEY))) OR
                (r.RESOURCE_TYPE_ID = 3 and
                exists (select 'TRUE' from CustomerMaster..subscription s where s.subscr_id = convert(numeric(12,0), r.RESOURCE_TYPE_ID_KEY))) OR
                (r.RESOURCE_TYPE_ID = 4 and
                exists (select 'TRUE' from CDC..master_customer m where m.master_id = convert(numeric(12,0),r.RESOURCE_TYPE_ID_KEY)))
            )
go