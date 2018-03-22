package com.enation.app.ext.core.service;

import java.util.List;

import com.enation.app.shop.core.model.support.CartItem;

public abstract interface INewCartManager {

	public abstract List<CartItem> listGoods(String paramString);
}
