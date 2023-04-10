/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.mapper.FileMapper;
import com.xiaolin.mpms.mapper.FileMetaMapper;
import com.xiaolin.mpms.mapper.FileTypeMapper;
import com.xiaolin.mpms.service.FileService;
import com.xiaolin.mpms.entity.*;
import com.xiaolin.mpms.utils.MediaUtil;
import com.xiaolin.mpms.utils.ThumbUtil;
import com.xiaolin.mpms.utils.UUIDUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文件服务实现类
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-28
 */
@Service
@Log4j2
public class FileServiceImpl extends ServiceImpl<FileMapper, FileInfo> implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FileTypeMapper fileTypeMapper;

    @Autowired
    private FileMetaMapper fileMetaMapper;

    private final Comparator<FileMeta> fileMetaComparator = Comparator.comparingInt(FileMeta::getSort).reversed();
//    private final Comparator<FileMeta> fileMetaComparator = (o1, o2) -> o2.getSort() - o1.getSort();


    /**
     * 根据文件名获取文件信息
     *
     * @param serverName 服务器存储文件名
     * @return 文件信息
     */
    @Override
    public FileInfo getOneByServerName(String serverName) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_name", serverName);
        return fileMapper.selectOne(queryWrapper);
    }

    /**
     * 根据文件MD5获取文件信息
     *
     * @param md5 文件MD5
     * @return 文件信息
     */
    @Override
    public List<FileInfo> getByMd5(String md5) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        return fileMapper.selectList(queryWrapper);
    }

    /**
     * 根据文件路径获取文件列表
     *
     * @param path     文件路径
     * @param type
     * @param current  当前页
     * @param size     每页大小
     * @param hostPath 域名
     * @return 文件列表
     */
    @Override
    public MyPage<FileInfo> getFileListByPath(String path, String type, int current, int size, String hostPath) {
        int total = fileMapper.getFileTotalNumWithPath(path, type);
        if (total > 0) {
            List<FileInfo> fileList = fileMapper.getFileListByPath(path, type, (current - 1) * size, size);
            return getFileInfoPage(current, size, hostPath, total, fileList);
        }
        return new MyPage<>(current, size);
    }

    /**
     * 根据文件夹ID获取文件列表
     *
     * @param id       文件夹ID
     * @param type
     * @param current  当前页
     * @param size     每页大小
     * @param hostPath 域名
     * @return 文件列表
     */
    @Override
    public MyPage<FileInfo> getFileListByFolderId(Integer fid, String type, int current, int size, String hostPath) {
        int total = fileMapper.getFileTotalNumWithPid(fid, type);
        if (total > 0) {
            List<FileInfo> fileList = fileMapper.getFileListByFid(fid, type, (current - 1) * size, size);
            return getFileInfoPage(current, size, hostPath, total, fileList);
        }
        return new MyPage<>(current, size);
    }

    /**
     * 根据ID获取文件信息
     *
     * @param id       文件ID
     * @param hostPath 主机路径
     * @return 文件信息
     */
    @Override
    public FileInfo getFileInfo(Integer id, String hostPath) {
        FileInfo fileInfo = fileMapper.getFileById(id);
        fileInfo.setUrl(hostPath + "/file/" + fileInfo.getServerName());
        if (fileInfo.getThumb() != null) {
            fileInfo.setThumb(hostPath + "/file/" + fileInfo.getThumb());
        }
        return fileInfo;
    }

    @NotNull
    private MyPage<FileInfo> getFileInfoPage(int current, int size, String hostPath, int total, List<FileInfo> fileList) {
        fileList.forEach(fileInfo -> {
            fileInfo.setUrl(hostPath + "/file/" + fileInfo.getServerName());
            if (fileInfo.getThumb() != null) {
                fileInfo.setThumb(hostPath + "/file/" + fileInfo.getThumb());
            } else {
//                fileInfo.setThumb("http://localhost/file/3584d71fa6404b4b814cdff52cbd3cb8");
            }
        });
        size = size == 0 ? 1 : size;
        int pageNum = total % size == 0 ? total / size : total / size + 1;
        return new MyPage<>(current, pageNum, size, total, fileList);
    }

    /**
     * 根据文件夹路径获取文件夹信息
     *
     * @param path 文件夹路径
     * @return 文件夹信息
     */
    @Override
    public FileInfo getFolderInfoByPath(String path) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path", path);
        return fileMapper.selectOne(queryWrapper);
    }

    /**
     * 保存文件信息
     *
     * @param hostPath 域名
     * @param md5      文件MD5
     * @param folder   文件夹信息
     * @param title    文件标题
     * @param fileName 文件名
     * @param type     文件类型
     * @param size     文件大小
     * @param filePath 文件路径
     */

    @Async
    @Override
    public void saveFileInfo(String hostPath, String md5, FileInfo folder, String title, String fileName, String type, long size, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setTitle(title);
            fileInfo.setAlt(title);
            fileInfo.setServerName(fileName);
            fileInfo.setPath((folder.getPath() + File.separator + fileName).replace("\\", "/"));
            fileInfo.setFullPath(filePath.replace("\\", "/"));
            fileInfo.setMd5(md5);
            FileType fileType = fileTypeMapper.getFileTypeByName(type);
            if (fileType == null) {
                fileType = fileTypeMapper.getFileTypeByName("other");
            }
            if (fileType != null) {
                fileInfo.setTypeId(fileType.getId());
//                if (fileType.getParent() != null && fileType.getParent().getName().equals("image")) {
//                    fileInfo.setThumb(fileName);
//                }
            }
            fileInfo.setSize(size);
            fileInfo.setSuffix(type);
            fileInfo.setFolderId(folder.getId());
            List<FileMeta> fileMetaList = new ArrayList<>();
            if (fileType != null && fileType.getParent() != null) {
                String parentName = fileType.getParent().getName();
                switch (parentName) {
                    case "image":
                        try {
                            // 图片对象
                            BufferedImage bufferedImage = ImageIO.read(Files.newInputStream(file.toPath()));
                            // 宽度
                            int width = bufferedImage.getWidth();
                            // 高度
                            int height = bufferedImage.getHeight();
                            fileMetaList.add(new FileMeta("宽度", width));
                            fileMetaList.add(new FileMeta("高度", height));

                            String url = saveThumbInfo(type, file, width, height);
                            fileInfo.setThumb(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "video":
                        try {
                            // 视频对象
                            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file); // 创建视频抓取器
                            frameGrabber.start(); // 开始获取视频信息
                            int length = frameGrabber.getLengthInFrames(); // 获取视频总帧数
                            int i = 0;
                            Frame frame = null;
                            while (i < length) {
                                // 过滤前5帧，避免出现全黑的图片，这里假设前5帧都是全黑的
                                frame = frameGrabber.grabImage();
                                if ((i > 5) && (frame.image != null)) {
                                    break;
                                }
                                i++;
                            }
                            // 获取视频宽度
                            int width = frame.imageWidth;
                            // 获取视频高度
                            int height = frame.imageHeight;
                            // 获取视频旋转角度
                            int rotate = frameGrabber.getVideoMetadata("rotate") == null ? 0 : Integer.parseInt(frameGrabber.getVideoMetadata("rotate"));
                            // 旋转角度为90或270时，宽高互换
                            if (rotate == 90 || rotate == 270) {
                                int temp = width;
                                width = height;
                                height = temp;
                            }
                            // 获取视频时长
                            long duration = frameGrabber.getLengthInTime() / 1000000;
                            // 获取视频帧率
                            double frameRate = frameGrabber.getFrameRate();
                            // 获取视频码率
                            double videoBitrate = frameGrabber.getVideoBitrate();
                            // 获取视频编码格式
                            int videoCodec = frameGrabber.getVideoCodec();
                            // 获取音频编码格式
                            int audioCodec = frameGrabber.getAudioCodec();
                            // 获取音频码率
                            double audioBitrate = frameGrabber.getAudioBitrate();
                            // 获取音频采样率
                            int sampleRate = frameGrabber.getSampleRate();
                            // 获取音频通道数
                            int audioChannels = frameGrabber.getAudioChannels();
                            // 获取视频格式
                            String format = frameGrabber.getFormat();
                            // 获取视频文件大小
                            long fileSize = file.length();
                            // frame转视频截图
                            Java2DFrameConverter converter = new Java2DFrameConverter();
                            BufferedImage bufferedImage = converter.getBufferedImage(frame);
                            frameGrabber.stop();
                            frameGrabber.close();
                            DecimalFormat df = new DecimalFormat("0.00");
                            fileMetaList.add(new FileMeta("宽度", width));
                            fileMetaList.add(new FileMeta("高度", height));
                            fileMetaList.add(new FileMeta("视频时长", MediaUtil.transPlayTime(duration)));
                            fileMetaList.add(new FileMeta("视频帧率", df.format(frameRate) + " fps"));
                            fileMetaList.add(new FileMeta("视频码率", df.format(videoBitrate / 1000) + " kbps"));
                            fileMetaList.add(new FileMeta("视频编码格式", MediaUtil.getCodecById(videoCodec)));
                            fileMetaList.add(new FileMeta("音频编码格式", MediaUtil.getCodecById(audioCodec)));
                            fileMetaList.add(new FileMeta("音频码率", df.format(audioBitrate / 1000) + " kbps"));
                            fileMetaList.add(new FileMeta("音频采样率", sampleRate + " Hz"));
                            fileMetaList.add(new FileMeta("音频通道数", audioChannels));
                            fileMetaList.add(new FileMeta("视频格式", format));
                            File tempFile = File.createTempFile(UUIDUtil.getUUID(), ".jpg");
                            if (ImageIO.write(bufferedImage, "jpg", tempFile)) {
                                String url = saveThumbInfo("jpg", tempFile, width, height);
                                fileInfo.setThumb(url);
                                tempFile.delete();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "audio":
                        try {
                            // 音频对象
                            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file); // 创建音频抓取器
                            frameGrabber.start(); // 开始获取音频信息
                            // 获取音频时长
                            long duration = frameGrabber.getLengthInTime() / 1000000;
                            // 获取音频码率
                            double audioBitrate = frameGrabber.getAudioBitrate();
                            // 获取音频采样率
                            int sampleRate = frameGrabber.getSampleRate();
                            // 获取音频通道数
                            int audioChannels = frameGrabber.getAudioChannels();
                            // 获取音频编码格式
                            int audioCodec = frameGrabber.getAudioCodec();
                            // 获取音频格式
                            String format = frameGrabber.getFormat();
                            // 获取音频文件大小
                            long fileSize = file.length();
                            frameGrabber.stop();
                            frameGrabber.close();
                            DecimalFormat df = new DecimalFormat("0.00");
                            fileMetaList.add(new FileMeta("音频时长", MediaUtil.transPlayTime(duration)));
                            fileMetaList.add(new FileMeta("音频码率", df.format(audioBitrate / 1000) + " kbps"));
                            fileMetaList.add(new FileMeta("音频采样率", sampleRate + " Hz"));
                            fileMetaList.add(new FileMeta("音频通道数", audioChannels));
                            fileMetaList.add(new FileMeta("音频编码格式", MediaUtil.getCodecById(audioCodec)));
                            fileMetaList.add(new FileMeta("音频格式", format));
                            fileMetaList.add(new FileMeta("歌词", "", 2, 1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            fileMapper.insert(fileInfo);
            for (FileMeta fileMeta : fileMetaList) {
                fileMeta.setFid(fileInfo.getId());
                fileMetaMapper.insert(fileMeta);
            }
        }
    }

    /**
     * 根据文件ID获取文件元数据
     *
     * @param fid 文件ID
     * @return 文件元数据
     */
    @Override
    public List<FileMeta> getFileMetaByFid(int fid, int status) {
        QueryWrapper<FileMeta> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fid", fid);
        if (status != 0) {
            queryWrapper.eq("status", status);
        }
        List<FileMeta> list = fileMetaMapper.selectList(queryWrapper);
        list.sort(fileMetaComparator);
        return list;
    }

    /**
     * 根据文件ID删除文件
     *
     * @param id 文件ID
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteFileById(int id) {
        FileInfo fileInfo = fileMapper.selectById(id);
        if (fileInfo == null) {
            return false;
        }
        String path = fileInfo.getPath();
        String filePath = new File("").getAbsolutePath() + File.separator + "upload" + path.replace("/", File.separator) + (fileInfo.getIsDir() ? "" : "." + fileInfo.getSuffix());
        File file = new File(filePath);
        if (fileInfo.getIsDir()) {
            File[] list = file.listFiles();
            System.out.println(Arrays.toString(list));
            if (list != null && list.length > 0)
                throw new RuntimeException("删除失败，文件夹不为空");
        }
        if (fileInfo.getThumb() != null && !fileInfo.getIsDir()) {
            String thumb = fileInfo.getThumb().substring(fileInfo.getThumb().lastIndexOf("/") + 1);
            FileInfo thumbInfo = getOneByServerName(thumb);
            if (thumbInfo != null) {
                String thumbPath = new File("").getAbsolutePath() + File.separator + "upload" + thumbInfo.getPath().replace("/", File.separator) + "." + thumbInfo.getSuffix();
                File thumbFile = new File(thumbPath);
                fileMapper.deleteById(thumbInfo.getId());
                if (thumbFile.exists()) {
                    thumbFile.delete();
                }
            }
        }
        fileMapper.deleteById(id);
        if (file.exists()) {
            file.delete();
            return true;
        }
        throw new RuntimeException("删除失败，文件不存在");
    }

    /**
     * 根据文件ID列表删除文件
     *
     * @param ids 文件ID列表
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFileByIds(List<Integer> ids) {
        int count = 0;
        for (Integer id : ids) {
            try {
                if (deleteFileById(id))
                    count++;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return count == ids.size();
    }

    /**
     * 根据文件ID更新文件信息
     *
     * @param id       文件ID
     * @param fileInfo 文件信息
     * @return 是否更新成功
     */
    @Override
    public boolean updateFileInfoById(int id, FileInfo fileInfo) {
        fileInfo.setId(id);
        String thumb = fileInfo.getThumb();
        fileInfo.setThumb(thumb.substring(thumb.lastIndexOf("/") + 1));
        if (fileInfo.getMeta() != null) {
            fileInfo.getMeta().forEach(fileMeta -> {
                fileMeta.setValue(JSON.toJSONString(fileMeta.getValue()));
                fileMeta.setFid(id);
                if (fileMeta.getStatus() == 3) {
                    fileMeta.setStatus(2);
                    fileMeta.setSort(1);
                    fileMeta.setId(null);
                    fileMetaMapper.insert(fileMeta);
                } else if (fileMeta.getStatus() == 2) {
                    fileMetaMapper.updateById(fileMeta);
                }
            });
        }
        return fileMapper.updateById(fileInfo) > 0;
    }

    @Override
    public Boolean deleteFileMetaById(int id) {
        fileMetaMapper.deleteById(id);
        return true;
    }

    /**
     * 创建文件夹
     *
     * @param title    文件夹名称
     * @param folderId 父级文件夹ID
     * @return 文件夹是否创建成功
     */
    @Override
    public Boolean createFolder(String title, int folderId) {
        String path = File.separator + title;
        String fullPath = "";
        try {
            fullPath = new File("").getCanonicalPath();
        } catch (IOException e) {
            fullPath = new File("").getAbsolutePath();
        }
        fullPath += File.separator + "upload";
        if (folderId != 0) {
            FileInfo folder = fileMapper.selectById(folderId);
            path = folder.getPath() + path;
        }
        path = path.replace("/", File.separator);
        fullPath += path;
        File folder = new File(fullPath);
        if (folder.exists()) {
            throw new RuntimeException("创建失败，文件夹已存在");
        }
        try {
            if (!folder.mkdirs()) {
                throw new RuntimeException("创建失败，未知异常");
            }
        } catch (Exception e) {
            throw new RuntimeException("创建失败，未知异常", e);
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setTitle(title);
        fileInfo.setAlt(title);
        fileInfo.setServerName(title);
        fileInfo.setPath(path.replace("\\", "/"));
        fileInfo.setFullPath(fullPath.replace("\\", "/"));
        fileInfo.setFolderId(folderId);
        fileInfo.setThumb("http://localhost/file/311a095245a847e8940b2b972225ca75");
        fileInfo.setTypeId(8);
        fileInfo.setIsDir(true);
        if (fileMapper.insert(fileInfo) <= 0)
            throw new RuntimeException("文件信息插入失败");
        return true;
    }

    /**
     * 根据ID获取文件类型树
     *
     * @param pid  文件类型父级ID
     * @param lazy 是否懒加载
     * @return 文件类型树
     */
    @Override
    public List<TreeNode> getTypeListById(int pid, Boolean lazy) {
        QueryWrapper<FileType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        if (lazy) {
            queryWrapper.eq("pid", pid);
            List<FileType> fileTypeList = fileTypeMapper.selectList(queryWrapper);
            List<TreeNode> treeNodeList = new ArrayList<>();
            for (FileType fileType : fileTypeList) {
                treeNodeList.add(new TreeNode(fileType.getId(), fileType.getPid(), fileType.getTitle(), fileType.getId()));
            }
            return treeNodeList;
        } else {
            List<FileType> fileTypeList = fileTypeMapper.selectList(queryWrapper);
            List<TreeNode> treeNodeList = new ArrayList<>();
            for (FileType fileType : fileTypeList) {
                treeNodeList.add(new TreeNode(fileType.getId(), fileType.getPid(), fileType.getTitle(), fileType.getId(), true));
            }
            return getTreeList(treeNodeList, 0);
        }
    }

    /**
     * 递归生成树形数组
     *
     * @param list 平行数组
     * @param pid  父级ID
     * @return 树形数组
     */
    private List<TreeNode> getTreeList(List<TreeNode> list, int pid) {
        return list.stream()
                .filter(item -> item != null && item.getPid() == pid)
                .peek(item -> {
                    item.setChildren(getTreeList(list, item.getId()));
                    if (item.getChildren().size() > 0)
                        item.setIsLeaf(false);
                })
                .collect(Collectors.toList());
    }


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
    @NotNull
    public String saveThumbInfo(String type, File file, int width, int height) throws IOException {
        //创建临时文件
        File markedPhoto = File.createTempFile(UUIDUtil.getUUID(), "." + type);
        String thumbTitle = UUIDUtil.getUUID();
        FileInfo thumbFolder = getFolderInfoByPath("/系统文件/缩略图");
        String thumbPath = thumbFolder.getPath().replace("/", File.separator) + File.separator;
        File directory = new File(new File("").getCanonicalFile() + File.separator + "upload" + thumbPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File smallerPhoto = new File(directory, thumbTitle + "." + type);
        //加水印 缩图
        int waterWidth = 400;
        int waterHeight = waterWidth * height / width;
        ThumbUtil.createThumb(file, smallerPhoto, waterWidth, waterHeight);
        // 保存缩略图信息
        FileInfo thumbFileInfo = new FileInfo();
        thumbFileInfo.setTitle(thumbTitle);
        thumbFileInfo.setAlt(thumbTitle);
        thumbFileInfo.setServerName(thumbTitle);
        thumbPath += thumbFileInfo.getServerName();
        thumbFileInfo.setPath(thumbPath.replace("\\", "/"));
        thumbFileInfo.setFullPath(smallerPhoto.getAbsolutePath().replace("\\", "/"));
        String url = thumbFileInfo.getServerName();
        thumbFileInfo.setThumb(url);
        thumbFileInfo.setSize(smallerPhoto.length());
        thumbFileInfo.setSuffix(type);
        thumbFileInfo.setFolderId(thumbFolder.getId());
        thumbFileInfo.setMd5(DigestUtils.md5Hex(Files.newInputStream(smallerPhoto.toPath())));
        FileType fileType = fileTypeMapper.getFileTypeByName(type);
        thumbFileInfo.setType(fileType);
        fileMapper.insert(thumbFileInfo);
        return url;
    }

    @Override
    public Page<FileInfo> getFolderList(Integer fid, int current, int size, String hostPath) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        if (fid != null) {
            queryWrapper.eq("folder_id", fid);
        }
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_enable", 1);
        queryWrapper.eq("is_dir", 1);
        Page<FileInfo> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        Page<FileInfo> result = fileMapper.selectPage(page, queryWrapper);
        for (FileInfo fileInfo : result.getRecords()) {
            if (fileInfo.getThumb() != null) {
                fileInfo.setThumb(hostPath + "/file/" + fileInfo.getThumb());
            }
        }
        return result;
    }
}