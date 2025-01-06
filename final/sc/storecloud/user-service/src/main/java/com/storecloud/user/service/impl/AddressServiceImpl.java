package com.storecloud.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.storecloud.user.domain.po.Address;
import com.storecloud.user.mapper.AddressMapper;
import com.storecloud.user.service.IAddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
