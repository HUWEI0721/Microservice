package com.storecloud.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.storecloud.common.exception.BizIllegalException;
import com.storecloud.common.utils.BeanUtils;
import com.storecloud.api.dto.ItemDTO;
import com.storecloud.api.dto.OrderDetailDTO;
import com.storecloud.item.domain.po.Item;
import com.storecloud.item.mapper.ItemMapper;
import com.storecloud.item.service.IItemService;
import org.springframework.stereotype.Service;
import io.seata.spring.annotation.GlobalTransactional;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Override
    @GlobalTransactional
    public void deductStock(List<OrderDetailDTO> items) {
        String sqlStatement = "com.storecloud.item.mapper.ItemMapper.updateStock";
        boolean r = false;
        try {
            r = executeBatch(items, (sqlSession, entity) -> sqlSession.update(sqlStatement, entity));
        } catch (Exception e) {
            throw new BizIllegalException("更新库存异常，可能是库存不足!", e);
        }
        if (!r) {
            throw new BizIllegalException("库存不足！");
        }
    }

    @Override
    public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
        return BeanUtils.copyList(listByIds(ids), ItemDTO.class);
    }
}
