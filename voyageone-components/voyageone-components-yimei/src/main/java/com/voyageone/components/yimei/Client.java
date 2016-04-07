package com.voyageone.components.yimei;

import com.voyageone.components.yimei.eucp.Mo;
import com.voyageone.components.yimei.eucp.SDKServiceBindingStub;
import com.voyageone.components.yimei.eucp.SDKServiceLocator;
import com.voyageone.components.yimei.eucp.StatusReport;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public class Client {

	private String softwareSerialNo;
	private String key;

	public Client(String sn, String key){
		this.softwareSerialNo=sn;
		this.key=key;
		init();
	}
	
	SDKServiceBindingStub binding;
	
	public void init(){
		 try {
            binding = (SDKServiceBindingStub)new SDKServiceLocator().getSDKService();
		 } catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
	}
	
	public int chargeUp(  String cardNo,String cardPass)throws RemoteException {
		return binding.chargeUp(softwareSerialNo, key, cardNo, cardPass);
	}

	public double getBalance() throws RemoteException {
		return binding.getBalance(softwareSerialNo, key);
	}

	public double getEachFee( ) throws RemoteException {
		return binding.getEachFee(softwareSerialNo, key);
	}
	public List<Mo> getMO( ) throws RemoteException {
		Mo[] mo=binding.getMO(softwareSerialNo, key);
		
		if(null == mo){
			return null;
		} else {
			return Arrays.asList(mo);
		}
	}
	

	public List<StatusReport> getReport( ) throws RemoteException {
		StatusReport[] sr = binding.getReport(softwareSerialNo, key);
		if(null!=sr){
			return Arrays.asList(sr);
		}else{
			return null;
		}
	}


	public int logout( ) throws RemoteException {
		return binding.logout(softwareSerialNo, key);
	}

	public int registDetailInfo(String eName, String linkMan, String phoneNum, String mobile,
			String email, String fax, String address, String postcode) throws RemoteException {
		return binding.registDetailInfo(softwareSerialNo, key, eName, linkMan, phoneNum, mobile, email, fax, address, postcode);
	}

	public int registEx(String password) throws RemoteException {
		return binding.registEx(softwareSerialNo, key, password);
	}

	public int sendSMS( String[] mobiles, String smsContent, String addSerial,int smsPriority) throws RemoteException {
		return binding.sendSMS(softwareSerialNo, key,"", mobiles, smsContent, addSerial, "gbk", smsPriority,0);
	}
	
	public int sendScheduledSMSEx(String[] mobiles, String smsContent,String sendTime,String srcCharset) throws RemoteException {
		return binding.sendSMS(softwareSerialNo, key, sendTime, mobiles, smsContent, "", srcCharset, 3,0);
	}
	public int sendSMSEx(String[] mobiles, String smsContent, String addSerial,String srcCharset, int smsPriority,long smsID) throws RemoteException {
		return binding.sendSMS(softwareSerialNo, key,"", mobiles, smsContent,addSerial, srcCharset, smsPriority,smsID);
	}

	public String sendVoice(String[] mobiles, String smsContent, String addSerial,String srcCharset, int smsPriority,long smsID) throws RemoteException {
		return binding.sendVoice(softwareSerialNo, key,"", mobiles, smsContent,addSerial, srcCharset, smsPriority,smsID);
	}
	
	public int serialPwdUpd( String serialPwd, String serialPwdNew) throws RemoteException {
		return binding.serialPwdUpd(softwareSerialNo, key, serialPwd, serialPwdNew);
	}
}
