package com.osen.aqms.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.aqms.modules.entity.message.MessagePush;
import com.osen.aqms.modules.mapper.message.MessagePushMapper;
import com.osen.aqms.modules.service.MessagePushService;
import org.springframework.stereotype.Service;

/**
 * User: PangYi
 * Date: 2019-11-30
 * Time: 10:52
 * Description:
 */
@Service
public class MessagePushServiceImpl extends ServiceImpl<MessagePushMapper, MessagePush> implements MessagePushService {
}
