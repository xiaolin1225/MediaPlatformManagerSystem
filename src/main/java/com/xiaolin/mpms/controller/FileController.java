/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.mpms.service.FileService;
import com.xiaolin.mpms.entity.*;
import com.xiaolin.mpms.utils.FileUtil;
import com.xiaolin.mpms.utils.UUIDUtil;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件控制器
 * </p>
 *
 * @author xiaolin
 * @since 2023-02-26
 */
@EnableAsync
@Api(tags = "文件管理")
@CrossOrigin
@RestController
@RequestMapping("/file")
@Log4j2
public class FileController {

    //    @Value("${files.upload.path}")
    private String uploadPath;

    @Autowired
    private FileService fileService;

    public FileController() {
        try {
            uploadPath = new File("").getCanonicalPath() + File.separator + "upload";
        } catch (IOException e) {
            uploadPath = new File("").getAbsolutePath() + File.separator + "upload";
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return ResultVO
     */
    @PostMapping("upload")
    @ApiOperation(value = "上传文件", notes = "上传文件", httpMethod = "POST", produces = "application/json", consumes = "multipart/form-data")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file", paramType = "form"), @ApiImplicitParam(name = "folderId", value = "存储路径", required = true, dataType = "int", paramType = "form"), @ApiImplicitParam(name = "md5", value = "文件MD5", required = true, dataType = "string", paramType = "form")})
    @ApiResponses({@ApiResponse(code = 200, message = "文件保存成功"), @ApiResponse(code = 60001, message = "存储路径不存在"), @ApiResponse(code = 60002, message = "存储路径异常"), @ApiResponse(code = 60003, message = "文件保存失败"),})
    public ResultVO<String> upload(@RequestParam MultipartFile file, @RequestParam(defaultValue = "3") int folderId, @RequestParam String md5, HttpServletRequest request) {
        // 判断该目录下相同MD5文件是否存在
        List<FileInfo> md5List = fileService.getByMd5(md5);
        if (!md5List.isEmpty()) {
            FileInfo sameMd5File = md5List.get(0);
            if (sameMd5File.getFolderId() == folderId) {
                sameMd5File.setId(null);
                sameMd5File.setFolderId(folderId);
                sameMd5File.setServerName(UUIDUtil.getUUID());
                fileService.save(sameMd5File);
                return ResultVO.success("文件保存成功", getHostPath(request) + "/file/" + sameMd5File.getServerName());
            }
        }
        // 获取文件夹信息
        FileInfo folder = fileService.getById(folderId);
        if (folder != null) {
            // 判断目录是否为文件夹
            if (!folder.getIsDir()) {
                return ResultVO.error(60002, "存储路径异常");
            }
            String fileUploadPath = uploadPath + folder.getPath();
            File uploadParentPath = new File(fileUploadPath.replace("/", File.separator));
            if (!uploadParentPath.exists()) {
                uploadParentPath.mkdirs();
                try {
                    folder.setFullPath(uploadParentPath.getCanonicalPath().replace("\\", "/"));
                } catch (IOException e) {
                    folder.setFullPath(uploadParentPath.getAbsolutePath().replace("\\", "/"));
                }
                fileService.updateById(folder);
            }

            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            long size = file.getSize();
            String fileName = UUIDUtil.getUUID();
            String filePath = fileUploadPath + File.separator + fileName + "." + type;
            File uploadFile = new File(filePath);
            try {
                file.transferTo(uploadFile);
                fileService.saveFileInfo(getHostPath(request), md5, folder, title, fileName, type, size, filePath);
                return ResultVO.success("文件保存成功", getHostPath(request) + "/file/" + fileName);
            } catch (IOException e) {
                return ResultVO.error(60003, "文件保存失败");
            }


        }
        return ResultVO.error(60001, "存储路径不存在");
    }

    @PostMapping("thumb")
    @ApiOperation(value = "上传封面", notes = "上传封面", httpMethod = "POST", produces = "application/json", consumes = "multipart/form-data")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file", paramType = "form"), @ApiImplicitParam(name = "folderId", value = "存储路径", required = true, dataType = "int", paramType = "form"), @ApiImplicitParam(name = "md5", value = "文件MD5", required = true, dataType = "string", paramType = "form")})
    @ApiResponses({@ApiResponse(code = 200, message = "文件保存成功"), @ApiResponse(code = 60001, message = "存储路径不存在"), @ApiResponse(code = 60002, message = "存储路径异常"), @ApiResponse(code = 60003, message = "文件保存失败"),})
    public ResultVO<Map<String, String>> thumb(@RequestParam MultipartFile file, @RequestParam String md5, HttpServletRequest request) {
        Map<String, String> data = new HashMap<>();
        // 判断该目录下相同MD5文件是否存在
        List<FileInfo> md5List = fileService.getByMd5(md5);
        if (!md5List.isEmpty()) {
            FileInfo sameMd5File = md5List.get(0);
            data.put("url", getHostPath(request) + "/file/" + sameMd5File.getServerName());
            data.put("name", sameMd5File.getServerName());
            return ResultVO.success("文件保存成功", data);
        }
        String originalFilename = file.getOriginalFilename();
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            File uploadFile = File.createTempFile(UUIDUtil.getUUID(), "." + type);
            file.transferTo(uploadFile);
            // 图片对象
            BufferedImage bufferedImage = ImageIO.read(Files.newInputStream(uploadFile.toPath()));
            // 宽度
            int width = bufferedImage.getWidth();
            // 高度
            int height = bufferedImage.getHeight();
            String fileName = fileService.saveThumbInfo(type, uploadFile, width, height);

            data.put("url", getHostPath(request) + "/file/" + fileName);
            data.put("name", fileName);
            return ResultVO.success("文件保存成功", data);
        } catch (IOException e) {
            return ResultVO.error(60003, "文件保存失败");
        }
    }

    @GetMapping("{fileName}")
    @ApiOperation(value = "下载文件", notes = "下载文件", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileName", value = "文件名", required = true, dataType = "string", paramType = "path")})
    public void downloadFile(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileInfo fileInfo = fileService.getOneByServerName(fileName);
        System.out.println(fileInfo);
        if (fileInfo == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(404);
            response.getWriter().println(JSON.toJSONString(ResultVO.error(60003, "文件信息不存在")));
            return;
        }
        String path = (uploadPath + fileInfo.getPath() + "." + fileInfo.getSuffix()).replace("/", File.separator);
        String range = request.getHeader("Range");
        response.setContentType("application/octet-stream;charset=utf-8");
        if (range != null && range.startsWith("bytes=")) {
            fileChunkDownload(path, fileInfo.getTitle(), request, response);
        } else {
            File file = new File(path.replace("/", File.separator));
            if (!file.exists()) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(404);
                response.getWriter().println(JSON.toJSONString(ResultVO.error(60003, "文件不存在")));
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
     * 文件支持分块下载和断点续传
     *
     * @param filePath 文件完整路径
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
        log.info("文件开始位置：{}，文件结束位置：{}，文件总长度：{}({})", startByte, endByte, file.length(), FileUtil.fileSizeByteToM(file.length()));

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

        //文件类型
        String contentType = request.getServletContext().getMimeType(fileName);

        // 解决下载文件时文件名乱码问题
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);

        //各种响应头设置
        //支持断点续传，获取部分字节内容：
        response.setHeader("Accept-Ranges", "bytes");
        //http状态码要为206：表示获取部分内容
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        //inline表示浏览器直接使用，attachment表示下载，fileName表示下载的文件名
        response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(contentLength));
        // Content-Range，格式为：[要下载的开始位置]-[结束位置]/[文件总大小]
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

    private String getHostPath(HttpServletRequest request) {
        return String.format("%s://%s%s", request.getScheme(), request.getServerName(), request.getServerPort() != 80 ? (":" + request.getServerPort()) : "");
    }

    @GetMapping
    @ApiOperation(value = "获取文件列表", notes = "获取文件列表", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "fid", value = "文件夹ID", dataType = "int", paramType = "query"), @ApiImplicitParam(name = "path", value = "存储路径", required = true, dataType = "string", paramType = "query"), @ApiImplicitParam(name = "current", value = "当前页", defaultValue = "1", dataType = "int", paramType = "query"), @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "5", dataType = "int", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "获取文件列表成功")})
    public ResultVO<MyPage<FileInfo>> getListByPath(@RequestParam(required = false) Integer fid, @RequestParam(defaultValue = "/资源库") String path, @RequestParam(required = false) String type, @RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, HttpServletRequest request) {
        if (fid != null)
            return ResultVO.success("获取文件列表成功", fileService.getFileListByFolderId(fid, type, current, size, getHostPath(request)));
        return ResultVO.success("获取文件列表成功", fileService.getFileListByPath(path, type, current, size, getHostPath(request)));
    }

    @GetMapping("folder/list")
    public ResultVO<Page<FileInfo>> getFolderList(@RequestParam(required = false) Integer fid, @RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "5") int size, HttpServletRequest request) {
        return ResultVO.success("获取文件夹列表成功", fileService.getFolderList(fid, current, size, getHostPath(request)));
    }

    @GetMapping("detail/{id}")
    @ApiOperation(value = "获取文件信息", notes = "获取文件信息", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "文件ID", dataType = "int", paramType = "path")})
    @ApiResponses({@ApiResponse(code = 200, message = "获取文件信息成功")})
    public ResultVO<FileInfo> getFileInfoById(@PathVariable Integer id, HttpServletRequest request) {
        if (id != null)
            return ResultVO.success("获取文件信息成功", fileService.getFileInfo(id, getHostPath(request)));
        return ResultVO.error(40001, "参数不正确");
    }

    @GetMapping("folder")
    @ApiOperation(value = "获取文件夹信息", notes = "获取文件夹信息", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "path", value = "存储路径", required = true, dataType = "string", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "获取文件夹信息成功"), @ApiResponse(code = 60001, message = "存储路径不存在")})
    public ResultVO<FileInfo> getFolder(@RequestParam(required = false) Integer id, @RequestParam(defaultValue = "/资源库") String path) {
        FileInfo folder;
        if (id != null) {
            folder = fileService.getById(id);
        } else {
            folder = fileService.getFolderInfoByPath(path);
        }
        if (folder == null) {
            return ResultVO.error(60001, "存储路径不存在");
        }
        return ResultVO.success("获取文件夹信息成功", folder);
    }

    // 获取文件元数据
    @GetMapping("meta")
    @ApiOperation(value = "获取文件元数据", notes = "获取文件元数据", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "fid", value = "文件ID", required = true, dataType = "int", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "获取文件元数据成功")})
    public ResultVO<List<FileMeta>> getMeta(@RequestParam int fid, @RequestParam(defaultValue = "0") int status) {
        List<FileMeta> fileMetaList = fileService.getFileMetaByFid(fid, status);
        return ResultVO.success("获取文件元数据成功", fileMetaList);
    }

    @GetMapping("type")
    public ResultVO<List<TreeNode>> getFileTypeList(@RequestParam(defaultValue = "0") int fid, @RequestParam(defaultValue = "false") Boolean lazy) {
        try {
            List<TreeNode> list = fileService.getTypeListById(fid, lazy);
            return ResultVO.success("数据获取成功", list);
        } catch (Exception e) {
            return ResultVO.error(60006, e.getMessage());
        }
    }

    /**
     * 新建文件夹
     *
     * @param title 文件夹标题
     * @return 是否创建成功
     */

    @PostMapping("folder")
    public ResultVO<String> createFolder(String title, @RequestParam(defaultValue = "0") int folderId) {
        try {
            fileService.createFolder(title, folderId);
        } catch (Exception e) {
            return ResultVO.error(60005, e.getMessage());
        }
        return ResultVO.success("文件夹创建成功");
    }

    // 删除元数据
    @DeleteMapping("meta/{id}")
    @ApiOperation(value = "删除文件元数据", notes = "删除文件元数据", httpMethod = "DELETE", produces = "application/json")
    @ApiImplicitParams({

    })
    @ApiResponses({@ApiResponse(code = 200, message = "删除文件元数据成功")})
    public ResultVO<String> deleteMeta(@PathVariable("id") int id) {
        fileService.deleteFileMetaById(id);
        return ResultVO.success("删除文件元数据成功");
    }


    @PutMapping("{id}")
    @ApiOperation(value = "更新文件信息", notes = "更新文件信息", httpMethod = "PUT", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "文件ID", required = true, dataType = "int", paramType = "path"), @ApiImplicitParam(name = "fileInfo", value = "文件信息", required = true, dataType = "FileInfo", paramType = "body")})
    @ApiResponses({@ApiResponse(code = 200, message = "更新文件信息成功"), @ApiResponse(code = 60003, message = "更新文件信息失败，文件不存在")})
    public ResultVO<String> updateFileInfo(@PathVariable int id, @RequestBody FileInfo fileInfo) {
        if (fileService.updateFileInfoById(id, fileInfo)) return ResultVO.success("更新文件信息成功");
        return ResultVO.error(60003, "更新文件信息失败，文件不存在");
    }


    @DeleteMapping("{id}")
    @ApiOperation(value = "删除文件", notes = "删除文件", httpMethod = "DELETE", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "文件ID", required = true, dataType = "int", paramType = "path")})
    @ApiResponses({@ApiResponse(code = 200, message = "删除文件成功")})
    public ResultVO<String> delete(@PathVariable int id) {
        try {
            fileService.deleteFileById(id);
        } catch (Exception e) {
            return ResultVO.error(6005, e.getMessage());
        }
        return ResultVO.success("删除文件成功");
    }

    @DeleteMapping
    @ApiOperation(value = "批量删除文件", notes = "删除文件", httpMethod = "DELETE", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "文件ID", required = true, dataType = "int", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "删除文件成功")})
    public ResultVO<String> delete(@RequestBody List<Integer> ids) {
        if (fileService.deleteFileByIds(ids)) return ResultVO.success("删除文件成功");
        return ResultVO.error(60004, "部分文件删除失败，文件不存在");
    }

}
