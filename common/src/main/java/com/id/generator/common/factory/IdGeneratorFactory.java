package com.id.generator.common.factory;

import com.id.generator.common.generator.IdGenerator;

public interface IdGeneratorFactory {
    /**
     * 根据bizType创建id生成器
     * @param bizType
     * @return
     */
    IdGenerator getIdGenerator(String bizType);
}
