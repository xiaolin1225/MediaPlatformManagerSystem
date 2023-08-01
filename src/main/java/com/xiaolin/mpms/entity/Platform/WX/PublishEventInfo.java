/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.entity.Platform.WX;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishEventInfo {

    @JacksonXmlProperty(localName = "publish_id")
    private String publishId;

    @JacksonXmlProperty(localName = "publish_status")
    private String publishStatus;
    @JacksonXmlProperty(localName = "article_id")
    private String articleId;
    @JacksonXmlProperty(localName = "article_detail")
    private ArticleDetail articleDetail;

    //    @JacksonXmlElementWrapper(localName = "PublishEventInfo")
    @JacksonXmlProperty(localName = "fail_idx")
    private List<String> failIdx = new ArrayList<>();

    public void setFailIdx(String idx) {
        failIdx.add(idx);
    }
}
