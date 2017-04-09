package com.rosense.module.system.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.CardForm;

public interface ICardService {

	public Msg importFile(List<CardForm> importUserList);
	public DataGrid datagridcard(CardForm form);
	public Msg addUserCard(CardForm form);
	public Msg update(CardForm form);
	public DataGrid orgdatagridcard(HttpSession session,CardForm form);
}
