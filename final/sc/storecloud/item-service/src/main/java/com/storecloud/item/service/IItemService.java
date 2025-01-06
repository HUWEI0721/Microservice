package com.storecloud.item.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.storecloud.api.dto.ItemDTO;
import com.storecloud.api.dto.OrderDetailDTO;
import com.storecloud.item.domain.po.Item;

import java.util.Collection;
import java.util.List;

public interface IItemService extends IService<Item> {

    void deductStock(List<OrderDetailDTO> items);

    List<ItemDTO> queryItemByIds(Collection<Long> ids);
}
