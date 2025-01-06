package com.storecloud.trade.listener;

import com.storecloud.trade.domain.po.Order;
import com.storecloud.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayStatusListener {

    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "trade.pay.success.queue", durable = "true"),
            exchange = @Exchange(value = "pay.direct"),
            key = "pay.success"
    ))
    public void ListenPaySuccess(Long id) {
        // 1.查询订单
        Order order = orderService.getById(id);
        // 2.判断订单状态，是否为未支付
        if(order == null || order.getStatus() != 1){
            // 不做处理
            return;
        }
        // 3.标记支付单为已支付
        orderService.markOrderPaySuccess(id);
    }
}
