/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.entity.VO.ResultVO;
import com.xiaolin.mpms.entity.media.Media;
import com.xiaolin.mpms.entity.media.MediaMeta;
import com.xiaolin.mpms.service.MediaService;
import com.xiaolin.mpms.entity.*;
import com.xiaolin.mpms.utils.FileUtil;
import com.xiaolin.mpms.utils.ServletUtils;
import com.xiaolin.mpms.utils.UUIDUtil;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 媒体控制器
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-26
 */
@EnableAsync
@Api(tags = "媒体管理")
@CrossOrigin
@RestController
@RequestMapping("/media")
@Log4j2
public class MediaController {

    //    @Value("${files.upload.path}")
    private String uploadPath;

    @Autowired
    private MediaService mediaService;

    public MediaController() {
        // 获取resources路径
//        File directory = new File("src/main/resources");
//        try {
//            uploadPath = directory.getCanonicalPath() + File.separator + "upload" + File.separator;
//        } catch (IOException e) {
//            uploadPath = directory.getAbsolutePath() + File.separator + "upload" + File.separator;
//        }
        File directory = new File("");
        try {
            uploadPath = directory.getCanonicalPath() + File.separator + "upload" + File.separator;
        } catch (IOException e) {
            uploadPath = directory.getAbsolutePath() + File.separator + "upload" + File.separator;
        }
        File uploadFolder = new File(uploadPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }

    /**
     * 上传媒体
     *
     * @param file 媒体
     * @return ResultVO
     */
    @PostMapping("upload")
    @ApiOperation(value = "上传媒体", notes = "上传媒体", httpMethod = "POST", produces = "application/json", consumes = "multipart/form-data")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "媒体", required = true, dataType = "file", paramType = "form"), @ApiImplicitParam(name = "isThumb", value = "是否缩略图", required = true, dataType = "boolean", paramType = "form")})
    @ApiResponses({@ApiResponse(code = 200, message = "媒体保存成功"), @ApiResponse(code = 60001, message = "存储路径不存在"), @ApiResponse(code = 60002, message = "存储路径异常"), @ApiResponse(code = 60003, message = "媒体保存失败"),})
    public ResultVO<String> upload(@RequestParam MultipartFile file, @RequestParam(defaultValue = "false") Boolean isThumb) {
        String originalFilename = file.getOriginalFilename();
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        long size = file.getSize();
        String randomId = UUIDUtil.getUUID();
        String filename = randomId + "." + type;
        String filePath = uploadPath + File.separator + filename;
        File uploadFile = new File(filePath);
        try {
            file.transferTo(uploadFile);
            mediaService.saveFileInfo(uploadPath, title, randomId, type, size, filePath, isThumb);
            return ResultVO.success("媒体保存成功", "/upload/" + filename);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return ResultVO.error(60003, "媒体保存失败");
        }
    }

    @GetMapping("download/{fileName}")
    @ApiOperation(value = "下载媒体", notes = "下载媒体", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileName", value = "媒体名", required = true, dataType = "string", paramType = "path")})
    public void downloadFile(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Media media = mediaService.getOneByServerName(fileName);
        if (media == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(404);
            response.getWriter().println(JSON.toJSONString(ResultVO.error(60003, "媒体信息不存在")));
            return;
        }
        String path = (uploadPath + media.getPath() + "." + media.getSuffix()).replace("/", File.separator);
        String range = request.getHeader("Range");
        response.setContentType("application/octet-stream;charset=utf-8");
        if (range != null && range.startsWith("bytes=")) {
            fileChunkDownload(path, media.getTitle(), request, response);
        } else {
            File file = new File(path.replace("/", File.separator));
            if (!file.exists()) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(404);
                response.getWriter().println(JSON.toJSONString(ResultVO.error(60003, "媒体不存在")));
                return;
            }
            ServletOutputStream outputStream = response.getOutputStream();
            response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
            byte[] bytes = new byte[1024 * 1024];
            while (bufferedInputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            bufferedInputStream.close();
            outputStream.close();
        }

    }

    /**
     * 媒体支持分块下载和断点续传
     *
     * @param filePath 媒体完整路径
     * @param request  请求
     * @param response 响应
     */
    private void fileChunkDownload(String filePath, String fileName, HttpServletRequest request, HttpServletResponse response) {
        String range = request.getHeader("Range");
//        log.info("当前请求范围:" + range);
        File file = new File(filePath);
        //开始下载位置
        long startByte = 0;
        //结束下载位置

        long endByte = file.length() - 1;
        log.info("媒体开始位置：{}，媒体结束位置：{}，媒体总长度：{}({})", startByte, endByte, file.length(), FileUtil.fileSizeByteToM(file.length()));

        //有range的话
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String[] ranges = range.split("-");
            try {
                //判断range的类型
                if (ranges.length == 1) {
                    //类型一：bytes=-2343
                    if (range.startsWith("-")) {
                        endByte = Long.parseLong(ranges[0]);
                    }
                    //类型二：bytes=2343-
                    else if (range.endsWith("-")) {
                        startByte = Long.parseLong(ranges[0]);
                    }
                }
                //类型三：bytes=22-2343
                else if (ranges.length == 2) {
                    startByte = Long.parseLong(ranges[0]);
                    endByte = Long.parseLong(ranges[1]);
                }

            } catch (NumberFormatException e) {
                startByte = 0;
                endByte = file.length() - 1;
                log.error("范围发生错误：{}", e.getLocalizedMessage());
            }
        }

        //要下载的长度
        long contentLength = endByte - startByte + 1;

        //媒体类型
        String contentType = request.getServletContext().getMimeType(fileName);

        // 解决下载媒体时媒体名乱码问题
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);

        //各种响应头设置
        //支持断点续传，获取部分字节内容：
        response.setHeader("Accept-Ranges", "bytes");
        //http状态码要为206：表示获取部分内容
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        //inline表示浏览器直接使用，attachment表示下载，fileName表示下载的媒体名
        response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(contentLength));
        // Content-Range，格式为：[要下载的开始位置]-[结束位置]/[媒体总大小]
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());

        BufferedOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        //已传送数据大小
        long transmitted = 0;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024];
            int len = 0;
            randomAccessFile.seek(startByte);
            // 判断是否到了最后不足1024（buff的length）个byte这个逻辑（(transmitted + len) <= contentLength）要放前面
            // 不然会会先读取randomAccessFile，造成后面读取位置出错，找了一天才发现问题所在
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
                transmitted += len;
            }
            //处理不足buff.length部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }

            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            log.info("下载完毕：" + startByte + "-" + endByte + "：" + transmitted);
        } catch (ClientAbortException e) {
            log.warn("用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
            //捕获此异常表示用户停止下载
        } catch (IOException e) {
            e.printStackTrace();
            log.error("用户下载异常：{}", e.getLocalizedMessage());
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取媒体列表
     *
     * @param current 当前页
     * @param size    每页条数
     * @param filter  过滤条件
     * @return 媒体列表
     */
    @GetMapping("list/page")
    @ApiOperation(value = "获取媒体列表", notes = "获取媒体列表", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "current", value = "当前页", defaultValue = "1", dataType = "int", paramType = "query"), @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "5", dataType = "int", paramType = "query"), @ApiImplicitParam(name = "filter", value = "过滤条件", defaultValue = "5", dataType = "Map", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "获取媒体列表成功")})
    public ResultVO<Page<Media>> getListPage(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Map<String, String> filter) {
        return ResultVO.success("获取媒体列表成功", mediaService.getFileListPage(current, size, filter));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获取媒体信息", notes = "获取媒体信息", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "媒体ID", dataType = "int", paramType = "path")})
    @ApiResponses({@ApiResponse(code = 200, message = "获取媒体信息成功")})
    public ResultVO<Media> getFileInfoById(@PathVariable Integer id) {
        if (id != null)
            return ResultVO.success("获取媒体信息成功", mediaService.getFileInfo(id));
        throw new NullPointerException();
    }

    // 获取媒体元数据
    @GetMapping("meta")
    @ApiOperation(value = "获取媒体元数据", notes = "获取媒体元数据", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "mid", value = "媒体ID", required = true, dataType = "int", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "获取媒体元数据成功")})
    public ResultVO<List<MediaMeta>> getMeta(@RequestParam int mid, @RequestParam(defaultValue = "0") int status) {
        List<MediaMeta> mediaMetaList = mediaService.getFileMetaByFid(mid, status);
        return ResultVO.success("获取媒体元数据成功", mediaMetaList);
    }

    @GetMapping("type")
    public ResultVO<List<TreeNode>> getFileTypeList(@RequestParam(defaultValue = "0") int fid, @RequestParam(defaultValue = "false") Boolean lazy) {
        try {
            List<TreeNode> list = mediaService.getTypeListById(fid, lazy);
            return ResultVO.success("数据获取成功", list);
        } catch (Exception e) {
            return ResultVO.error(60006, e.getMessage());
        }
    }

    // 删除元数据
    @DeleteMapping("meta/{id}")
    @ApiOperation(value = "删除媒体元数据", notes = "删除媒体元数据", httpMethod = "DELETE", produces = "application/json")
    @ApiImplicitParams({

    })
    @ApiResponses({@ApiResponse(code = 200, message = "删除媒体元数据成功")})
    public ResultVO<String> deleteMeta(@PathVariable("id") int id) {
        mediaService.deleteFileMetaById(id);
        return ResultVO.success("删除媒体元数据成功");
    }


    @PutMapping("{id}")
    @ApiOperation(value = "更新媒体信息", notes = "更新媒体信息", httpMethod = "PUT", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "媒体ID", required = true, dataType = "int", paramType = "path"), @ApiImplicitParam(name = "fileInfo", value = "媒体信息", required = true, dataType = "FileInfo", paramType = "body")})
    @ApiResponses({@ApiResponse(code = 200, message = "更新媒体信息成功"), @ApiResponse(code = 60003, message = "更新媒体信息失败，媒体不存在")})
    public ResultVO<String> updateFileInfo(@PathVariable int id, @RequestBody Media media) {
        if (mediaService.updateFileInfoById(id, media)) return ResultVO.success("更新媒体信息成功");
        return ResultVO.error(60003, "更新媒体信息失败，媒体不存在");
    }


    @DeleteMapping("{id}")
    @ApiOperation(value = "删除媒体", notes = "删除媒体", httpMethod = "DELETE", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "媒体ID", required = true, dataType = "int", paramType = "path")})
    @ApiResponses({@ApiResponse(code = 200, message = "删除媒体成功")})
    public ResultVO<String> delete(@PathVariable int id) {
        try {
            mediaService.deleteFileById(id);
        } catch (Exception e) {
            return ResultVO.error(6005, e.getMessage());
        }
        return ResultVO.success("删除媒体成功");
    }

    @DeleteMapping
    @ApiOperation(value = "批量删除媒体", notes = "删除媒体", httpMethod = "DELETE", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "媒体ID", required = true, dataType = "int", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "删除媒体成功")})
    public ResultVO<String> delete(@RequestBody List<Integer> ids) {
        if (mediaService.deleteFileByIds(ids)) return ResultVO.success("删除媒体成功");
        return ResultVO.error(60004, "部分媒体删除失败，媒体不存在");
    }

}
