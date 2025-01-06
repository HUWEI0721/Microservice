package com.storecloud.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.storecloud.pay.domain.dto.PayApplyDTO;
import com.storecloud.pay.domain.dto.PayOrderFormDTO;
import com.storecloud.pay.domain.po.PayOrder;

public interface IPayOrderService extends IService<PayOrder> {

    String applyPayOrder(PayApplyDTO applyDTO);

    void tryPayOrderByBalance(PayOrderFormDTO payOrderFormDTO);
}
