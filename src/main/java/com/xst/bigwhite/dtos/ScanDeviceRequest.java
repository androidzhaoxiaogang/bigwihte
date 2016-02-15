package com.xst.bigwhite.dtos;

/**
 * 扫描二位码加入大白
 * @author wangjun
 *
 */
public class ScanDeviceRequest extends DeviceInfoRequest {
	
	/**
	 * 是扫描二维码还是输入文字
	 */
	private ScanInputType scanType = ScanInputType.ScanQR;
	
	public ScanInputType getScanType() {
		return scanType;
	}

	public void setScanType(ScanInputType scanType) {
		this.scanType = scanType;
	}
    
	
}
