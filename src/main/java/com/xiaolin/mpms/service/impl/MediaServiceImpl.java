/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.mpms.entity.media.Media;
import com.xiaolin.mpms.entity.media.MediaMeta;
import com.xiaolin.mpms.entity.media.MediaType;
import com.xiaolin.mpms.mapper.MediaMapper;
import com.xiaolin.mpms.mapper.MediaMetaMapper;
import com.xiaolin.mpms.mapper.MediaTypeMapper;
import com.xiaolin.mpms.service.MediaService;
import com.xiaolin.mpms.entity.*;
import com.xiaolin.mpms.utils.MediaUtil;
import com.xiaolin.mpms.utils.ServletUtils;
import com.xiaolin.mpms.utils.ThumbUtil;
import com.xiaolin.mpms.utils.UUIDUtil;
import lombok.extern.log4j.Log4j2;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private MediaTypeMapper mediaTypeMapper;

    @Autowired
    private MediaMetaMapper mediaMetaMapper;

    private final Comparator<MediaMeta> fileMetaComparator = Comparator.comparingInt(MediaMeta::getOrder).reversed();
//    private final Comparator<FileMeta> fileMetaComparator = (o1, o2) -> o2.getSort() - o1.getSort();


    /**
     * 根据文件名获取文件信息
     *
     * @param serverName 服务器存储文件名
     * @return 文件信息
     */
    @Override
    public Media getOneByServerName(String serverName) {
        QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_name", serverName);
        return mediaMapper.selectOne(queryWrapper);
    }

    /**
     * 根据文件MD5获取文件信息
     *
     * @param md5 文件MD5
     * @return 文件信息
     */
    @Override
    public List<Media> getByMd5(String md5) {
        QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        return mediaMapper.selectList(queryWrapper);
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
    public MyPage<Media> getFileListByPath(String path, String type, int current, int size, String hostPath) {
        int total = mediaMapper.getFileTotalNumWithPath(path, type);
        if (total > 0) {
            List<Media> fileList = mediaMapper.getFileListByPath(path, type, (current - 1) * size, size);
            return getFileInfoPage(current, size, hostPath, total, fileList);
        }
        return new MyPage<>(current, size);
    }

    /**
     * 根据文件夹ID获取文件列表
     *
     * @param fid      文件夹ID
     * @param type     类型
     * @param current  当前页
     * @param size     每页大小
     * @param hostPath 域名
     * @return 文件列表
     */
    @Override
    public MyPage<Media> getFileListByFolderId(Integer fid, String type, int current, int size, String hostPath) {
        int total = mediaMapper.getFileTotalNumWithPid(fid, type);
        if (total > 0) {
            List<Media> fileList = mediaMapper.getFileListByFid(fid, type, (current - 1) * size, size);
            return getFileInfoPage(current, size, hostPath, total, fileList);
        }
        return new MyPage<>(current, size);
    }

    /**
     * 根据ID获取文件信息
     *
     * @param id 文件ID
     * @return 文件信息
     */
    @Override
    public Media getFileInfo(Integer id) {
        Media media = mediaMapper.selectById(id);
        media.setUrl(ServletUtils.getHostPath() + media.getPath());
        LambdaQueryWrapper<MediaMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MediaMeta::getMid, id);
        media.setMeta(mediaMetaMapper.selectList(queryWrapper));
        return media;
    }

    @NotNull
    private MyPage<Media> getFileInfoPage(int current, int size, String hostPath, int total, List<Media> fileList) {
        fileList.forEach(media -> {
            media.setUrl(hostPath + "/file/" + media.getServerName());
            if (media.getThumb() != null) {
                media.setThumb(hostPath + "/file/" + media.getThumb());
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
    public Media getFolderInfoByPath(String path) {
        QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path", path);
        return mediaMapper.selectOne(queryWrapper);
    }

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


    @Async
    @Override
    public void saveFileInfo(String uploadPath, String title, String randomId, String type, long size, String filePath, Boolean isThumb) {
        File file = new File(filePath);
        System.out.println(file.exists());
        if (file.exists()) {
            Media media = new Media();
            media.setTitle(title);
            media.setAlt(title);
            media.setServerName(randomId);
            media.setPath("/upload/" + randomId + "." + type);
            media.setFullPath(filePath);
            MediaType mediaType = mediaTypeMapper.getMediaTypeByName(type);
            if (mediaType == null) {
                mediaType = mediaTypeMapper.getMediaTypeByName("other");
            }
            media.setType(mediaType.getParent().getName());
            media.setSize(size);
            media.setSuffix(type);
            mediaMapper.insert(media);
            if (!isThumb) {
                List<MediaMeta> mediaMetaList = new ArrayList<>();
                if (mediaType.getParent() != null) {
                    String thumbUrl = null;
                    String parentName = mediaType.getParent().getName();
                    switch (parentName) {
                        case "image":
                            try {
                                // 图片对象
                                BufferedImage bufferedImage = ImageIO.read(Files.newInputStream(file.toPath()));
                                // 宽度
                                int width = bufferedImage.getWidth();
                                // 高度
                                int height = bufferedImage.getHeight();
                                mediaMetaList.add(new MediaMeta("宽度", width));
                                mediaMetaList.add(new MediaMeta("高度", height));
                                thumbUrl = saveThumbInfo(uploadPath, "jpg", file, width, height);
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
                                mediaMetaList.add(new MediaMeta("宽度", width));
                                mediaMetaList.add(new MediaMeta("高度", height));
                                mediaMetaList.add(new MediaMeta("视频时长", MediaUtil.transPlayTime(duration)));
                                mediaMetaList.add(new MediaMeta("视频帧率", df.format(frameRate) + " fps"));
                                mediaMetaList.add(new MediaMeta("视频码率", df.format(videoBitrate / 1000) + " kbps"));
                                mediaMetaList.add(new MediaMeta("视频编码格式", MediaUtil.getCodecById(videoCodec)));
                                mediaMetaList.add(new MediaMeta("音频编码格式", MediaUtil.getCodecById(audioCodec)));
                                mediaMetaList.add(new MediaMeta("音频码率", df.format(audioBitrate / 1000) + " kbps"));
                                mediaMetaList.add(new MediaMeta("音频采样率", sampleRate + " Hz"));
                                mediaMetaList.add(new MediaMeta("音频通道数", audioChannels));
                                mediaMetaList.add(new MediaMeta("视频格式", format));
                                File tempFile = File.createTempFile(UUIDUtil.getUUID(), ".jpg");
                                if (ImageIO.write(bufferedImage, "jpg", tempFile)) {
                                    thumbUrl = saveThumbInfo(uploadPath, "jpg", tempFile, width, height);
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
                                mediaMetaList.add(new MediaMeta("音频时长", MediaUtil.transPlayTime(duration)));
                                mediaMetaList.add(new MediaMeta("音频码率", df.format(audioBitrate / 1000) + " kbps"));
                                mediaMetaList.add(new MediaMeta("音频采样率", sampleRate + " Hz"));
                                mediaMetaList.add(new MediaMeta("音频通道数", audioChannels));
                                mediaMetaList.add(new MediaMeta("音频编码格式", MediaUtil.getCodecById(audioCodec)));
                                mediaMetaList.add(new MediaMeta("音频格式", format));
                                mediaMetaList.add(new MediaMeta("歌词", "", 2, 1));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    Media temp = new Media();
                    temp.setId(media.getId());
                    temp.setThumb(thumbUrl);
                    updateById(temp);
                }
                for (MediaMeta mediaMeta : mediaMetaList) {
                    mediaMeta.setMid(media.getId());
                    mediaMetaMapper.insert(mediaMeta);
                }
            }
        }
    }


    /**
     * 创建缩略图并保存缩略图信息
     *
     * @param uploadPath 上传路径
     * @param type       类型
     * @param file       文件
     * @param width      源文件宽度
     * @param height     源文件高度
     * @return 缩略图URL
     * @throws IOException IO异常
     */
    @NotNull
    public String saveThumbInfo(String uploadPath, String type, File file, int width, int height) {
        //创建临时文件
        String thumbTitle = UUIDUtil.getUUID();
        String thumbPath = uploadPath + File.separator + "thumb" + File.separator;
        File directory = new File(thumbPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File smallerPhoto = new File(directory, thumbTitle + "." + type);
        //加水印 缩图
        int waterWidth = 400;
        int waterHeight = waterWidth * height / width;
        ThumbUtil.createThumb(file, smallerPhoto, waterWidth, waterHeight);
        // 保存缩略图信息
        return "/upload/thumb/" + thumbTitle + "." + type;
    }

    /**
     * 根据文件ID获取文件元数据
     *
     * @param mid 文件ID
     * @return 文件元数据
     */
    @Override
    public List<MediaMeta> getFileMetaByFid(int mid, int status) {
        LambdaQueryWrapper<MediaMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MediaMeta::getMid, mid);
        if (status != 0) {
            queryWrapper.eq(MediaMeta::getStatus, status);
        }
        List<MediaMeta> list = mediaMetaMapper.selectList(queryWrapper);
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
        Media media = mediaMapper.selectById(id);
        if (media == null) {
            return false;
        }
        String path = media.getPath();
        String filePath = new File("").getAbsolutePath() + File.separator + "upload" + path.replace("/", File.separator) + "." + media.getSuffix();
        File file = new File(filePath);
        if (media.getThumb() != null) {
            String thumb = media.getThumb().substring(media.getThumb().lastIndexOf("/") + 1);
            Media thumbInfo = getOneByServerName(thumb);
            if (thumbInfo != null) {
                String thumbPath = new File("").getAbsolutePath() + File.separator + "upload" + thumbInfo.getPath().replace("/", File.separator) + "." + thumbInfo.getSuffix();
                File thumbFile = new File(thumbPath);
                mediaMapper.deleteById(thumbInfo.getId());
                if (thumbFile.exists()) {
                    thumbFile.delete();
                }
            }
        }
        mediaMapper.deleteById(id);
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
     * @param id    文件ID
     * @param media 文件信息
     * @return 是否更新成功
     */
    @Override
    public boolean updateFileInfoById(int id, Media media) {
        media.setId(id);
        String thumb = media.getThumb();
        media.setThumb(thumb.substring(thumb.lastIndexOf("/") + 1));
        if (media.getMeta() != null) {
            media.getMeta().forEach(fileMeta -> {
                fileMeta.setValue(JSON.toJSONString(fileMeta.getValue()));
                fileMeta.setMid(id);
                if (fileMeta.getStatus() == 3) {
                    fileMeta.setStatus(2);
                    fileMeta.setOrder(1);
                    fileMeta.setId(null);
                    mediaMetaMapper.insert(fileMeta);
                } else if (fileMeta.getStatus() == 2) {
                    mediaMetaMapper.updateById(fileMeta);
                }
            });
        }
        return mediaMapper.updateById(media) > 0;
    }

    @Override
    public Boolean deleteFileMetaById(int id) {
        mediaMetaMapper.deleteById(id);
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
            Media folder = mediaMapper.selectById(folderId);
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
        Media media = new Media();
        media.setTitle(title);
        media.setAlt(title);
        media.setServerName(title);
        media.setPath(path.replace("\\", "/"));
        media.setFullPath(fullPath.replace("\\", "/"));
        media.setThumb("http://localhost/file/311a095245a847e8940b2b972225ca75");
        media.setType("image");
        if (mediaMapper.insert(media) <= 0)
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
        QueryWrapper<MediaType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        if (lazy) {
            queryWrapper.eq("pid", pid);
            List<MediaType> mediaTypeList = mediaTypeMapper.selectList(queryWrapper);
            List<TreeNode> treeNodeList = new ArrayList<>();
            for (MediaType mediaType : mediaTypeList) {
                treeNodeList.add(new TreeNode(mediaType.getId(), mediaType.getPid(), mediaType.getTitle(), mediaType.getId()));
            }
            return treeNodeList;
        } else {
            List<MediaType> mediaTypeList = mediaTypeMapper.selectList(queryWrapper);
            List<TreeNode> treeNodeList = new ArrayList<>();
            for (MediaType mediaType : mediaTypeList) {
                treeNodeList.add(new TreeNode(mediaType.getId(), mediaType.getPid(), mediaType.getTitle(), mediaType.getId(), true));
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

    @Override
    public Page<Media> getFolderList(Integer fid, int current, int size, String hostPath) {
        QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
        if (fid != null) {
            queryWrapper.eq("folder_id", fid);
        }
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_enable", 1);
        queryWrapper.eq("is_dir", 1);
        Page<Media> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        Page<Media> result = mediaMapper.selectPage(page, queryWrapper);
        for (Media media : result.getRecords()) {
            if (media.getThumb() != null) {
                media.setThumb(hostPath + "/file/" + media.getThumb());
            }
        }
        return result;
    }

    @Override
    public Page<Media> getFileListPage(int current, int size, Map<String, String> filter) {
        String cid = filter.getOrDefault("cid", "");
        String type = filter.getOrDefault("type", "");
        String keyword = filter.getOrDefault("keyword", "");
        LambdaQueryWrapper<Media> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Media::getTitle, keyword);
        if (!cid.equals("")) {
            queryWrapper.eq(Media::getCid, cid);
        }
        if (!type.equals("")) {
            queryWrapper.eq(Media::getType, type);
        }
        Page<Media> page = new Page<>(current, size);
        page(page, queryWrapper);
        for (Media record : page.getRecords()) {
            record.setUrl(ServletUtils.getHostPath() + record.getPath());
        }
        return page;
    }

    @Override
    public List<MediaType> getTopMediaType() {
        LambdaQueryWrapper<MediaType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MediaType::getPid, 0);
        return mediaTypeMapper.selectList(queryWrapper);
    }

    @Override
    public String getRealPath(String path) {
        LambdaQueryWrapper<Media> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Media::getPath, path);
        Media one = getOne(queryWrapper);
        if (one == null) {
            return "";
        }
        return one.getFullPath();
    }
}