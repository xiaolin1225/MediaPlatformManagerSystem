/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.media.Media;
import com.xiaolin.mpms.entity.media.MediaMeta;
import com.xiaolin.mpms.entity.MyPage;
import com.xiaolin.mpms.entity.TreeNode;
import com.xiaolin.mpms.entity.media.MediaType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件服务层
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
public interface MediaService extends IService<Media> {

    /**
     * 根据文件名获取文件信息
     *
     * @param serverName 服务器存储文件名
     * @return 文件信息
     */
    Media getOneByServerName(String serverName);

    /**
     * 根据文件MD5获取文件信息
     *
     * @param md5 文件MD5
     * @return 文件信息
     */
    List<Media> getByMd5(String md5);

    /**
     * 根据文件路径获取文件列表
     *
     * @param path     文件路径
     * @param type
     * @param current  当前页
     * @param size     每页大小
     * @param hostPath 主机路径
     * @return 文件列表
     */
    MyPage<Media> getFileListByPath(String path, String type, int current, int size, String hostPath);

    /**
     * 根据文件夹ID获取文件列表
     *
     * @param fid       文件夹ID
     * @param type
     * @param current  当前页
     * @param size     每页大小
     * @param hostPath 主机路径
     * @return 文件列表
     */
    MyPage<Media> getFileListByFolderId(Integer fid, String type, int current, int size, String hostPath);

    /**
     * 根据ID获取文件信息
     *
     * @param id       文件ID
     * @return 文件信息
     */
    Media getFileInfo(Integer id);

    /**
     * 根据文件路径获取文件夹信息
     *
     * @param path 文件路径
     * @return 文件夹信息
     */
    Media getFolderInfoByPath(String path);

    /**
     * 保存文件信息
     *
     * @param uploadPath 上传路径
     * @param title      文件标题
     * @param randomId   文件名
     * @param type       文件类型
     * @param size       文件大小
     * @param filePath   文件路径
     * @param isThumb    是否缩略图
     */
    void saveFileInfo(String uploadPath, String title, String randomId, String type, long size, String filePath, Boolean isThumb);


    /**
     * 创建缩略图并保存缩略图信息
     *
     * @param uploadPath 上传路径
     * @param type   类型
     * @param file   文件
     * @param width  源文件宽度
     * @param height 源文件高度
     * @return 缩略图URL
     * @throws IOException IO异常
     */
    String saveThumbInfo(String uploadPath, String type, File file, int width, int height);

    /**
     * 根据文件ID获取文件元数据
     *
     * @param fid    文件ID
     * @param status 文件状态
     */
    List<MediaMeta> getFileMetaByFid(int fid, int status);

    /**
     * 根据文件ID删除文件
     *
     * @param id 文件ID
     * @return 是否删除成功
     */
    Boolean deleteFileById(int id);

    /**
     * 根据文件ID列表删除文件
     *
     * @param ids 文件ID列表
     * @return 是否删除成功
     */
    boolean deleteFileByIds(List<Integer> ids);

    /**
     * 根据文件ID更新文件信息
     *
     * @param id       文件ID
     * @param media 文件信息
     * @return 是否更新成功
     */
    boolean updateFileInfoById(int id, Media media);

    /**
     * 根据文件元数据ID删除元数据
     *
     * @param id 文件元数据ID
     * @return 是否删除成功
     */
    Boolean deleteFileMetaById(int id);

    /**
     * 创建文件夹
     *
     * @param title    文件夹名称
     * @param folderId 父级文件夹ID
     * @return 文件夹是否创建成功
     */
    Boolean createFolder(String title, int folderId);

    /**
     * 根据ID获取文件类型树
     *
     * @param fid  文件类型父级ID
     * @param lazy 是否懒加载
     * @return 文件类型树
     */
    List<TreeNode> getTypeListById(int fid, Boolean lazy);

    Page<Media> getFolderList(Integer fid, int current, int size, String hostPath);

    Page<Media> getFileListPage(int current, int size, Map<String, String> filter);

    List<MediaType> getTopMediaType();

    String getRealPath(String path);
}
