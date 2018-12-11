

ALTER TABLE `fas_mall_balance_date`
ADD COLUMN `calculation_method`  tinyint(3) NOT NULL DEFAULT 0 COMMENT '0:毛收入,1:净收入' AFTER `check_date`;

ALTER TABLE `fas_mall_balance_date_dtl`
ADD COLUMN `calculation_method`  tinyint(3) NOT NULL DEFAULT 0 COMMENT '0:毛收入,1:净收入' AFTER `points_calculate_flag`;