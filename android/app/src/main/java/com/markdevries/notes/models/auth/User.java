package com.markdevries.notes.models.auth;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by mark on 2/15/16.
 */
@Table(name = "Users", id = BaseColumns._ID)
public class User extends Model {

    public enum AccountType {
        USER("user");

        String value;

        AccountType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static AccountType fromValue(String value) {
            for (AccountType type : AccountType.values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }

            return USER;
        }
    }

    @Column(name = "Username", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String username;

    //we dont store this.
    public String password;

    public AccountType accountType = AccountType.USER;

}
