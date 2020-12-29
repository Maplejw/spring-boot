package com.igg.boot.framework.jdbc.persistence.shard;

import java.util.Map;

import org.springframework.core.ResolvableType;

import com.igg.boot.framework.jdbc.persistence.Entity;

public abstract class AbstractShardStrategy<E extends Entity> implements ShardStrategy<E> {
    protected int factor;
    protected String split = "";
    protected String field;
    private Class<E> clz;

    @SuppressWarnings("unchecked")
    public AbstractShardStrategy(int factor, String split, String field) {
        this.factor = factor;
        this.split = split;
        this.field = field;
        ResolvableType resolvableType = ResolvableType.forClass(this.getClass());
        ResolvableType[] params = resolvableType.as(ShardStrategy.class).getGenerics();
        this.clz = (Class<E>) params[0].resolve();
    }

    public Class<E> getEntityClass() {
        return this.clz;
    }

    public String getField() {
        return field;
    }
    
    private String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    @Override
    public String getShardName(Map<String, Object> params) {
        Object value = 0;
        if(params.containsKey(field)) {
            value = params.get(field);
        }else {
            //转驼峰
            String camelNameField = camelName(field);
            value = params.get(camelNameField);
        }
        return split + getShard(value);
    }

    abstract protected int getShard(Object value);
}
