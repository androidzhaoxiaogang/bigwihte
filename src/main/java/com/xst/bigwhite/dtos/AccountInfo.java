package com.xst.bigwhite.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.xst.bigwhite.models.ConferenceAccount;

public class AccountInfo {
	/**
	 * 手机号
	 */
	public String mobileno;
	
	
	/**
	 * 用户名
	 */
	public String username;
	
	 /**
     * 
     * @return
     */
    public String nick;
    
    

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public static List<AccountInfo> mappingList(Set<ConferenceAccount> accounts) {
		List<AccountInfo> items = new ArrayList<>();
		
		if(accounts!=null && !accounts.isEmpty()){
		     for(ConferenceAccount account : accounts){
		    	 AccountInfo item = AccountInfo.mapping(account);
		    	 items.add(item);
		     }
		}
		
		return items;
	}

	private static AccountInfo mapping(ConferenceAccount account) {
		AccountInfo item = new AccountInfo();
		
		item.setMobileno(account.getAccount().mobileno);
		item.setNick(account.getAccount().username);
		item.setUsername(account.getAccount().username);
		
		return item;
	}
}
