/**
 * <b>包名：</b>com.nnk.log.analuzer.client.common<br/>
 * <b>文件名：</b>GZipUtil.java<br/>
 * <b>版本信息：</b>1.0<br/>
 * <b>日期：</b>2014-1-9-上午10:02:50<br/>
 * <b>Copyright (c)</b> 2014 深圳市年年卡网络科技有限公司-版权所有<br/>
 * 
 */
package com.msgsrv.log.analyzer.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

/**
 * 
 * <b>类名称：</b>GZipUtil<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改人：</b>yuanxiangwu520@126.com<br/>
 * <b>修改时间：</b>2014-1-9 上午10:02:50<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class GZipUtil {

	private static final int BUFF_SIZE = 1024;

	public static File unTargzFile(File zipFile) {
		FileInputStream fis = null;
		ArchiveInputStream in = null;
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		File file = null;
		try {
			fis = new FileInputStream(zipFile);
			GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
			in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
			bufferedInputStream = new BufferedInputStream(in);
			TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
			while (entry != null) {
				file = new File(zipFile.getParent() + "/" + entry.getName());
				bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
				int length;
				byte[] data = new byte[BUFF_SIZE];
				while ((length = bufferedInputStream.read(data)) > 0) {
					bufferedOutputStream.write(data, 0, length);
				}
				bufferedOutputStream.flush();
				entry = (TarArchiveEntry) in.getNextEntry();
			}
		} catch (Exception e) {
		} finally {
			try {
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
			} catch (Exception e) {
			}
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (Exception e) {
			}
		}
		return file;
	}
}
