package com.storecloud.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.storecloud.trade.domain.po.OrderLogistics;
import com.storecloud.trade.mapper.OrderLogisticsMapper;
import com.storecloud.trade.service.IOrderLogisticsService;
import org.springframework.stereotype.Service;

@Service
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsMapper, OrderLogistics> implements IOrderLogisticsService {

}
