/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.Platform.WX;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetail {

    @JacksonXmlProperty(localName = "count")
    private String count;

    @JacksonXmlElementWrapper(localName = "item")
    private List<Item> item = new ArrayList<>();

    public void setItem(Item i) {
        item.add(i);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Item {
        @JacksonXmlProperty(localName = "idx")
        private String idx;

        @JacksonXmlProperty(localName = "article_url")
        private String articleUrl;
    }
}
