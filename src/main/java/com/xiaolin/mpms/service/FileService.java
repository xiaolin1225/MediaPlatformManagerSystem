/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.mpms.entity.FileInfo;
import com.xiaolin.mpms.entity.FileMeta;
import com.xiaolin.mpms.entity.MyPage;
import com.xiaolin.mpms.entity.TreeNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 文件服务层
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
public interface FileService extends IService<FileInfo> {

    /**
     * 根据文件名获取文件信息
     *
     * @param serverName 服务器存储文件名
     * @return 文件信息
     */
    FileInfo getOneByServerName(String serverName);

    /**
     * 根据文件MD5获取文件信息
     *
     * @param md5 文件MD5
     * @return 文件信息
     */
    List<FileInfo> getByMd5(String md5);

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
    MyPage<FileInfo> getFileListByPath(String path, String type, int current, int size, String hostPath);

    /**
     * 根据文件夹ID获取文件列表
     *
     * @param id       文件夹ID
     * @param type
     * @param current  当前页
     * @param size     每页大小
     * @param hostPath 主机路径
     * @return 文件列表
     */
    MyPage<FileInfo> getFileListByFolderId(Integer fid, String type, int current, int size, String hostPath);

    /**
     * 根据ID获取文件信息
     *
     * @param id       文件ID
     * @param hostPath 主机路径
     * @return 文件信息
     */
    FileInfo getFileInfo(Integer id, String hostPath);

    /**
     * 根据文件路径获取文件夹信息
     *
     * @param path 文件路径
     * @return 文件夹信息
     */
    FileInfo getFolderInfoByPath(String path);

    /**
     * 保存文件信息
     *
     * @param hostPath 主机路径
     * @param md5      文件MD5
     * @param folder   文件夹信息
     * @param title    文件标题
     * @param fileName 文件名
     * @param type     文件类型
     * @param size     文件大小
     * @param filePath 文件路径
     */
    void saveFileInfo(String hostPath, String md5, FileInfo folder, String title, String fileName, String type, long size, String filePath);

    /**
     * 根据文件ID获取文件元数据
     *
     * @param fid    文件ID
     * @param status 文件状态
     */
    List<FileMeta> getFileMetaByFid(int fid, int status);

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
     * @param fileInfo 文件信息
     * @return 是否更新成功
     */
    boolean updateFileInfoById(int id, FileInfo fileInfo);

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

    /**
     * 创建缩略图并保存缩略图信息
     *
     * @param type   类型
     * @param file   文件
     * @param width  源文件宽度
     * @param height 源文件高度
     * @return 缩略图URL
     * @throws IOException IO异常
     */
    String saveThumbInfo(String type, File file, int width, int height) throws IOException;

    Page<FileInfo> getFolderList(Integer fid, int current, int size, String hostPath);
}
