ALTER TABLE `fas_mall_pay`
ADD COLUMN `mall_pay_amount`  decimal(20,4) NULL DEFAULT 0.0000 COMMENT '物业方支付方式' AFTER `pay_amount`,
ADD COLUMN `diff_pay_amount`  decimal(20,4) NULL DEFAULT 0.0000 COMMENT '差异支付金额' AFTER `mall_pay_amount`;

alter table fas_bill_mall_balance MODIFY `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';

alter table fas_mall_balance_date MODIFY `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';

alter table fas_mall_cost MODIFY `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';

alter table fas_property_cost MODIFY `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';


