package com.storecloud.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.storecloud.trade.domain.po.OrderDetail;
import com.storecloud.trade.mapper.OrderDetailMapper;
import com.storecloud.trade.service.IOrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
