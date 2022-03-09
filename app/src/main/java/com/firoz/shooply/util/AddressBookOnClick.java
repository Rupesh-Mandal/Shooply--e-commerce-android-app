package com.firoz.shooply.util;

import com.firoz.shooply.model.AddressBookModel;

public interface AddressBookOnClick {
    void onEdit(AddressBookModel addressBookModel);
    void onDelete(AddressBookModel addressBookModel);
    void onSetDefault(AddressBookModel addressBookModel);

}
