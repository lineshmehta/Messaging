select distinct 
       lcs.legacy_product_ref,
       pl.product_id_member
from legacy_code_system lcs,
     price_plan_fixed ppf,
     product_link pl,
     product p
where lcs.legacy_code_id = ppf.legacy_code_id
      and lcs.LEGACY_SYSTEM_ID = 1 and lcs.INFO_IS_DELETED = 'N'
      and ppf.INFO_IS_DELETED = 'N' and getDate() between ppf.PRICE_PL_FIXED_VALID_FROM_DATE and ppf.PRICE_PL_FIXED_VALID_TO_DATE
      and pl.INFO_IS_DELETED = 'N' and getDate() between pl.PRODUCT_LINK_VALID_FROM_DATE and pl.PRODUCT_LINK_VALID_TO_DATE
      and p.INFO_IS_DELETED = 'N' and getDate() between p.PRODUCT_VALID_FROM_DATE and p.PRODUCT_VALID_TO_DATE
      and ppf.product_link_id = pl.product_link_id and pl.product_id_member = p.product_id
      and p.PRODUCT_TYPE_ID = 'X'