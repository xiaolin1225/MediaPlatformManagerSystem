package com.xiaolin.mpms.filter;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;

/**
 * 排除敏感属性
 *
 */
public class PropertyPreExcludeFilter extends SimplePropertyPreFilter
{
    public PropertyPreExcludeFilter()
    {
    }

    public PropertyPreExcludeFilter addExcludes(String... filters)
    {
        for (String filter : filters) {
            this.getExcludes().add(filter);
        }
        return this;
    }
}
