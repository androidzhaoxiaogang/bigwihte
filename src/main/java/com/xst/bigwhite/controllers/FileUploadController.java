package com.xst.bigwhite.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties.Headers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xst.bigwhite.daos.AccountDeviceRepository;
import com.xst.bigwhite.daos.AccountRepository;
import com.xst.bigwhite.daos.DeviceRepository;
import com.xst.bigwhite.daos.PictureRepository;
import com.xst.bigwhite.daos.VerifyMessageRepository;
import com.xst.bigwhite.exception.RestRuntimeException;
import com.xst.bigwhite.models.Account;
import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.models.Picture;
import com.xst.bigwhite.utils.Helpers;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/upload")
public class FileUploadController {
	private final PictureRepository pictureRepository;
	private final DeviceRepository deviceRepository;
	private final AccountRepository accountRepository;
	private final VerifyMessageRepository verifyMessageRepository;
	private final AccountDeviceRepository accountDeviceRepository;
	private final String uploadFolder =  "/upload";
	
	@Autowired
	FileUploadController(AccountRepository accountRepository,
			DeviceRepository deviceRepository,
			VerifyMessageRepository verifyMessageRepository,
			AccountDeviceRepository accountDeviceRepository,
			PictureRepository pictureRepository) {
		this.deviceRepository = deviceRepository;
		this.accountRepository = accountRepository;
		this.verifyMessageRepository = verifyMessageRepository;
		this.accountDeviceRepository = accountDeviceRepository;
		this.pictureRepository = pictureRepository;
	}
	
	/**
	 *  图片上传不支持GET
	 * @return
	 */
	@RequestMapping(value="/image", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
	
	/**
	 * 上传用户的头像
	 * @param no   mobileno
	 * @param file bytes[]
	 * @return
	 */
    @RequestMapping(value="/accountimage", method = RequestMethod.POST)
    public @ResponseBody String handleAccountImageFileUpload(@RequestParam("mobileno") String mobileno,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response){
    	
    	 if (!file.isEmpty()) {
             try {
                String basePath = Helpers.getPath(request, uploadFolder);
                
             	File destinationDir = new File(basePath);
             	if(!destinationDir.exists()  && !destinationDir.isDirectory()){
             		destinationDir.mkdir();
             	}
             	 
             	String filename = Helpers.getFileName() + ".jpg";
             	String fullname =  basePath + filename;
             	String thumbnail = "thumbnail-" + filename;
             	
                 byte[] bytes = file.getBytes();
                 BufferedOutputStream stream =
                         new BufferedOutputStream(new FileOutputStream(new File(fullname)));
                 stream.write(bytes);
                 stream.close();
                 
                
                 Thumbnails.of(filename)
                         .size(200, 200)
                         .toFiles(destinationDir, Rename.PREFIX_HYPHEN_THUMBNAIL);
                 
                 Picture picture = new Picture(uploadFolder + filename, destinationDir + filename);
                 pictureRepository.save(picture);
                 
                 Optional<Account> accouted = accountRepository.findByMobileno(mobileno);
                 if(accouted.isPresent()){
                	 Account account = accouted.get();
                	 account.headimage = uploadFolder + thumbnail;
                	 accountRepository.save(account);
                	 
                 }else{
                	 throw new RestRuntimeException ("图片上传失败! 手机号:" + mobileno + " 不存在! ");
                 }
                 
                 return Helpers.getBasePath(request, uploadFolder);
             } catch (Exception e) {
                 throw new RestRuntimeException ("图片上传失败! 手机号:" + mobileno + " => " + e.getMessage());
             }
         } else {
         	 throw new RestRuntimeException ("图片上传失败! 手机号:" + mobileno + ",图片文件内容为空!");
         }
    }

    

	/**
	 * 上传大白的头像
	 * @param no   deviceno
	 * @param file bytes[]
	 * @return
	 */
    @RequestMapping(value="/deviceimage", method = RequestMethod.POST)
    public @ResponseBody String handleDeviceImageFileUpload(@RequestParam("deviceno") String deviceno,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response){
    	
    	 if (!file.isEmpty()) {
             try {
                String basePath = Helpers.getPath(request, uploadFolder);
                
             	File destinationDir = new File(basePath);
             	if(!destinationDir.exists()  && !destinationDir.isDirectory()){
             		destinationDir.mkdir();
             	}
             	 
             	String filename = Helpers.getFileName() + ".jpg";
             	String fullname =  basePath + filename;
             	String thumbnail = "thumbnail-" + filename;
             	
                 byte[] bytes = file.getBytes();
                 BufferedOutputStream stream =
                         new BufferedOutputStream(new FileOutputStream(new File(fullname)));
                 stream.write(bytes);
                 stream.close();
                 
                
                 Thumbnails.of(filename)
                         .size(200, 200)
                         .toFiles(destinationDir, Rename.PREFIX_HYPHEN_THUMBNAIL);
                 
                 Picture picture = new Picture(uploadFolder + filename, destinationDir + filename);
                 pictureRepository.save(picture);
                 
                 Optional<Device> deviced = deviceRepository.findByno(deviceno);
                 if(deviced.isPresent()){
                	 Device device = deviced.get();
                	 device.headimage = uploadFolder + thumbnail;
                	 deviceRepository.save(device);
                	 
                 }else{
                	 throw new RestRuntimeException ("上传图片失败!设备号:" + deviceno + " 不存在! ");
                 }
                 
                 return Helpers.getBasePath(request, uploadFolder);
             } catch (Exception e) {
                 throw new RestRuntimeException ("上传图片失败!设备号:" + deviceno + " => " + e.getMessage());
             }
         } else {
         	 throw new RestRuntimeException ("上传图片失败!设备号:" + deviceno + ",图片文件内容为空!");
         }
    }

	/**
	 * 上传用户的头像或者大白的头像
	 * @param no   固定值 : mobile 或  bigwhite
	 * @param file bytes[]
	 * @return
	 */
    @RequestMapping(value="/image", method = RequestMethod.POST)
    public @ResponseBody String handleImageFileUpload(@RequestParam("no") String no,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response){
        if (!file.isEmpty()) {
            try {
                String basePath = Helpers.getPath(request, "/upload");
            	File destinationDir = new File(basePath);
            	if(!destinationDir.exists()  && !destinationDir.isDirectory()){
            		destinationDir.mkdir();
            	}
            	 
            	String filename = basePath + Helpers.getFileName() + ".jpg";
            	String thumbnail = "thumbnail-" + filename;
            	File imageFile = new File(filename);
            	
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(imageFile));
                stream.write(bytes);
                stream.close();
                
               
                Thumbnails.of(filename)
                        .size(200, 200)
                        .toFiles(destinationDir, Rename.PREFIX_HYPHEN_THUMBNAIL);
                
                return thumbnail;
            } catch (Exception e) {
                throw new RestRuntimeException ("You failed to upload " + no + " => " + e.getMessage());
            }
        } else {
        	 throw new RestRuntimeException ("You failed to upload " + no + " because the file was empty.");
        }
    }
}
