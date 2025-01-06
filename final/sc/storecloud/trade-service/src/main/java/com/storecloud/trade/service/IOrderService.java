package com.storecloud.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.storecloud.trade.domain.dto.OrderFormDTO;
import com.storecloud.trade.domain.po.Order;

public interface IOrderService extends IService<Order> {

    Long createOrder(OrderFormDTO orderFormDTO);

    void markOrderPaySuccess(Long orderId);
}
