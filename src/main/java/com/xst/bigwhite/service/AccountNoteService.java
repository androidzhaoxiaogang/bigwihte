package com.xst.bigwhite.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.xst.bigwhite.models.AccountNote;
import com.xst.bigwhite.models.QAccount;
import com.xst.bigwhite.models.QAccountNote;
import com.xst.bigwhite.models.QDevice;

@Service("accountNoteService")
public class AccountNoteService {
	
	@PersistenceContext
    private EntityManager entityManager;

	public Iterable<AccountNote> findAccountNoteByMobileno(String mobileno, String deviceno,String note_mobileno) {
		QAccountNote qAccountNote = QAccountNote.accountNote;
		
		QAccount qAccount = QAccount.account;
		QAccount qAccount2 = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression bAccount = qAccount.mobileno.eq(mobileno);
		BooleanExpression bDevice = qDevice.no.eq(deviceno);
		// Iterable<AccountDevice> accountList =
		// accountDeviceRepository.findAll(bDeviceno);
		JPAQuery query = new JPAQuery(entityManager);
		
		Iterable<AccountNote> accountNotes = query.from(qAccountNote)
				                                     .leftJoin(qAccountNote.account,qAccount).fetch()
				                                     .leftJoin(qAccountNote.device,qDevice).fetch()
				                                     //.leftJoin(qAccountNote.contact,qAccount2).fetch()
				                                     .where(bAccount.and(bDevice))
				                                     .list(qAccountNote);

		return accountNotes;
	}

	public Iterable<AccountNote> getAccountNotesByAccountMobileAndDevice(String mobileno, String deviceno) {
		QAccountNote qAccountNote = QAccountNote.accountNote;
		
		QAccount qAccount = QAccount.account;
		QAccount qAccount2 = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression bAccount = qAccount.mobileno.eq(mobileno);
		BooleanExpression bDevice = qDevice.no.eq(deviceno);
		// Iterable<AccountDevice> accountList =
		// accountDeviceRepository.findAll(bDeviceno);
		JPAQuery query = new JPAQuery(entityManager);
		
		Iterable<AccountNote> accountNotes = query.from(qAccountNote)
				                                     .rightJoin(qAccountNote.account,qAccount).fetch()
				                                     .leftJoin(qAccountNote.device,qDevice).fetch()
				                                     //.leftJoin(qAccountNote.contact,qAccount2).fetch()
				                                     .where(bAccount.and(bDevice))
				                                     .list(qAccountNote);

		return accountNotes;
	}

	
}
